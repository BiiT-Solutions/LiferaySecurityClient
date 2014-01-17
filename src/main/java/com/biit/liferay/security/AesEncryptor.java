package com.biit.liferay.security;

import java.security.Key;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;

import org.apache.commons.codec.binary.Base64;

import com.biit.liferay.security.exceptions.EncryptorException;
import com.biit.liferay.security.exceptions.InvalidEncryptionKey;

class AesEncryptor {

	private static Map<String, Cipher> encryptCipherMap = new ConcurrentHashMap<String, Cipher>(1, 1f, 1);
	public static final String PROVIDER_CLASS = "com.sun.crypto.provider.SunJCE";
	public static final String KEY_ALGORITHM = "AES";
	public static final int KEY_SIZE = 128;

	// Key is stored in the CompanySoap
	public static String encrypt(Key key, String plainTextPassword) throws InvalidEncryptionKey, EncryptorException {
		if (key == null) {
			throw new InvalidEncryptionKey();
		}
		byte[] encryptedBytes = encryptUnencoded(key, plainTextPassword);
		return Base64.encodeBase64String(encryptedBytes);
	}

	public static byte[] encryptUnencoded(Key key, String plainText) throws EncryptorException {

		try {
			byte[] decryptedBytes = plainText.getBytes("UTF-8");
			return encryptUnencoded(key, decryptedBytes);
		} catch (Exception e) {
			throw new EncryptorException(e);
		}
	}

	public static byte[] encryptUnencoded(Key key, byte[] plainBytes) throws EncryptorException {

		String algorithm = key.getAlgorithm();

		String cacheKey = algorithm.concat("#").concat(key.toString());

		Cipher cipher = encryptCipherMap.get(cacheKey);

		try {
			if (cipher == null) {
				Security.addProvider(getProvider());
				cipher = Cipher.getInstance(algorithm);
				cipher.init(Cipher.ENCRYPT_MODE, key);
				encryptCipherMap.put(cacheKey, cipher);
			}
			synchronized (cipher) {
				return cipher.doFinal(plainBytes);
			}
		} catch (Exception e) {
			throw new EncryptorException(e);
		}
	}

	public static Provider getProvider() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
		Class<?> providerClass = null;
		providerClass = Class.forName(PROVIDER_CLASS);
		return (Provider) providerClass.newInstance();
	}

	public static Key generateKey() throws EncryptorException {
		return generateKey(KEY_ALGORITHM);
	}

	public static Key generateKey(String algorithm) throws EncryptorException {
		try {
			Security.addProvider(getProvider());
			KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm);
			keyGenerator.init(KEY_SIZE, new SecureRandom());
			Key key = keyGenerator.generateKey();
			return key;
		} catch (Exception e) {
			throw new EncryptorException(e);
		}
	}

}
