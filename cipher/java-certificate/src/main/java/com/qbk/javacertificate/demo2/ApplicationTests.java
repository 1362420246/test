package com.qbk.javacertificate.demo2;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;

public class ApplicationTests {

    public static void main(String[] args) throws Exception {
        String publicPath = "C:\\Users\\DXYT\\Desktop\\certificate\\pc"; //公匙存放位置
        String privatePath = "C:\\Users\\DXYT\\Desktop\\certificate\\pc"; //私匙存放位置

        //生成密钥对
//        RSAEncrypt.genKeyPair(publicPath);

        System.out.println("--------------公钥加密私钥解密过程-------------------");

        String signKey = "qbk_公钥加密私钥解密";
        System.out.println("原文：" + signKey);

        // 公钥加密过程
        String loadPublicKeyFile = RSAEncrypt.loadPublicKeyByFile(publicPath);
        RSAPublicKey rsaPublicKey = RSAEncrypt.loadPublicKeyByStr(loadPublicKeyFile);
        byte[] cipherData = RSAEncrypt.encrypt(rsaPublicKey, signKey.getBytes());
        String cipher = Base64.getEncoder().encodeToString(cipherData);
        System.out.println("加密后的数据: " + cipher);

        // 私钥解密过程
        String loadPrivateKeyFile = RSAEncrypt.loadPrivateKeyByFile(privatePath);
        RSAPrivateKey rsaPrivateKey = RSAEncrypt.loadPrivateKeyByStr(loadPrivateKeyFile);
        byte[] res = RSAEncrypt.decrypt(rsaPrivateKey, Base64.getDecoder().decode(cipher));
        String restr = new String(res);
        System.out.println("解密后的数据：" + restr);
        System.out.println();

        System.out.println("--------------私钥加密公钥解密过程-------------------");
        // 私钥加密过程
        cipherData = RSAEncrypt.encrypt(RSAEncrypt.loadPrivateKeyByStr(RSAEncrypt.loadPrivateKeyByFile(privatePath)), signKey.getBytes());
        cipher = Base64.getEncoder().encodeToString(cipherData);
        // 公钥解密过程
        res = RSAEncrypt.decrypt(RSAEncrypt.loadPublicKeyByStr(RSAEncrypt.loadPublicKeyByFile(publicPath)), Base64.getDecoder().decode(cipher));
        restr = new String(res);
        System.out.println("原文：" + signKey);
        System.out.println("加密：" + cipher);
        System.out.println("解密：" + restr);
    }

}
