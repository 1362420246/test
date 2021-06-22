package com.qbk.javacertificate.demo;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import sun.security.util.DerInputStream;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * 签名 校验
 */
public class SignatureDemo {

    public static final String KEY_ALGORITHM = "RSA";

    public static void main(String[] args)throws Exception{
        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCtInNTlnT3eq65LZ9JqEHaaqi5q5NeAGuJF3wNUu+4grQg3AYbZ/kE56kh2o3jviC90VFpWkx1OQbXcN//E7aXTr6j5aGo/p0lRd1Or7ZcbCHRC8V34Nih9nCV7fzHdV3K5vizLsN6MpFi8wP0/XaGF+V/BKU4jbjyAlBpC373xQIDAQAB";
        String privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAK0ic1OWdPd6rrktn0moQdpqqLmrk14Aa4kXfA1S77iCtCDcBhtn+QTnqSHajeO+IL3RUWlaTHU5Btdw3/8TtpdOvqPloaj+nSVF3U6vtlxsIdELxXfg2KH2cJXt/Md1Xcrm+LMuw3oykWLzA/T9doYX5X8EpTiNuPICUGkLfvfFAgMBAAECgYA5IkRCkkUZIhAkpcyJ5w+MP2RcmSUxgRv7ipdyYisfjWpZ6aHOS3pexwAGXvQx8p0lZrzh8l2G1YHPeL1ClMPNEQ3LPHgDtKLBfNnVIeZu1VDXtIIXX7+aLd5LnQj6dK5bTrYslXQ0wqluhLv0seSQNnQKMenRL0f5f9jWj6cWwQJBAOlGJj2p/ghUfElJe4keDG0ZOAZ2evaxW/LKUiipSf4OJ8/UlyZntqySu2xr1eFZeRlAAmE4s5h9Gmn6M+XM+lECQQC+AGyVf6lkUM1zDP02Aw/s2w9yQlv0zppcCwETsTjQrt3HF9rKjD7tIPS/rcEsOHzg3IWBQAvgI2OwUItu/pU1AkARsoy8KOVo5F/5f2Wr5Wez7zHc66gwhwwew1KwWweOCqzii5JcC4pEVW16sEOtsQgK7lw/2/lkHDmgyKafTInBAkEAnFJ9BAMkuwRABIiLTAT49UbGTpuKTMAu/8uN90W4GPnGPifCOyNoInEa7Rln9ZoEJH1K2ix3mNGFE2sxTQDogQJBAJGQo8Mpu/at1Xopc6xlTmnOmDg6FvFCSICxeejqwkS0f7IzCkqBIsok8zW9wD6hWRqKw+HJQtPXQuW3vss1W+c=";

        String content = "测试测试";

        String sign = sign(content, privateKey);
        System.out.println("签名:" + sign);

        boolean verify = verify(content, publicKey, sign);
        System.out.println("签名校验结果:" + verify);
    }

    /**
     * 签名
     *
     * @param content 数据
     * @param privateKey 私钥
     */
    public static String sign(String content, String privateKey) throws Exception {
        byte[] data= content.getBytes(StandardCharsets.UTF_8);

        // 解密由base64编码的私钥
        byte[] keyBytes = Base64.getDecoder().decode(privateKey);

        // 构造PKCS8EncodedKeySpec对象
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);

        // KEY_ALGORITHM 指定的加密算法
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);

        // 取私钥匙对象
        PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);

        // 用私钥对信息生成数字签名
        Signature signature = Signature.getInstance("SHA384WithRSA");
        signature.initSign(priKey);
        signature.update(data);
        byte[] sign = signature.sign();
        return Base64.getEncoder().encodeToString(sign);
    }

    /**
     * 校验数字签名
     *
     * @param content 数据
     * @param publicKey 公钥
     * @param sign 数字签名
     * @return 校验成功返回true 失败返回false
     *
     */
    public static boolean verify(String content, String publicKey, String sign) throws Exception {
        byte[] data = content.getBytes(StandardCharsets.UTF_8);

        // 解密由base64编码的公钥
        byte[] keyBytes = Base64.getDecoder().decode(publicKey);

        // 构造X509EncodedKeySpec对象
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);

        // KEY_ALGORITHM 指定的加密算法
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);

        // 取公钥匙对象
        PublicKey pubKey = keyFactory.generatePublic(keySpec);

        Signature signature = Signature.getInstance("SHA384WithRSA");
        signature.initVerify(pubKey);
        signature.update(data);

        // 验证签名是否正常
        return signature.verify(Base64.getDecoder().decode(sign));
    }
}
