package com.qbk.javacertificate.demo;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import sun.security.util.DerInputStream;

import javax.crypto.Cipher;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Base64;

/**
 * 读取证书 中的扩展信息
 */
public class ReadCeriticate {
    public static void main(String[] args) throws Exception{
        //临时bc方法添加都环境变量
        Security.addProvider(new BouncyCastleProvider());
        //读取证书
        FileInputStream fileInputStream = new FileInputStream("C:\\Users\\DXYT\\Desktop\\certificate\\qbk.cer");
        //获取证书
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509", "BC");
        Certificate certificate = certificateFactory.generateCertificate(fileInputStream);
        X509Certificate x509Certificate = (X509Certificate)certificate;
        //获取公钥
        PublicKey publicKey = x509Certificate.getPublicKey();

        //获取 扩展信息
        ASN1ObjectIdentifier asn1ObjectIdentifier = new ASN1ObjectIdentifier("1.2.3.4");
        byte[] extensionValue = x509Certificate.getExtensionValue(asn1ObjectIdentifier.getId());

        DerInputStream derInputStream = new DerInputStream(extensionValue);
        byte[] octetString = derInputStream.getOctetString();

        String ciphertext = new String(octetString, StandardCharsets.UTF_8);

        byte[] decode = Base64.getDecoder().decode(ciphertext);

        //使用公钥 给扩展信息解密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        byte[] bytes = cipher.doFinal(decode);
        String result = new String(bytes, StandardCharsets.UTF_8);
        System.out.println(result);
    }
}
