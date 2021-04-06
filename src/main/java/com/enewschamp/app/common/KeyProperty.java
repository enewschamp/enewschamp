package com.enewschamp.app.common;

import java.security.Key;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class KeyProperty {

	public static String DATABASE_ENCRYPTION_KEY;

	public static String MID;

	public static String MERCHANT_KEY;

	@Value("${enewschamp.eNewsChamp1}")
	public void setMid(String mid) {
		MID = decrypt(mid, DATABASE_ENCRYPTION_KEY);
	}

	@Value("${enewschamp.eNewsChamp2}")
	public void setMerchantKey(String merchantKey) {
		MERCHANT_KEY = decrypt(merchantKey, DATABASE_ENCRYPTION_KEY);
	}

	@Value("${enewschamp.eNewsChamp3}")
	public void setProperties(String databaseEncryptionKey) {
		DATABASE_ENCRYPTION_KEY = databaseEncryptionKey;
	}

	private String decrypt(String value, String skey) {
		try {
			Key key = new SecretKeySpec(Arrays.copyOf(skey.getBytes("UTF-8"), 16), "AES");
			Cipher c = Cipher.getInstance("AES/ECB/PKCS5Padding");
			c.init(Cipher.DECRYPT_MODE, key);
			return new String(c.doFinal(Base64.getDecoder().decode(value)));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
