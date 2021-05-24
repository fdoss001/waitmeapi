package com.waitme.utils;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 * Utility class for reusabilty when doing security functions
 * @author Fernando Dos Santos
 * @version 1.0 2019-02-08
 * @since 1.0 2019-01-30
 */
public class SecurityUtils {
	private static WMLogger log = new WMLogger(SecurityUtils.class);
	
	/**
	 * Generates a completely random byte array for use as a salt.
	 * @return a random byte array of 16 bytes
	 */
	public static byte[] genSalt() {
		SecureRandom random = new SecureRandom();
		byte[] salt = new byte[16];
		random.nextBytes(salt);
		return salt;
	}
	
	/**
	 * Hashes a string using SHA-512 with a random salt for maximum security
	 * @param salt a random byte array to append to the string. this prevents brute force attacks
	 * @param password the string to hash
	 * @return A complete byte array of the hashed string including the salt
	 */
	public static byte[] hashPassword(byte[] salt, String password) {
		//create SHA message digest
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-512");
		} catch (NoSuchAlgorithmException e) {
			//this will never happen because SHA-512 is a valid algorithm
		}
		md.update(salt);
		
		//hash password
		byte[] hashedPassword = md.digest(password.getBytes(StandardCharsets.UTF_8));
		
		return hashedPassword;
	}
	
	/**
	 * Hashes an int using SHA-512
	 * @param number the int to hash (converted to UTF8)
	 * @return the hashed int
	 */
	public static String hashInt512(int number) {
		try {
			return hashInt(number, "SHA-512");
		} catch (NoSuchAlgorithmException e) {
			System.err.println("this will never happen because SHA-512 is a valid algorithm");
			return null;
		}
	}
	
	/**
	 * Hashes an int using SHA-256
	 * @param number the int to hash (converted to UTF8)
	 * @return the hashed int
	 */
	public static String hashInt256(int number) {
		try {
			return hashInt(number, "SHA-256");
		} catch (NoSuchAlgorithmException e) {
			System.err.println("this will never happen because SHA-256 is a valid algorithm");
			return null;
		}
	}
	
	/**
	 * A helper to hash an int using the given hashing algorithm
	 * @param number the int to hash (converted to UTF8)
	 * @param mode the hashing algorithm to use
	 * @return the hashed int as a string
	 * @throws NoSuchAlgorithmException if the algorithm chosen does not exist
	 */
	private static String hashInt(int number, String mode) throws NoSuchAlgorithmException {
		//create SHA message digest
		MessageDigest md = MessageDigest.getInstance(mode);

		//hash int
		byte[] hashedInt = md.digest((""+number).getBytes(StandardCharsets.UTF_8));
		
		return Base64.getEncoder().encodeToString(hashedInt);
	}
	
	/**
	 * Encrypts a string with the given key using AES encryption
	 * @param key the key to encrypt with
	 * @param content the content to encrypt
	 */
	public static String encrypt(String key, String content) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		key = key.substring(0,16);
		Cipher cipher = encryptDecrypt(key, content, Cipher.ENCRYPT_MODE);
		return Base64.getEncoder().encodeToString(cipher.doFinal(content.getBytes(StandardCharsets.UTF_8)));
	}
	
	/**
	 * Decrypts a string with the given key
	 * @param key the key to use for decryption
	 * @param content the content to decrypt
	 * @return the decrypted content in clear text
	 * @throws InvalidKeyException if the key is invalid
	 */
	public static String decrypt(String key, String content) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		key = key.substring(0,16);
		Cipher cipher = encryptDecrypt(key, content, Cipher.DECRYPT_MODE);
		return new String(cipher.doFinal(Base64.getDecoder().decode(content)));
	}
	
	/**
	 * Encodes a string's bytes into UTF8
	 * @param content the string to encode
	 * @return the encoded string
	 */
	public static String encodeString(String content) {
		Encoder enc = Base64.getEncoder();
		return new String(enc.encode(content.getBytes(StandardCharsets.UTF_8)));
	}
	
	/**
	 * Decodes a string from UTF8
	 * @param content the string to decode
	 * @return the decoded string
	 */
	public static String decodeString(String content) {
		Decoder dec = Base64.getDecoder();
		return new String(dec.decode(content));
	}
	
	/**
	 * Helper to encrypt or decrypt based on the key, content, and mode 
	 * @param key the key for encryption/decryption
	 * @param content the content to encrypt/decrypt
	 * @param mode either encrypt or decrypt
	 * @return the cipher to use for the encrypt/decrypt operation
	 * @throws InvalidKeyException if the key is invalid
	 */
	private static Cipher encryptDecrypt(String key, String content, int mode) throws InvalidKeyException {
		SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			log.error("AES/ECB/PKCS5Padding is a valid algorithm with valid padding. This should never happen.");
		}
		cipher.init(mode, keySpec);
		return cipher;
	}
}
