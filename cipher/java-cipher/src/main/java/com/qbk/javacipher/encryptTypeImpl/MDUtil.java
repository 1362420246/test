package com.qbk.javacipher.encryptTypeImpl;

import com.qbk.javacipher.EncryptEnum.MDEnum;
import com.qbk.javacipher.EncryptUtil;
import com.qbk.javacipher.encryptImp.MessageDigestImp;
import lombok.extern.slf4j.Slf4j;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD加密工具类
 */
@Slf4j
public class MDUtil extends MessageDigestImp<MDEnum> {
	
//	private final static Logger logger = LoggerFactory.getLogger(ShaUtil.class);
	
	public MDUtil(MDEnum defaultEncrypt) {
		this.defaultAlgorithm = defaultEncrypt == null ? MDEnum.MD5 : defaultEncrypt;
		this.configSlat = EncryptUtil.MD_SLAT;
	}
	
	@Override
	protected byte[] encrypt(String content, String slat, MDEnum encryptType) {
		try {
			String encryptContent = null;
			if (slat != null)
			{
				encryptContent = content + slat;
			}
			else
			{
				encryptContent = content;
			}
			MessageDigest messageDigest = MessageDigest.getInstance(encryptType.getEncryptType());
			return messageDigest.digest(encryptContent.getBytes());
		} catch (NoSuchAlgorithmException e) {
			log.error("MD MessageDigest init error, encrypt type no support.");
		}
		return null;
	}
}
