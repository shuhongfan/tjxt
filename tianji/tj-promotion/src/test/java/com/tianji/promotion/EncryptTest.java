package com.tianji.promotion;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.HMac;
import com.tianji.common.exceptions.BadRequestException;
import com.tianji.promotion.utils.AESUtil;
import com.tianji.promotion.utils.Base32;
import com.tianji.promotion.utils.BitConverter;
import com.tianji.promotion.utils.CodeUtil;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class EncryptTest {
    private static final String CIPHER_ALGORITHM = "AES/CTR/NoPadding";
    private AESUtil aesUtil = new AESUtil("tjxt-test-key-in", "tjxt-test-ivinit");
    @Test
    void testRC4() throws NoSuchAlgorithmException, Exception {
        String encode = encode(1124);
        System.out.println("encode1 = " + encode);
        System.out.println("num1 = " + decode(encode));
        System.out.println("num1 = " + decode(encode + "A"));
        encode = encode(1125);
        System.out.println("encode2 = " + encode);
        System.out.println("num2 = " + decode(encode));
        encode = encode(1126);
        System.out.println("encode3 = " + encode);
        System.out.println("num3 = " + decode(encode));

    }

    private String encode(int serialNum){
        // 1.对序列号加密，生成32数据作为秘钥
        byte[] encrypt = aesUtil.encrypt(BitConverter.getBytes(serialNum));
        // 2.用加密密文作为 HMac的秘钥，
        HMac hMac = SecureUtil.hmacMd5(encrypt);

        // 加密结果的首尾两个字节做异或运算
        byte checkCode = (byte) (encrypt[0] ^ encrypt[3]);
        // 拼接结果
        byte[] rawCode = ArrayUtil.addAll(encrypt, new byte[]{checkCode});
        // 再次加密后转码
        return Base32.encode(aesUtil.encrypt(rawCode));
    }

    private int decode(String code){
        // Base32解码
        byte[] bytes = Base32.decode2Byte(code);
        // AES 解码
        bytes = aesUtil.decrypt(bytes);
        // 获取前4字节，是加密数据，后1字节是向量
        byte[] encrypt = Arrays.copyOfRange(bytes, 0, 5);
        byte checkCode = bytes[4];
        if(checkCode != (byte) (encrypt[0] ^ encrypt[3])){
            throw new BadRequestException("无效兑换码");
        }
        // 对序列号解密
        byte[] decrypt = aesUtil.decrypt(encrypt);
        // 转为数值
        return BitConverter.toInt(decrypt);
    }


    @Test
    void testCodeUtil() {
        CodeUtil codeUtil = new CodeUtil(new AESUtil("tjxt-test-key-in", "tjxt-test-ivinit"));

        String code1 = codeUtil.generateCode(109);
        System.out.println("code1 = " + code1);
        String code2 = codeUtil.generateCode(110);
        System.out.println("code2 = " + code2);
        String code3 = codeUtil.generateCode(111);
        System.out.println("code3 = " + code3);

        System.out.println("num2 = " + codeUtil.parseCode(code2));
        System.out.println("num3 = " + codeUtil.parseCode(code3));
        System.out.println("num1 = " + codeUtil.parseCode(code1));

        System.out.println("num = " + codeUtil.parseCode("SQBAUP2M"));
    }
}
