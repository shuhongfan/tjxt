package com.tianji.promotion.utils;

import java.nio.ByteOrder;
import java.nio.charset.Charset;

/**
 * 数字转字节数组工具类
 */
public class BitConverter {

    /**
     * 以字节数组的形式返回指定的布尔值
     * @param data 一个布尔值
     * @return 长度为 1 的字节数组
     */
    public static byte[] getBytes(boolean data) {
        byte[] bytes = new byte[1];
        bytes[0] = (byte) (data ? 1 : 0);
        return bytes;
    }
    
    /**
     * 以字节数组的形式返回指定的 16 位有符号整数值
     * @param data 要转换的数字
     * @return 长度为 2 的字节数组
     */
    public static byte[] getBytes(short data) {
        byte[] bytes = new byte[2];
        if (isLittleEndian()) {
            bytes[0] = (byte) (data & 0xff);
            bytes[1] = (byte) ((data & 0xff00) >> 8);
        } else {
            bytes[1] = (byte) (data & 0xff);
            bytes[0] = (byte) ((data & 0xff00) >> 8);
        }
        return bytes;
    }

    /**
     * 以字节数组的形式返回指定的 Unicode 字符值
     * @param data 要转换的字符
     * @return 长度为 2 的字节数组
     */
    public static byte[] getBytes(char data) {
        byte[] bytes = new byte[2];
        if (isLittleEndian()) {
            bytes[0] = (byte) (data);
            bytes[1] = (byte) (data >> 8);
        } else {
            bytes[1] = (byte) (data);
            bytes[0] = (byte) (data >> 8);
        }
        return bytes;
    }

    /**
     * 以字节数组的形式返回指定的 32 位有符号整数值
     * @param data 要转换的数字
     * @return 长度为 4 的字节数组
     */
    public static byte[] getBytes(int data) {
        byte[] bytes = new byte[4];
        if (isLittleEndian()) {
            bytes[0] = (byte) (data & 0xff);
            bytes[1] = (byte) ((data & 0xff00) >> 8);
            bytes[2] = (byte) ((data & 0xff0000) >> 16);
            bytes[3] = (byte) ((data & 0xff000000) >> 24);
        } else {
            bytes[3] = (byte) (data & 0xff);
            bytes[2] = (byte) ((data & 0xff00) >> 8);
            bytes[1] = (byte) ((data & 0xff0000) >> 16);
            bytes[0] = (byte) ((data & 0xff000000) >> 24);
        }
        return bytes;
    }

    /**
     * 以字节数组的形式返回指定的 64 位有符号整数值
     * @param data 要转换的数字
     * @return 长度为 8 的字节数组
     */
    public static byte[] getBytes(long data) {
        byte[] bytes = new byte[8];
        if (isLittleEndian()) {
            bytes[0] = (byte) (data & 0xff);
            bytes[1] = (byte) ((data >> 8) & 0xff);
            bytes[2] = (byte) ((data >> 16) & 0xff);
            bytes[3] = (byte) ((data >> 24) & 0xff);
            bytes[4] = (byte) ((data >> 32) & 0xff);
            bytes[5] = (byte) ((data >> 40) & 0xff);
            bytes[6] = (byte) ((data >> 48) & 0xff);
            bytes[7] = (byte) ((data >> 56) & 0xff);
        } else {
            bytes[7] = (byte) (data & 0xff);
            bytes[6] = (byte) ((data >> 8) & 0xff);
            bytes[5] = (byte) ((data >> 16) & 0xff);
            bytes[4] = (byte) ((data >> 24) & 0xff);
            bytes[3] = (byte) ((data >> 32) & 0xff);
            bytes[2] = (byte) ((data >> 40) & 0xff);
            bytes[1] = (byte) ((data >> 48) & 0xff);
            bytes[0] = (byte) ((data >> 56) & 0xff);
        }
        return bytes;
    }

    /**
     * 以字节数组的形式返回指定的单精度浮点值
     * @param data 要转换的数字
     * @return 长度为 4 的字节数组
     */
    public static byte[] getBytes(float data) {
        return getBytes(Float.floatToIntBits(data));
    }

    /**
     * 以字节数组的形式返回指定的双精度浮点值
     * @param data 要转换的数字
     * @return 长度为 8 的字节数组
     */
    public static byte[] getBytes(double data) {
        return getBytes(Double.doubleToLongBits(data));
    }
    
