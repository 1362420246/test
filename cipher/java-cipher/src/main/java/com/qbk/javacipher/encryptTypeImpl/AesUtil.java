package com.qbk.javacipher.encryptTypeImpl;

import com.qbk.javacipher.EncryptEnum.AseEnum;
import com.qbk.javacipher.EncryptEnum.MDEnum;
import com.qbk.javacipher.EncryptUtil;
import com.qbk.javacipher.encryptImp.CipherEencryptImp;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES加密工具类
 */
@Slf4j
public class AesUtil extends CipherEencryptImp<AseEnum> {
	public AesUtil(AseEnum defaultEncrypt) {
		this.defaultAlgorithm = defaultEncrypt == null ? AseEnum.CBC_NO_PADDING : defaultEncrypt;
		String slat = EncryptUtil.MD_ENCRYPT.encryptHex(EncryptUtil.AES_SLAT, EncryptUtil.AES_SLAT, MDEnum.MD5);
		this.configSlat = slat.substring(0, 16);
		this.configVectorKey = slat.substring(16);
	}
	private final static String AES_ALGORITHM_NAME = "AES";
	private final static int SLAT_KEY_LENGTH = 16;
	private final static int VECTOR_KEY_LENGTH = 16;

	@Override
	protected byte[] encrypt(String content, String slatKey, String vectorKey, AseEnum encryptType) throws Exception {
		byte[] encrypted = null;
		try {
			if (slatKey == null || slatKey.length() != SLAT_KEY_LENGTH) 
			{
				throw new Exception("slatKey is null or slatKey is not at " + SLAT_KEY_LENGTH + "-bytes.");
			}
			if (encryptType == null) 
			{
				throw new Exception("encryptType is null");
			}
			Cipher cipher = Cipher.getInstance(encryptType.getEncryptType());
			SecretKey secretKey = new SecretKeySpec(slatKey.getBytes(), AES_ALGORITHM_NAME);
			byte[] plaintext = null;
			if (AseEnum.CBC_NO_PADDING.equals(encryptType) || AseEnum.ECB_NO_PADDING.equals(encryptType)) 
			{
				plaintext = handleNoPaddingEncryptFormat(cipher, content);
			} 
			else 
			{
				plaintext = content.getBytes();
			}
			if (AseEnum.CBC_NO_PADDING.equals(encryptType) || AseEnum.CBC_PKCS5PADDING.equals(encryptType)) 
			{
				if (vectorKey == null || vectorKey.length() != VECTOR_KEY_LENGTH) 
				{
					throw new Exception("vectorKey is null or vectorKey is not at " + VECTOR_KEY_LENGTH + "-bytes.");
				}
				IvParameterSpec iv = new IvParameterSpec(vectorKey.getBytes());
				cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
			} 
			else 
			{
				cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			}
			encrypted = cipher.doFinal(plaintext);
		} catch (Exception e) {
			log.error("Aes Util encryption failed, errors: {}", e.getMessage());
			throw new Exception(encryptException);
		}
		return encrypted;
	}
	
	@Override
	protected String decrypt(byte[] content, String slatKey, String vectorKey, AseEnum encryptType) throws Exception {
		String result = null;
		try {
			if (slatKey == null || slatKey.length() != SLAT_KEY_LENGTH) 
			{
				throw new Exception("slatKey is null or slatKey is not at " + SLAT_KEY_LENGTH + "-bytes.");
			}
			if (encryptType == null) 
			{
				throw new Exception("encryptType is null");
			}
			Cipher cipher = Cipher.getInstance(encryptType.getEncryptType());
			SecretKey secretKey = new SecretKeySpec(slatKey.getBytes(), AES_ALGORITHM_NAME);
			if (AseEnum.CBC_NO_PADDING.equals(encryptType) || AseEnum.CBC_PKCS5PADDING.equals(encryptType)) 
			{
				if (vectorKey == null || vectorKey.length() != VECTOR_KEY_LENGTH) 
				{
					throw new Exception("vectorKey is null or vectorKey is not at " + VECTOR_KEY_LENGTH + "-bytes.");
				}
				IvParameterSpec iv = new IvParameterSpec(vectorKey.getBytes());
				cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
			} 
			else 
			{
				cipher.init(Cipher.DECRYPT_MODE, secretKey);
			}
			byte[] original = cipher.doFinal(content);
			String originalString = new String(original);
			result = originalString.trim();
		} catch (Exception e) {
			log.error("Aes Util decryption failed, errors: {}", e.getMessage());
			throw new Exception(decryptException);
		}
		return result;
	}
}
