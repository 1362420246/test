package com.qbk.javacertificate.demo;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 解析证书
 */
public class JavaCertificate {

    static String cerPath = "C:\\Users\\DXYT\\Desktop\\certificate\\demo.cer";
    static String jksPath = "C:\\Users\\DXYT\\Desktop\\certificate\\demo.jks";

    public static void main(String[] args) throws Exception {
        String pblicKey = getCerKey();

        String privateKey = getKey();

        System.out.println("公钥对象:");
        PublicKey publicKey = getPublicKey(pblicKey);
        System.out.println(publicKey);

        System.out.println();
        showCertInfo();

    }

    /**
     * 获取jks中密钥
     */
    public static String getKey() {
        try{
            KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
            keystore.load(new FileInputStream(jksPath), "123456".toCharArray());

            //jks文件别名
            Enumeration<String> aliases = keystore.aliases();
            String alias = null;
            while (aliases.hasMoreElements()) {
                alias = aliases.nextElement();
            }

            Key key = keystore.getKey(alias,"123456".toCharArray());

            Certificate cert = keystore.getCertificate(alias);
            PublicKey publicKey=cert.getPublicKey();
            String encoded=Base64.getEncoder().encodeToString(publicKey.getEncoded());
            System.out.println("publicKey key = " + encoded);

            KeyPair keyPair = new KeyPair(publicKey,(PrivateKey)key);
            PrivateKey privateKey = keyPair.getPrivate();
            String encoded2 = Base64.getEncoder().encodeToString(privateKey.getEncoded());
            System.out.println("private key = " + encoded2);

            return encoded2;
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return "";
    }

    /**
     * 获取证书中的公钥
     */
    public static String getCerKey() {
        try{

            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            FileInputStream fileInputStream = new FileInputStream(cerPath);
            X509Certificate x509Certificate = (X509Certificate) certificateFactory.generateCertificate(fileInputStream);
            fileInputStream.close();
            PublicKey publicKey = x509Certificate.getPublicKey();
            String encoded = Base64.getEncoder().encodeToString(publicKey.getEncoded());
            System.out.println("publicKey key = " + encoded);
            return encoded;
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    /**
     * 获取公钥对象
     * @param publicKey 公钥串
     */
    static PublicKey getPublicKey(String publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(publicKey.getBytes(StandardCharsets.UTF_8))));
    }

    //************************************* 解析证书 ***************************************************************

    public static void showCertInfo() {
        try {
            //读取证书文件
            File file = new File(cerPath);
            InputStream inStream = new FileInputStream(file);
            //创建X509工厂类
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            //创建证书对象
            X509Certificate oCert = (X509Certificate)cf.generateCertificate(inStream);
            inStream.close();
            SimpleDateFormat dateformat = new SimpleDateFormat("yyyy/MM/dd");
            String info = null;

            //公钥
            PublicKey publicKey = oCert.getPublicKey();
            String encoded = Base64.getEncoder().encodeToString(publicKey.getEncoded());
            System.out.println("证书公钥:" + encoded);

            //获得证书版本
            info = String.valueOf(oCert.getVersion());
            System.out.println("证书版本:"+info);
            //获得证书序列号
            info = oCert.getSerialNumber().toString(16);
            System.out.println("证书序列号:"+info);
            //获得证书有效期
            Date beforedate = oCert.getNotBefore();
            info = dateformat.format(beforedate);
            System.out.println("证书生效日期:"+info);
            Date afterdate = oCert.getNotAfter();
            info = dateformat.format(afterdate);
            System.out.println("证书失效日期:"+info);
            //获得证书主体信息
            info = oCert.getSubjectDN().getName();
            System.out.println("证书拥有者:"+info);
            //获得证书颁发者信息
            info = oCert.getIssuerDN().getName();
            System.out.println("证书颁发者:"+info);
            //获得证书签名算法名称
            info = oCert.getSigAlgName();
            System.out.println("证书签名算法:"+info);

//            List<String> byts = oCert.getExtendedKeyUsage();
//            for (String str : byts) {
//                System.out.println(str);
//                byte[] extensionValue = oCert.getExtensionValue(str);
//                System.out.println(new String(extensionValue));
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
