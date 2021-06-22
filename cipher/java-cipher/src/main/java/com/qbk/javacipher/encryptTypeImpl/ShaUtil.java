package com.qbk.javacipher.encryptTypeImpl;

import com.qbk.javacipher.EncryptEnum.ShaEnum;
import com.qbk.javacipher.EncryptUtil;
import com.qbk.javacipher.encryptImp.MessageDigestImp;
import lombok.extern.slf4j.Slf4j;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * SHA摘要加密
 */
@Slf4j
public class ShaUtil extends MessageDigestImp<ShaEnum> {
	public ShaUtil(ShaEnum defaultEncrypt) {
		this.defaultAlgorithm = defaultEncrypt == null ? ShaEnum.SHA256 : defaultEncrypt;
		this.configSlat = EncryptUtil.SHA_SLAT;
	}

	@Override
	protected byte[] encrypt(String content, String slat, ShaEnum encryptType) {
		try {
			String encryptContent = content + slat;
			MessageDigest messageDigest = MessageDigest.getInstance(encryptType.getEncryptType());
			return messageDigest.digest(encryptContent.getBytes());
		} catch (NoSuchAlgorithmException e) {
			log.error("Sha MessageDigest init error, encrypt type no support.");
		}
		return null;
	}
}