    /**
     * 将指定字符串中的所有字符编码为一个字节序列
     * @param data 包含要编码的字符的字符串
     * @return 一个字节数组，包含对指定的字符集进行编码的结果
     */
    public static byte[] getBytes(String data) {
        return data.getBytes(Charset.forName("UTF-8"));
    }
    
    /**
     * 将指定字符串中的所有字符编码为一个字节序列
     * @param data 包含要编码的字符的字符串
     * @param charsetName 字符集编码
     * @return 一个字节数组，包含对指定的字符集进行编码的结果
     */
    public static byte[] getBytes(String data, String charsetName) {
        return data.getBytes(Charset.forName(charsetName));
    }
    
    /**
     * 返回由字节数组转换来的布尔值
     * @param bytes 字节数组
     * @return 布尔值
     */
    public static boolean toBoolean(byte[] bytes) {
        return bytes[0] == 0 ? false : true;
    }
    
    /**
     * 返回由字节数组中的指定的一个字节转换来的布尔值
     * @param bytes 字节数组
     * @param startIndex 起始下标
     * @return 布尔值
     */
    public static boolean toBoolean(byte[] bytes, int startIndex) {
        return toBoolean(copyFrom(bytes, startIndex, 1));
    }
    
    /**
     * 返回由字节数组转换来的 16 位有符号整数
     * @param bytes 字节数组
     * @return 由两个字节构成的 16 位有符号整数
     */
    public static short toShort(byte[] bytes) {
        if (isLittleEndian()) {
            return (short) ((0xff & bytes[0]) | (0xff00 & (bytes[1] << 8)));
        } else {
            return (short) ((0xff & bytes[1]) | (0xff00 & (bytes[0] << 8)));
        }
    }
    
    /**
     * 返回由字节数组中的指定的两个字节转换来的 16 位有符号整数
     * @param bytes 字节数组
     * @param startIndex 起始下标
     * @return 由两个字节构成的 16 位有符号整数
     */
    public static short toShort(byte[] bytes, int startIndex) {
        return toShort(copyFrom(bytes, startIndex, 2));
    }

    /**
     * 返回由字节数组转换来的 Unicode 字符
     * @param bytes 字节数组
     * @return 由两个字节构成的字符
     */
    public static char toChar(byte[] bytes) {
        if (isLittleEndian()) {
            return (char) ((0xff & bytes[0]) | (0xff00 & (bytes[1] << 8)));
        } else {
            return (char) ((0xff & bytes[1]) | (0xff00 & (bytes[0] << 8)));
        }
    }
    
    /**
     * 返回由字节数组中的指定的两个字节转换来的 Unicode 字符
     * @param bytes 字节数组
     * @param startIndex 起始下标
     * @return 由两个字节构成的字符
     */
    public static char toChar(byte[] bytes, int startIndex) {
        return toChar(copyFrom(bytes, startIndex, 2));
    }

    /**
     * 返回由字节数组转换来的 32 位有符号整数
     * @param bytes 字节数组
     * @return 由四个字节构成的 32 位有符号整数
     */
    public static int toInt(byte[] bytes) {
        if (isLittleEndian()) {
            return (0xff & bytes[0]) 
                    | (0xff00 & (bytes[1] << 8)) 
                    | (0xff0000 & (bytes[2] << 16))
                    | (0xff000000 & (bytes[3] << 24));
        } else {
            return (0xff & bytes[3]) 
                    | (0xff00 & (bytes[2] << 8)) 
                    | (0xff0000 & (bytes[1] << 16))
                    | (0xff000000 & (bytes[0] << 24));
        }
    }
    
    /**
     * 返回由字节数组中的指定的四个字节转换来的 32 位有符号整数
     * @param bytes 字节数组
     * @param startIndex 起始下标
     * @return 由四个字节构成的 32 位有符号整数
     */
    public static int toInt(byte[] bytes, int startIndex) {
        return toInt(copyFrom(bytes, startIndex, 4));
    }

