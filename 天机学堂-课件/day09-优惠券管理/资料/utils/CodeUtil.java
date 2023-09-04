package com.tianji.promotion.utils;

import com.tianji.common.constants.RegexConstants;
import com.tianji.common.exceptions.BadRequestException;

/**
 * <h1 style='font-weight:500'>1.兑换码算法说明：</h1>
 * <p>兑换码分为明文和密文，明文是50位二进制数，密文是长度为10的Base32编码的字符串 </p>
 * <h1 style='font-weight:500'>2.兑换码的明文结构：</h1>
 * <p style='padding: 0 15px'>14(校验码) + 4 (新鲜值) + 32(序列号) </p>
 *   <ul style='padding: 0 15px'>
 *       <li>序列号：一个单调递增的数字，可以通过Redis来生成</li>
 *       <li>新鲜值：可以是优惠券id的最后4位，同一张优惠券的兑换码就会有一个相同标记</li>
 *       <li>载荷：将新鲜值（4位）拼接序列号（32位）得到载荷</li>
 *       <li>校验码：将载荷4位一组，每组乘以加权数，最后累加求和，然后对2^14求余得到</li>
 *   </ul>
 *  <h1 style='font-weight:500'>3.兑换码的加密过程：</h1>
 *     <ol type='a' style='padding: 0 15px'>
 *         <li>首先利用优惠券id计算新鲜值 f</li>
 *         <li>将f和序列号s拼接，得到载荷payload</li>
 *         <li>然后以f为角标，从提前准备好的16组加权码表中选一组</li>
 *         <li>对payload做加权计算，得到校验码 c  </li>
 *         <li>利用c的后4位做角标，从提前准备好的异或密钥表中选择一个密钥：key</li>
 *         <li>将payload与key做异或，作为新payload2</li>
 *         <li>然后拼接兑换码明文：f (4位) + payload2（36位）</li>
 *         <li>利用Base32对密文转码，生成兑换码</li>
 *     </ol>
 * <h1 style='font-weight:500'>4.兑换码的解密过程：</h1>
 * <ol type='a' style='padding: 0 15px'>
 *      <li>首先利用Base32解码兑换码，得到明文数值num</li>
 *      <li>取num的高14位得到c1，取num低36位得payload </li>
 *      <li>利用c1的后4位做角标，从提前准备好的异或密钥表中选择一个密钥：key</li>
 *      <li>将payload与key做异或，作为新payload2</li>
 *      <li>利用加密时的算法，用payload2和s1计算出新校验码c2，把c1和c2比较，一致则通过 </li>
 * </ol>
 */
public class CodeUtil {
    /**
     * 异或密钥表，用于最后的数据混淆
     */
    private final static long[] XOR_TABLE = {
            61261925471L, 61261925523L, 58169127203L, 64169927267L,
            64169927199L, 61261925629L, 58169127227L, 64169927363L,
            59169127063L, 64169927359L, 58169127291L, 61261925739L,
            59169127133L, 55139281911L, 56169127077L, 59169127167L
    };
    /**
     * fresh值的偏移位数
     */
    private final static int FRESH_BIT_OFFSET = 32;
    /**
     * 校验码的偏移位数
     */
    private final static int CHECK_CODE_BIT_OFFSET = 36;
    /**
     * fresh值的掩码，4位
     */
    private final static int FRESH_MASK = 0xF;
    /**
     * 验证码的掩码，14位
     */
    private final static int CHECK_CODE_MASK = 0b11111111111111;
    /**
     * 载荷的掩码，36位
     */
    private final static long PAYLOAD_MASK = 0xFFFFFFFFFL;
    /**
     * 序列号掩码，32位
     */
    private final static long SERIAL_NUM_MASK = 0xFFFFFFFFL;
    /**
     * 序列号加权运算的秘钥表
     */
    private final static int[][] PRIME_TABLE = {
            {23, 59, 241, 61, 607, 67, 977, 1217, 1289, 1601},
            {79, 83, 107, 439, 313, 619, 911, 1049, 1237},
            {173, 211, 499, 673, 823, 941, 1039, 1213, 1429, 1259},
            {31, 293, 311, 349, 431, 577, 757, 883, 1009, 1657},
            {353, 23, 367, 499, 599, 661, 719, 929, 1301, 1511},
            {103, 179, 353, 467, 577, 691, 811, 947, 1153, 1453},
            {213, 439, 257, 313, 571, 619, 743, 829, 983, 1103},
            {31, 151, 241, 349, 607, 677, 769, 823, 967, 1049},
            {61, 83, 109, 137, 151, 521, 701, 827, 1123},
            {23, 61, 199, 223, 479, 647, 739, 811, 947, 1019},
            {31, 109, 311, 467, 613, 743, 821, 881, 1031, 1171},
            {41, 173, 367, 401, 569, 683, 761, 883, 1009, 1181},
            {127, 283, 467, 577, 661, 773, 881, 967, 1097, 1289},
            {59, 137, 257, 347, 439, 547, 641, 839, 977, 1009},
            {61, 199, 313, 421, 613, 739, 827, 941, 1087, 1307},
            {19, 127, 241, 353, 499, 607, 811, 919, 1031, 1301}
    };

    /**
     * 生成兑换码
     *
     * @param serialNum 递增序列号
     * @return 兑换码
     */
    public static String generateCode(long serialNum, long fresh) {
        // 1.计算新鲜值
        fresh = fresh & FRESH_MASK;
        // 2.拼接payload，fresh（4位） + serialNum（32位）
        long payload = fresh << FRESH_BIT_OFFSET | serialNum;
        // 3.计算验证码
        long checkCode = calcCheckCode(payload, (int) fresh);
        System.out.println("checkCode = " + checkCode);
        // 4.payload做大质数异或运算，混淆数据
        payload ^= XOR_TABLE[(int) (checkCode & FRESH_MASK)];
        // 5.拼接兑换码明文: 校验码（14位） + payload（36位）
        long code = checkCode << CHECK_CODE_BIT_OFFSET | payload;
        // 6.转码
        return Base32.encode(code);
    }

    private static long calcCheckCode(long payload, int fresh) {
        // 1.获取码表
        int[] table = PRIME_TABLE[fresh];
        // 2.生成校验码，payload每4位乘加权数，求和，取最后13位结果
        long sum = 0;
        int index = 0;
        while (payload > 0) {
            sum += (payload & 0xf) * table[index++];
            payload >>>= 4;
        }
        return sum & CHECK_CODE_MASK;
    }

    public static long parseCode(String code) {
        if (code == null || !code.matches(RegexConstants.COUPON_CODE_PATTERN)) {
            // 兑换码格式错误
            throw new BadRequestException("无效兑换码");
        }
        // 1.Base32解码
        long num = Base32.decode(code);
        // 2.获取低36位，payload
        long payload = num & PAYLOAD_MASK;
        // 3.获取高14位，校验码
        int checkCode = (int) (num >>> CHECK_CODE_BIT_OFFSET);
        // 4.载荷异或大质数，解析出原来的payload
        payload ^= XOR_TABLE[(checkCode & FRESH_MASK)];
        // 5.获取高4位，fresh
        int fresh = (int) (payload >>> FRESH_BIT_OFFSET & FRESH_MASK);
        // 6.验证格式：
        if (calcCheckCode(payload, fresh) != checkCode) {
            throw new BadRequestException("无效兑换码");
        }
        return payload & SERIAL_NUM_MASK;
    }
}
