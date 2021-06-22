package com.qbk.javacertificate.demo;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.*;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509ExtensionUtils;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECNamedCurveGenParameterSpec;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.PKCS10CertificationRequestBuilder;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequestBuilder;
import org.bouncycastle.util.encoders.Base64;
import sun.security.util.DerInputStream;
import sun.security.util.ObjectIdentifier;
import sun.security.x509.CertificateExtensions;
import sun.security.x509.ExtendedKeyUsageExtension;
import sun.security.x509.KeyUsageExtension;

import javax.crypto.Cipher;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.*;
import java.util.*;

/**
 * 生成证书 cer
 *
 * 带扩展信息
 */
public class X509v3Certificate1 {

    // 签发者DN
    public static String issuerDN = "C=CN,ST=BJ,L=BJ,O=BEIJING,OU=BEIJING,CN=BEIJING";

    static{
        try{
            //bc方法添加都环境变量
            Security.addProvider(new BouncyCastleProvider());
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        makeUserSelfSignCert();
    }

    public static void makeUserSelfSignCert() throws Exception {

        //获取已注册提供者列表
        //[SUN version 1.8, SunRsaSign version 1.8, SunEC version 1.8, SunJSSE version 1.8, SunJCE version 1.8, SunJGSS version 1.8, SunSASL version 1.8, XMLDSig version 1.8, SunPCSC version 1.8, SunMSCAPI version 1.8, BC version 1.68]
        System.out.println(Arrays.toString(Security.getProviders()));

        //创建RSA密匙
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA", "BC");
        keyPairGenerator.initialize(1024);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        //私钥
        PrivateKey privKey = keyPair.getPrivate();
        //公钥
        PublicKey pubKey = keyPair.getPublic();
        System.out.println( "私钥: " + new String(Base64.encode(privKey.getEncoded())));
        System.out.println( "公钥: " + new String(Base64.encode(pubKey.getEncoded())));

        //the certificate issuer
        X500Name issuer = new X500Name(issuerDN);

        // 1. 创建签名
        ContentSigner signer =
                new JcaContentSignerBuilder("SHA384WithRSA")
                        .setProvider(BouncyCastleProvider.PROVIDER_NAME)
                        .build(privKey);

        // 2. 创建证书请求
        PKCS10CertificationRequestBuilder pkcs10CertificationRequestBuilder =
                new JcaPKCS10CertificationRequestBuilder(issuer, pubKey);

        PKCS10CertificationRequest pkcs10CertificationRequest =
                pkcs10CertificationRequestBuilder.build(signer);

        /**
         * 3. 创建证书
         *
         * Create a builder for a version 3 certificate.
         *
         * @param issuer the certificate issuer
         * @param serial the certificate serial number
         * @param notBefore the date before which the certificate is not valid
         * @param notAfter the date after which the certificate is not valid
         * @param subject the certificate subject
         * @param publicKeyInfo the info structure for the public key to be associated with this certificate.
         **/
        X509v3CertificateBuilder certBuilder =
                new X509v3CertificateBuilder(
                        issuer,
                        BigInteger.TEN,
                        new Date(System.currentTimeMillis() ),
                        new Date(System.currentTimeMillis() + 1000 * 365 * 24 * 3600),
                        pkcs10CertificationRequest.getSubject(),
                        pkcs10CertificationRequest.getSubjectPublicKeyInfo());

        //添加扩展信息
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, privKey);
        byte[] plaintext = cipher.doFinal("这是一条扩展信息".getBytes());
        byte[] encode = Base64.encode(plaintext);
        certBuilder.addExtension(new ASN1ObjectIdentifier("1.2.3.4"), true, encode);

        X509CertificateHolder holder = certBuilder.build(signer);

        java.security.cert.X509Certificate certificate = new JcaX509CertificateConverter()
                .setProvider(BouncyCastleProvider.PROVIDER_NAME)
                .getCertificate(holder);

        FileOutputStream fos = new FileOutputStream("C:\\Users\\DXYT\\Desktop\\certificate\\qbk.cer");
        fos.write(certificate.getEncoded());
        fos.flush();
        fos.close();
    }

}