    /**
     * 返回由字节数组转换来的 64 位有符号整数
     * @param bytes 字节数组
     * @return 由八个字节构成的 64 位有符号整数
     */
    public static long toLong(byte[] bytes) {
        if (isLittleEndian()) {
            return (0xffL & (long) bytes[0]) 
                    | (0xff00L & ((long) bytes[1] << 8)) 
                    | (0xff0000L & ((long) bytes[2] << 16))
                    | (0xff000000L & ((long) bytes[3] << 24)) 
                    | (0xff00000000L & ((long) bytes[4] << 32))
                    | (0xff0000000000L & ((long) bytes[5] << 40)) 
                    | (0xff000000000000L & ((long) bytes[6] << 48))
                    | (0xff00000000000000L & ((long) bytes[7] << 56));
        } else {
            return (0xffL & (long) bytes[7]) 
                    | (0xff00L & ((long) bytes[6] << 8)) 
                    | (0xff0000L & ((long) bytes[5] << 16))
                    | (0xff000000L & ((long) bytes[4] << 24)) 
                    | (0xff00000000L & ((long) bytes[3] << 32))
                    | (0xff0000000000L & ((long) bytes[2] << 40)) 
                    | (0xff000000000000L & ((long) bytes[1] << 48))
                    | (0xff00000000000000L & ((long) bytes[0] << 56));
        }
    }
    
    /**
     * 返回由字节数组中的指定的八个字节转换来的 64 位有符号整数
     * @param bytes 字节数组
     * @param startIndex 起始下标
     * @return 由八个字节构成的 64 位有符号整数
     */
    public static long toLong(byte[] bytes, int startIndex) {
        return toLong(copyFrom(bytes, startIndex, 8));
    }

    /**
     * 返回由字节数组转换来的单精度浮点数
     * @param bytes 字节数组
     * @return 由四个字节构成的单精度浮点数
     */
    public static float toFloat(byte[] bytes) {
        return Float.intBitsToFloat(toInt(bytes));
    }
    
    /**
     * 返回由字节数组中的指定的四个字节转换来的单精度浮点数
     * @param bytes 字节数组
     * @param startIndex 起始下标
     * @return 由四个字节构成的单精度浮点数
     */
    public static float toFloat(byte[] bytes, int startIndex) {
        return Float.intBitsToFloat(toInt(copyFrom(bytes, startIndex, 4)));
    }

    /**
     * 返回由字节数组转换来的双精度浮点数
     * @param bytes 字节数组
     * @return 由八个字节构成的双精度浮点数
     */
    public static double toDouble(byte[] bytes) {
        return Double.longBitsToDouble(toLong(bytes));
    }
    
    /**
     * 返回由字节数组中的指定的八个字节转换来的双精度浮点数
     * @param bytes 字节数组
     * @param startIndex 起始下标
     * @return 由八个字节构成的双精度浮点数
     */
    public static double toDouble(byte[] bytes, int startIndex) {
        return Double.longBitsToDouble(toLong(copyFrom(bytes, startIndex, 8)));
    }
    
    /**
     * 返回由字节数组转换来的字符串
     * @param bytes 字节数组
     * @return 字符串
     */
    public static String toString(byte[] bytes) {
        return new String(bytes, Charset.forName("UTF-8"));
    }
    
    /**
     * 返回由字节数组转换来的字符串
     * @param bytes 字节数组
     * @param charsetName 字符集编码
     * @return 字符串
     */
    public static String toString(byte[] bytes, String charsetName) {
        return new String(bytes, Charset.forName(charsetName));
    }
    
    /**
     * 以字符串表示形式返回字节数组的内容
     * @param bytes 字节数组
     * @return 字符串形式的 <tt>bytes</tt>
     */
    public static String toHexString(byte[] bytes) {
        if (bytes == null)
            return "null";
        int iMax = bytes.length - 1;
        if (iMax == -1)
            return "[]";
        String digits = "0123456789abcdef";
        StringBuilder b = new StringBuilder();
        b.append('[');
        for (int i = 0; ; i++) {
            b.append(digits.charAt((0xF0 & bytes[i]) >>> 4));
            b.append(digits.charAt(0x0F & bytes[i]));
            if (i == iMax)
                return b.append(']').toString();
            b.append(',').append(' ');
        }
    }
    

    // --------------------------------------------------------------------------------------------

 
    /**
     * 数组拷贝。
     * @param src 字节数组。
     * @param off 起始下标。
     * @param len 拷贝长度。
     * @return 指定长度的字节数组。
     */
    private static byte[] copyFrom(byte[] src, int off, int len) {
        // return Arrays.copyOfRange(src, off, off + len);
        byte[] copy = new byte[len];
        System.arraycopy(src, off, copy, 0, Math.min(src.length - off, len));
        return copy;
    }
    
    /**
     * 判断 CPU Endian 是否为 Little
     * @return 判断结果
     */
    private static boolean isLittleEndian() {
        return ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN;
    }
}
