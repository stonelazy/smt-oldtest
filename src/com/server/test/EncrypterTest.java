//$Id$
/**
 * 
 */

package com.server.test;
import org.apache.commons.codec.binary.Base64;

import java.security.MessageDigest;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author sudharsan-2598
 *
 */

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.io.Charsets;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

class EncrypterTest
{
	private static Logger logger = Logger.getLogger(EncrypterTest.class.getName());
	
	Cipher ecipher;
    static final ThreadLocal<MessageDigest> sha512 = new ThreadLocal();
    public static final char[] HEX = 
    new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
	static Cipher dcipher;

	  public static synchronized String encrypt(String password, String salt) {
	        try {
	            byte[] e = password.getBytes("ISO-8859-1");
	            byte[] s = salt.getBytes("ISO-8859-1");
	            MessageDigest md = null;
//	            if(algorithm == IAMUtil.EncryptionAlgorithm.SHA512) {
	                md = SHA512();
//	            } else {
//	                md = MD();
//	            }

	            md.reset();
	            md.update(e);
	            if(!salt.equals("0")) {
	                md.update(s);
	            }

	            byte[] digest = md.digest();
	            return BASE16_ENCODE(digest);
	        } catch (Exception var7) {
	            logger.log(Level.WARNING, null, var7);
	            return password;
	        }
	    }
	  public static MessageDigest SHA512() {
	        MessageDigest md = sha512.get();
	        if(md == null) {
	            try {
	                md = MessageDigest.getInstance("SHA-512");
	                sha512.set(md);
	            } catch (Exception var2) {
	                logger.log(Level.WARNING, null, var2);
	            }
	        }

	        return md;
	  }
	  

	    public static String BASE16_ENCODE(byte[] input) {
	        char[] b16 = new char[input.length * 2];
	        int i = 0;
	        byte[] arr$ = input;
	        int len$ = input.length;
	        for(int i$ = 0; i$ < len$; ++i$) {
	            byte c = arr$[i$];
	            int low = c & 15;
	            int high = (c & 240) >> 4;
	            b16[i++] = HEX[high];
	            b16[i++] = HEX[low];
	        }

	        return new String(b16);
	    }
	    
	EncrypterTest(SecretKey key) throws Exception
	{
		ecipher = Cipher.getInstance("DES");
		dcipher = Cipher.getInstance("DES");
		ecipher.init(Cipher.ENCRYPT_MODE, key);
		dcipher.init(Cipher.DECRYPT_MODE, key);
	}

	public String encrypt(String str) throws Exception
	{
		// Encode the string into bytes using utf-8
		byte[] utf8 = str.getBytes("UTF8");

		// Encrypt
		byte[] enc = ecipher.doFinal(utf8);

		// Encode bytes to base64 to get a string
		return new sun.misc.BASE64Encoder().encode(enc);
	}

	public static String decrypt(String str) throws Exception
	{
		// Decode base64 to get bytes
		byte[] dec = new sun.misc.BASE64Decoder().decodeBuffer(str);

		byte[] utf8 = dcipher.doFinal(dec);

		// Decode using utf-8
		return new String(utf8, "UTF8");
	}
	
	 public static String encrypt(String key, String initVector, String value) {
	        try {
	            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes(Charsets.ISO_8859_1));
	            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(Charsets.ISO_8859_1), "AES");

	            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
	            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

	            byte[] encrypted = cipher.doFinal(value.getBytes(Charsets.ISO_8859_1));
	            System.out.println("encrypted string: "
	                    + Base64.encodeBase64String(encrypted));

	            return Base64.encodeBase64String(encrypted);
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }

	        return null;
	    }

	    public static String decrypt(String key, String initVector, String encrypted) {
	        try {
	            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes(Charsets.ISO_8859_1));
	            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(Charsets.ISO_8859_1), "AES");

	            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
	            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

	            byte[] original = cipher.doFinal(Base64.decodeBase64(encrypted));

	            return new String(original);
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }

	        return null;
	    }

	    public static void main(String[] args) {
	        String key = "Bar12345Bar12345"; // 128 bit key
	        String initVector = "RandomInitVector"; // 16 bytes IV

	        System.out.println(decrypt(key, initVector,
	                encrypt(key, initVector, "123123123-uid")));
	    }
/*
	public static void main(String[] argv) throws Exception
	{
//		SecretKey key = KeyGenerator.getInstance("DES").generateKey();
//		SecretKey key = KeyGenerator.getInstance("DES").generateKey();
//		System.out.println(new String(key.getEncoded()));
////		key.getEncoded();
//		
//		
//		byte[] encoded =   "password".getBytes("ISO-8859-1");
//		SecretKey secretKey = new SecretKeySpec(encoded, "DES");
//		
////		SecretKeySpec secretKeySpec = new SecretKeySpec("test".getBytes(Charsets.UTF_8), "AES");
//		EncrypterTest encrypter = new EncrypterTest(secretKey);
//		String encrypted = encrypter.encrypt("Don't tell anybody!");
//		String decrypted = encrypter.decrypt(encrypted);
//		System.out.println("enc>> " + encrypted + " dec>> " + decrypted);
		logger.info(encrypt("testtest", "test"));
		logger.info(decrypt("623c92d8c3e80a6963599e42aa37d43f8f4f4e84c742bfe5cf26b33b6e5a281599dd9e948691b5f76566e526375ef46cc5485af55bac2a198b69b40333ac92fb"));
	}
	*/
	/*public static String decrypt(String keyLabel, String cipherText, boolean percentDecode) throws Exception {
        CryptoUtil.Encryptor et = getEncryptor(keyLabel);
        String decStr = null;

        try {
            decStr = et.decrypt(cipherText, percentDecode);
        } catch (GeneralSecurityException var10) {
            logger.log(Level.FINE, "Error in decryption try previous Encryptor: {0}", var10.getMessage());

            try {
                et = getOldEncryptor(keyLabel);
                decStr = et.decrypt(cipherText, percentDecode);
            } catch (GeneralSecurityException var9) {
                throw new IAMException("E101", var10.getMessage());
            }
        }

        int lenIdx = decStr.indexOf(LEN_DELIM);
        int calcLen = -1;
        if(lenIdx > 0) {
            try {
                calcLen = Integer.parseInt(decStr.substring(0, lenIdx));
            } catch (Exception var8) {
                ;
            }
        }

        if(calcLen > 0) {
            logger.log(Level.FINE, "Decrypted String : {0}", decStr);
            decStr = decStr.substring(lenIdx + LEN_DELIM.length());
            if(decStr.length() == calcLen) {
                return decStr;
            } else {
                logger.log(Level.WARNING, "Invalid data submitted for decryption :{0}", decStr);
                throw new IAMException("E101", "Invalid data submitted for decryption");
            }
        } else {
            return decStr;
        }
    }
*/
}
