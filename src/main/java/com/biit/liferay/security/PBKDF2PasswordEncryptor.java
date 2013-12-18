package com.biit.liferay.security;

import java.nio.ByteBuffer;
import java.util.regex.Pattern;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.apache.axis.encoding.Base64;

import com.biit.liferay.security.exceptions.InvalidCredentialsException;
import com.biit.liferay.security.exceptions.PwdEncryptorException;

public class PBKDF2PasswordEncryptor {
	public static final String TYPE_PBKDF2 = "PBKDF2";
	public static final String ALGORITHM = "PBKDF2WithHmacSHA1";
	private static final int _KEY_SIZE = 160;
	private static final int _ROUNDS = 128000;
	private static final int _SALT_BYTES_LENGTH = 8;
	private static Pattern _pattern = Pattern.compile("^.*/?([0-9]+)?/([0-9]+)$");

	public String[] getSupportedAlgorithmTypes() {
		return new String[] { TYPE_PBKDF2 };
	}

	public void validate(String newPasswordPlainText, String oldPasswordEncrypted) throws PwdEncryptorException,
			InvalidCredentialsException {
		String newEncPwd = encrypt(newPasswordPlainText, oldPasswordEncrypted);

		if (!oldPasswordEncrypted.equals(newEncPwd)) {
			throw new InvalidCredentialsException("Password does not match!");
		}
	}

	public String encrypt(String plainTextPassword) throws PwdEncryptorException {
		return encrypt(plainTextPassword, null);
	}

	public String encrypt(String plainTextPassword, String oldEncriptedPassword) throws PwdEncryptorException {
		if (plainTextPassword == null) {
			throw new PwdEncryptorException("Unable to encrypt blank password");
		}

		return doEncrypt(ALGORITHM, plainTextPassword, oldEncriptedPassword);
	}

	protected String doEncrypt(String algorithm, String plainTextPassword, String encryptedPassword)
			throws PwdEncryptorException {

		try {
			PBKDF2EncryptionConfiguration pbkdf2EncryptionConfiguration = new PBKDF2EncryptionConfiguration();
			pbkdf2EncryptionConfiguration.configure(algorithm, encryptedPassword);
			byte[] saltBytes = pbkdf2EncryptionConfiguration.getSaltBytes();

			PBEKeySpec pbeKeySpec = new PBEKeySpec(plainTextPassword.toCharArray(), saltBytes,
					pbkdf2EncryptionConfiguration.getRounds(), pbkdf2EncryptionConfiguration.getKeySize());

			String algorithmName = algorithm;
			int index = algorithm.indexOf('/');

			if (index > -1) {
				algorithmName = algorithm.substring(0, index);
			}

			SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(algorithmName);

			SecretKey secretKey = secretKeyFactory.generateSecret(pbeKeySpec);

			byte[] secretKeyBytes = secretKey.getEncoded();

			ByteBuffer byteBuffer = ByteBuffer.allocate(2 * 4 + saltBytes.length + secretKeyBytes.length);

			byteBuffer.putInt(pbkdf2EncryptionConfiguration.getKeySize());
			byteBuffer.putInt(pbkdf2EncryptionConfiguration.getRounds());
			byteBuffer.put(saltBytes);
			byteBuffer.put(secretKeyBytes);

			return Base64.encode(byteBuffer.array());
		} catch (Exception e) {
			throw new PwdEncryptorException(e.getMessage(), e);
		}
	}

	private class PBKDF2EncryptionConfiguration {
		private int _keySize = _KEY_SIZE;
		private int _rounds = _ROUNDS;
		private byte[] _saltBytes = new byte[_SALT_BYTES_LENGTH];

		public void configure(String algorithm, String encryptedPassword) throws PwdEncryptorException {

			if (encryptedPassword == null) {
				_keySize = _KEY_SIZE;
				_rounds = _ROUNDS;

				BigEndianCodec.putLong(_saltBytes, 0, SecureRandomUtil.nextLong());
			} else {
				byte[] bytes = new byte[16];

				try {
					byte[] encryptedPasswordBytes = Base64.decode(encryptedPassword);

					System.arraycopy(encryptedPasswordBytes, 0, bytes, 0, bytes.length);
				} catch (Exception e) {
					throw new PwdEncryptorException("Unable to extract salt from encrypted password " + e.getMessage(),
							e);
				}

				ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);

				_keySize = byteBuffer.getInt();
				_rounds = byteBuffer.getInt();

				byteBuffer.get(_saltBytes);
			}
		}

		public int getKeySize() {
			return _keySize;
		}

		public int getRounds() {
			return _rounds;
		}

		public byte[] getSaltBytes() {
			return _saltBytes;
		}
	}
}
