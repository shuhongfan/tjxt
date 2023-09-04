package com.tianji.promotion.utils;

import cn.hutool.core.text.StrBuilder;

/**
 * 将整数转为base32字符的工具，因为是32进制，所以每5个bit位转一次
 */
public class Base32 {
    private final static String baseChars = "6CSB7H8DAKXZF3N95RTMVUQG2YE4JWPL";

    public static String encode(long raw) {
        StrBuilder sb = new StrBuilder();
        while (raw != 0) {
            int i = (int) (raw & 0b11111);
            sb.append(baseChars.charAt(i));
            raw = raw >>> 5;
        }
        return sb.toString();
    }

    public static long decode(String code) {
        long r = 0;
        char[] chars = code.toCharArray();
        for (int i = chars.length - 1; i >= 0; i--) {
            long n = baseChars.indexOf(chars[i]);
            r = r | (n << (5*i));
        }
        return r;
    }

    public static String encode(byte[] raw) {
        StrBuilder sb = new StrBuilder();
        int size = 0;
        int temp = 0;
        for (byte b : raw) {
            if (size == 0) {
                // 取5个bit
                int index = (b >>> 3) & 0b11111;
                sb.append(baseChars.charAt(index));
                // 还剩下3位
                size = 3;
                temp = b & 0b111;
            } else {
                int index = temp << (5 - size) | (b >>> (3 + size) & ((1 << 5 - size) - 1)) ;
                sb.append(baseChars.charAt(index));
                int left = 3 + size;
                size = 0;
                if(left >= 5){
                    index = b >>> (left - 5) & ((1 << 5) - 1);
                    sb.append(baseChars.charAt(index));
                    left = left - 5;
                }
                if(left == 0){
                    continue;
                }
                temp = b & ((1 << left) - 1);
                size = left;
            }
        }
        if(size > 0){
            sb.append(baseChars.charAt(temp));
        }
        return sb.toString();
    }

    public static byte[] decode2Byte(String code) {
        char[] chars = code.toCharArray();
        byte[] bytes = new byte[(code.length() * 5 )/ 8];
        byte tmp = 0;
        byte byteSize = 0;
        int index = 0;
        int i = 0;
        for (char c : chars) {
            byte n = (byte) baseChars.indexOf(c);
            i++;
            if (byteSize == 0) {
                tmp = n;
                byteSize = 5;
            } else {
                int left = Math.min(8 - byteSize, 5);
                if(i == chars.length){
                    bytes[index] =(byte) (tmp << left | (n & ((1 << left) - 1)));
                    break;
                }
                tmp = (byte) (tmp << left | (n >>> (5 - left)));
                byteSize += left;
                if (byteSize >= 8) {
                    bytes[index++] = tmp;
                    byteSize = (byte) (5 - left);
                    if (byteSize == 0) {
                        tmp = 0;
                    } else {
                        tmp = (byte) (n & ((1 << byteSize) - 1));
                    }
                }
            }
        }
        return bytes;
    }
}
