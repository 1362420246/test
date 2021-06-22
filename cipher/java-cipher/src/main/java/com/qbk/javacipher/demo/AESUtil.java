package com.qbk.javacipher.demo;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import java.nio.charset.Charset;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * 对称加密
 */
public class AESUtil {

    /**
     * 盐
     */
    private static final String SLATKEY = "qbkqbk";

    public static void main(String[] args) throws Exception {
        String encrypt = encrypt("{\n\t\"name\":\"qbk\"\n}");
        System.out.println(encrypt);

        String decrypt = decrypt("WvRKdhkmzBAuwTDroyuNU3vL5wdCrqRK/E0WIwWAPe0=");
        System.out.println(decrypt);
    }

    /**
     * 加密
     */
    public static String encrypt(String content)throws Exception {
        Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
        Key secretKey = getSlatKey(SLATKEY);
        byte[] plaintext = handleNoPaddingEncryptFormat(cipher, content,Charset.defaultCharset());
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encrypted = cipher.doFinal(plaintext);
        return Base64.getEncoder().encodeToString(encrypted);
    }

    /**
     * 解密
     */
    public static String decrypt(String content)throws Exception {
        Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
        Key secretKey = getSlatKey(SLATKEY);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] byteContent = Base64.getDecoder().decode(content);
        byte[] original = cipher.doFinal(byteContent);
        return new String(original).trim();
    }

    /**
     * 获取加密的密匙，传入的slatKey可以是任意长度的，作为SecureRandom的随机种子，
     * 而在KeyGenerator初始化时设置密匙的长度128bit(16位byte)
     */
    private static Key getSlatKey(String slatKey) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        random.setSeed(slatKey.getBytes());
        kgen.init(128, random);
        return kgen.generateKey();
    }


    /**
     * <p>NoPadding加密模式, 加密内容必须是 8byte的倍数, 不足8位则末位补足0</p>
     * <p>加密算法不提供该补码方式, 需要代码完成该补码方式</p>
     * @param cipher
     * @param content ：加密内容
     * @param charset :指定的字符集
     * @return 符合加密的内容(byte[])
     */
    public static byte[] handleNoPaddingEncryptFormat(Cipher cipher, String content, Charset charset) throws Exception {
        int blockSize = cipher.getBlockSize();
        byte[] srawt = content.getBytes(charset);
        int plaintextLength = srawt.length;
        if (plaintextLength % blockSize != 0) {
            plaintextLength = plaintextLength + (blockSize - plaintextLength % blockSize);
        }
        byte[] plaintext = new byte[plaintextLength];
        System.arraycopy(srawt, 0, plaintext, 0, srawt.length);
        return plaintext;
    }
}
