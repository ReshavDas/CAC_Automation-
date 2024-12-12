package cts.Osp.License.Validator;

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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.Format;
import java.text.SimpleDateFormat;



/// <summary>
/// Cryptographic hashing used for license key generation.
/// </summary> 
public class Digester {
	private static int KEY_SIZE = 32;
	private static String CRYPTO_PEPPER = "M3d!c@l";

	/// <summary>
	/// Generates a license key.
	/// </summary>
	/// <remarks>
	/// This is a <see langword="protected internal"/> method to avoid giving direct
	/// access by a bad actor, while also allowing
	/// the Generator to use this same code by extending this base class there.
	/// </remarks>
	/// <param name="licenseKeyParams">The license parameters.</param>
	/// <param name="mapLegacyModules">Set to <see langword="true"/> for legacy CTP
	/// licenses only.</param>
	/// <param name="pepper">Additional data added prior to digest.</param>
	/// <param name="keySize">The final key size in bytes.</param>
	/// <returns>The generated license key.</returns>
	public static String Digest(LicenseContent licenseKeyParams, Boolean mapLegacyModules1, String pepper1, int keySize1)
    {
    	String pepper = CRYPTO_PEPPER;
    	Boolean mapLegacyModules = false; //we are not CTP so we really don't care about this.  Always false
   	    	
        String plainText = GetPlainText(licenseKeyParams, mapLegacyModules);
        plainText += pepper;
        
        MessageDigest digest = null;
        StringBuilder sb = new StringBuilder();
        
		try {
			//create digest
			digest = MessageDigest.getInstance("SHA-256");
			
			//add the password bytes to the digest
			digest.update(plainText.getBytes());
			
			//get the hash's bytes
			byte[] bytes = digest.digest();
			
			//convert to hex		    
		    for (int i = 0; i < bytes.length; i++) {
		        sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
		    }
		    
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        return sb.toString();
    }

	private static String GetPlainText(LicenseContent license, Boolean isLegacyCTP)
    {
    	Format formatter = new SimpleDateFormat("MM/dd/yyyy");
    	String expDateAsString = formatter.format(license.ExpirationDate);
    	StringBuilder plainTextBuilder = new StringBuilder(100);
    	
        if(isLegacyCTP)
        {
        	//not using as the value above is always false
			/*
			 * plainTextBuilder.append(license.CustomerCode.trim());
			 * plainTextBuilder.append(license.EnvironmentCode.trim());
			 * plainTextBuilder.append(String.valueOf(license.LicenseCount));
			 * plainTextBuilder.append(expDateAsString);
			 * //plainTextBuilder.append(MapLegacyModules(license.Modules.trim())); return
			 * plainTextBuilder.toString();
			 */
        }
        else
        {        
        	plainTextBuilder.append(expDateAsString);
        	plainTextBuilder.append(license.Modules.trim());
        	plainTextBuilder.append(String.valueOf(license.LicenseCount));
        	plainTextBuilder.append(String.valueOf(license.GracePeriod));
        	plainTextBuilder.append(license.EnvironmentCode.trim());
        	plainTextBuilder.append(license.ReleaseNumber.trim());
        	plainTextBuilder.append(license.LicenseType);
        	plainTextBuilder.append(license.ProductCode.trim());
        	plainTextBuilder.append(license.CustomerCode.trim());
        }
        return plainTextBuilder.toString();
    }

	/*
	 * private static String MapLegacyModules(String modules) { return
	 * modules.contains('1') ? modules.Contains('2') && modules.Contains('3') ? "5"
	 * : modules.Contains('2') ? "3" : modules.Contains('3') ? "4" :
	 * modules.Contains('4') ? "7" : "1" : modules.Contains('3') ? "2" :
	 * modules.Contains('4') ? "6" : "0"; }
	 */
}
