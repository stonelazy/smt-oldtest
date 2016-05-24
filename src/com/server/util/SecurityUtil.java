//$Id$
/**
 * 
 */

package com.server.util;

import java.security.MessageDigest;
import java.security.SignatureException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.Charsets;
import org.apache.commons.codec.binary.Base64;

import com.share.constants.CredentialsConstants;

/**
 * @author sudharsan-2598
 *
 */
public class SecurityUtil extends CredentialsConstants
{
	private static Logger logger = Logger.getLogger(SecurityUtil.class.getName());
	private static final byte[] key = CredentialsConstants.SECRET_KEY.getBytes(Charsets.UTF_8);
	private static final IvParameterSpec iv = new IvParameterSpec("randomValue".getBytes(Charsets.UTF_8));

	public static final String HASH_ALGORITHM = "HmacSHA1";// No I18N
	public static final String AES_ALGO = "AES";

	public static void main(String[] args) throws SignatureException
	{
		logger.info("mac>> " + calculateHMAC("test"));
	}
	
	
	public static String calculateHMAC(String data) throws SignatureException
	{
		String result;
		try
		{
			SecretKeySpec signingKey = new SecretKeySpec(CredentialsConstants.SECRET_KEY.getBytes(Charsets.UTF_8), HASH_ALGORITHM);

			Mac mac = Mac.getInstance(HASH_ALGORITHM);
			mac.init(signingKey);

			byte[] rawHmac = mac.doFinal(data.getBytes(Charsets.UTF_8));
			result = Base64.encodeBase64String(rawHmac);

			logger.fine("result>> " + result + "  rawhmac>> " + rawHmac);
		} catch (Exception e)
		{
			throw new SignatureException("Failed to generate HMAC : " + e.getMessage());// No I18N
		}
		return result;
	}

	public static boolean verifyIfDigestIsSame(String dataReceived, String dataEncryptedByMe)
	{
	        return (MessageDigest.isEqual(Base64.decodeBase64(dataReceived), Base64.decodeBase64(dataEncryptedByMe)));
	}
	
	public static String encrypt(String value)
	{
		try
		{
			SecretKeySpec skeySpec = new SecretKeySpec(key, AES_ALGO);

			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

			byte[] encrypted = cipher.doFinal(value.getBytes(Charsets.UTF_8));

			return Base64.encodeBase64String(encrypted);
		} catch (Exception e)
		{
			logger.info("value>> " + value);
			logger.log(Level.WARNING, "Exception Occured", e);
		}
		return null;
	}

	public static String decrypt(String encrypted)
	{
		try
		{
			SecretKeySpec skeySpec = new SecretKeySpec(key, AES_ALGO);

			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
			cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

			byte[] original = cipher.doFinal(Base64.decodeBase64(encrypted));

			return new String(original);
		} catch (Exception e)
		{
			logger.info("encrypted>> " + encrypted);
			logger.log(Level.WARNING, "Exception Occured", e);
		}
		return null;
	}

}
