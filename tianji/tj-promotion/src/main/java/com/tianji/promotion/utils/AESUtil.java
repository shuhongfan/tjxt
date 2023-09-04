package com.tianji.promotion.utils;

import cn.hutool.core.util.RandomUtil;
import com.tianji.common.exceptions.CommonException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Encryption and Decryption using AES 256 Algorithm
 */
public class AESUtil {

    private static final String CIPHER_ALGORITHM = "AES/CTR/NoPadding";
    private static final String KEY_ALGORITHM = "AES";

    private final Key secretKey;
    private final IvParameterSpec ivSpec;

    public AESUtil() {
        this(RandomUtil.randomString(16), RandomUtil.randomString(16));
    }

    public AESUtil(String key, String iv) {
        try {
            KeyGenerator generator = KeyGenerator.getInstance(KEY_ALGORITHM);
            // 生成秘钥
            generator.init(256, new SecureRandom(key.getBytes(StandardCharsets.UTF_8)));
            this.secretKey = generator.generateKey();
            // 生成向量
            this.ivSpec = new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            throw new CommonException("初始化AES工具失败", e);
        }
    }

    public byte[] encrypt(byte[] plainBytes) {
        try {
            Cipher aes = Cipher.getInstance(CIPHER_ALGORITHM);
            aes.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
            return aes.doFinal(plainBytes);
        } catch (Exception e) {
            throw new CommonException("数据加密异常", e);
        }
    }

    public byte[] decrypt(byte[] encryptBytes) {
        try {
            Cipher aes = Cipher.getInstance(CIPHER_ALGORITHM);
            aes.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
            return aes.doFinal(encryptBytes);
        } catch (Exception e) {
            throw new CommonException("数据解密异常", e);
        }
    }
}