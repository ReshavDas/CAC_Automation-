/**
 * 
 */
package cts.qea.automation.security;

/**********************************************************************
* COGNIZANT CONFIDENTIAL OR TRADE SECRET
*
* Copyright 2020 - 2023 Cognizant.  All rights reserved.
*
* NOTICE:  This unpublished material is proprietary to Cognizant and 
* its suppliers, if any.  The methods, techniques and technical 
* concepts herein are considered Cognizant confidential or trade 
* secret information.  This material may also be covered by U.S. or
* foreign patents or patent applications.  Use, distribution or 
* copying of these materials, in whole or in part, is forbidden, 
* except by express written permission of Cognizant.
***********************************************************************/

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang3.StringUtils;


public class SecurityUtils {
	private static SecretKeySpec createSecretKey(char[] password, byte[] salt, int iterationCount, int keyLength)
			throws NoSuchAlgorithmException, InvalidKeySpecException {
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(Messages.getString("keyalgorithm"));
		PBEKeySpec keySpec = new PBEKeySpec(password, salt, iterationCount, keyLength);
		SecretKey keyTmp = keyFactory.generateSecret(keySpec);
		return new SecretKeySpec(keyTmp.getEncoded(), Messages.getString("secretkeyalgorithm"));
	}

	private static String decrypt(String string, SecretKeySpec key) throws GeneralSecurityException, IOException {
		try {
			String iv = string.split(":")[0];
			String property = string.split(":")[1];
			Cipher pbeCipher = Cipher.getInstance(Messages.getString("cipheralgorithm"));
			pbeCipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(base64Decode(iv)));
			return new String(pbeCipher.doFinal(base64Decode(property)), "UTF-8");
		}catch(ArrayIndexOutOfBoundsException e) {
			return StringUtils.EMPTY;
		}
	}

	private static byte[] base64Decode(String property) throws IOException {
		return Base64.getDecoder().decode(property);
	}
	
	public static String getDecodedValue(String encrp) throws GeneralSecurityException, IOException {
		String password = Messages.getString("password"); 
        byte[] salt = new String(Messages.getString("salt")).getBytes(); 
        int iterationCount = 40000;
        int keyLength = 128;
		SecretKeySpec key = createSecretKey(password.toCharArray(), salt, iterationCount, keyLength);
		return decrypt(encrp, key);
	}
	
	public static String getDecodedString(String encoded) {
		// decode into String from encoded format
		byte[] actualByte = Base64.getDecoder().decode(encoded);
		return new String(actualByte);
	}
}
