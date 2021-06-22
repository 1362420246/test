package com.qbk.javacipher.demo;

import com.qbk.javacipher.encryptTypeImpl.HexUtil;

import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * 简单实现了AES("AES/CBC/PKCS5Padding")的加密和解密。可以看到代码中主要的是cipher对象，并有以下调用
 *
 * （1）新建Cipher对象时需要传入一个参数"AES/CBC/PKCS5Padding"
 * （2）cipher对象使用之前还需要初始化，共三个参数("加密模式或者解密模式","密匙","向量")
 * （3）调用数据转换：cipher.doFinal(content)，其中content是一个byte数组
 *
 * 实际上Cipher类实现了多种加密算法，在创建Cipher对象时，传入不同的参数就可以进行不同的加密算法。而这些算法不同的地方只是创建密匙的方法不同而已。
 * 如传入“AES/CBC/NoPadding”可进行AES加密，传入"DESede/CBC/NoPadding"可进行DES3加密
 *
 * 要参考Java自带的加密算法，可以参考JDK文档的附录：https://docs.oracle.com/javase/8/docs/technotes/guides/security/StandardNames.html
 */
public class JavaCipher {

    /**
     * content: 加密内容
     * slatKey: 加密的盐，16位字符串
     * vectorKey: 加密的向量，16位字符串
     */
    public static String encrypt(String content, String slatKey, String vectorKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKey secretKey = new SecretKeySpec(slatKey.getBytes(), "AES");
        IvParameterSpec iv = new IvParameterSpec(vectorKey.getBytes());
        /*
        Cipher对象需要初始化
        init(int opmode, Key key, AlgorithmParameterSpec params)
        (1)opmode ：Cipher.ENCRYPT_MODE(加密模式)和 Cipher.DECRYPT_MODE(解密模式)
        (2)key ：密匙，使用传入的盐构造出一个密匙，可以使用SecretKeySpec、KeyGenerator和KeyPairGenerator创建密匙，其中
        * SecretKeySpec和KeyGenerator支持AES，DES，DESede三种加密算法创建密匙
        * KeyPairGenerator支持RSA加密算法创建密匙
        (3)params ：使用CBC模式时必须传入该参数，该项目使用IvParameterSpec创建iv 对象
         */
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
        byte[] encrypted = cipher.doFinal(content.getBytes());
        return Base64.getEncoder().encodeToString(encrypted);
    }

    /**
     * content: 解密内容(base64编码格式)
     * slatKey: 加密时使用的盐，16位字符串
     * vectorKey: 加密时使用的向量，16位字符串
     */
    public static String decrypt(String base64Content, String slatKey, String vectorKey) throws Exception {
        //参数按"算法/模式/填充模式"
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        //加密算法有：AES，DES，DESede(DES3)和RSA 四种
        //创建密匙主要使用SecretKeySpec、KeyGenerator和KeyPairGenerator三个类来创建密匙。
        SecretKey secretKey = new SecretKeySpec(slatKey.getBytes(), "AES");
        // 模式有CBC(有向量模式)和ECB(无向量模式)，向量模式可以简单理解为偏移量，使用CBC模式需要定义一个IvParameterSpec对象
        // 填充模式:
        //* NoPadding: 加密内容不足8位用0补足8位, Cipher类不提供补位功能，需自己实现代码给加密内容添加0, 如{65,65,65,0,0,0,0,0}
        // * PKCS5Padding: 加密内容不足8位用余位数补足8位, 如{65,65,65,5,5,5,5,5}或{97,97,97,97,97,97,2,2}; 刚好8位补8位8
        IvParameterSpec iv = new IvParameterSpec(vectorKey.getBytes());
        //Cipher.ENCRYPT_MODE(加密模式)和 Cipher.DECRYPT_MODE(解密模式)
        cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
        byte[] content = Base64.getDecoder().decode(base64Content);
        byte[] encrypted = cipher.doFinal(content);
        return new String(encrypted);
    }

    public static void main(String[] args) throws Exception {

        String encrypt = JavaCipher.encrypt("123456", "1234567890123456", "1234567890123456");
        System.out.println(encrypt);

        //1jdzWuniG6UMtoa3T6uNLA==

        String decrypt = JavaCipher.decrypt("1jdzWuniG6UMtoa3T6uNLA==", "1234567890123456", "1234567890123456");
        System.out.println(decrypt);
    }
}
