package com.qbk.javacertificate.demo;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.DefaultSignatureAlgorithmIdentifierFinder;

import java.io.*;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.Date;
import java.util.Random;

/**
 * 生成证书 cer
 */
public class X509v3Certificate2 {

    static{
        try{
            Security.addProvider(new BouncyCastleProvider());
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        // 设置开始日期和结束日期
        long year = 360 * 24 * 60 * 60 * 1000;
        Date notBefore = new Date();
        Date notAfter = new Date(notBefore.getTime() + year);

        // 设置颁发者和主题
        String issuerString = "CN=root,OU=单位,O=组织";
        X500Name issueDn = new X500Name(issuerString);
        X500Name subjectDn = new X500Name(issuerString);

        // 证书序列号
        BigInteger serail = BigInteger.probablePrime(32, new Random());

        //证书中的公钥
        KeyPair keyPair = null;
        try {
            keyPair = KeyPairGenerator.getInstance("RSA", "BC")
                    .generateKeyPair();
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        }
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        //组装公钥信息
        SubjectPublicKeyInfo subjectPublicKeyInfo = null;
        try {
            subjectPublicKeyInfo = SubjectPublicKeyInfo
                    .getInstance(new ASN1InputStream(publicKey.getEncoded())
                            .readObject());
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        //证书的签名数据
        final byte[] signatureData ;
        try {
            Signature signature = Signature.getInstance("SHA1withRSA");
            signature.initSign(privateKey);
            signature.update(publicKey.getEncoded());
            signatureData = signature.sign();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(),e);
        }

        //组装证书
        X509v3CertificateBuilder builder = new X509v3CertificateBuilder(
                issueDn, serail, notBefore, notAfter, subjectDn,
                subjectPublicKeyInfo);

        //给证书签名
        X509CertificateHolder holder = builder.build(new ContentSigner() {
            ByteArrayOutputStream buf = new ByteArrayOutputStream();
            @Override
            public byte[] getSignature() {
                try {
                    buf.write(signatureData);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return signatureData;
            }
            @Override
            public OutputStream getOutputStream() {
                return buf;
            }
            @Override
            public AlgorithmIdentifier getAlgorithmIdentifier() {
                return AlgorithmIdentifier.getInstance(new DefaultSignatureAlgorithmIdentifierFinder().find("SHA1withRSA"));
            }
        });

        try {
            byte[] certBuf = holder.getEncoded();
            X509Certificate certificate = (X509Certificate) CertificateFactory.getInstance("X509").generateCertificate(new ByteArrayInputStream(certBuf));
            System.out.println(certificate);
            //证书base64编码字符串
            System.out.println( Base64.getEncoder().encodeToString(certificate.getEncoded()));

            FileOutputStream fos = new FileOutputStream("C:\\Users\\DXYT\\Desktop\\certificate\\qbk2.cer");
            fos.write(certificate.getEncoded());
            fos.flush();
            fos.close();
        } catch (IOException | CertificateException e) {
            e.printStackTrace();
        }

    }
}