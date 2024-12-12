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

import java.util.Calendar;
import java.util.Date;

import cts.Osp.License.Validator.LicenseValidationResults.ErrorType;

public class LicenseValidator
{
    private static String LICENSE_KEY_NOT_FOUND = "License key not found. The application cannot be started. Contact Cognizant through a Service Desk ticket to Optimization Software Products to resolve the issue. If requesting a new license key please indicate the release number the new license key should apply to in your Service Desk ticket.";
    private static String LICENSE_INFO_NOT_FOUND = "License information not found. The application cannot be started. Contact Cognizant through a Service Desk ticket to Optimization Software Products to resolve the issue. If requesting a new license key please indicate the release number the new license key should apply to in your Service Desk ticket.";
    private static String LICENSE_KEY_INVALID = "Invalid license key %s. The application cannot be started. Contact Cognizant through a Service Desk ticket to Optimization Software Products to resolve the issue. If requesting a new license key please indicate the release number the new license key should apply to in your Service Desk ticket.";
    private static String LICENSE_KEY_NOT_COMPATIBLE = "Your software license key %1s is not compatible with %2s. The application cannot be started. Contact Cognizant through a Service Desk ticket to Optimization Software Products to resolve the issue. If requesting a new license key please indicate the release number the new license key should apply to in your Service Desk ticket.";

    /// <summary>
    /// This method will validate the License Key Params which has license information and provide the response which includes
    /// IsValid, Error, Warning Period and Grace Period Information.
    /// </summary>
    /// <param name="license">License Key Params</param>
    /// <param name="productCode">The product code.</param>
    /// <param name="productVersion">The product version number.</param>
    /// <param name="isLegacyCTP">Indicates if this license is for CTP legacy.</param>
    /// <returns>License Key Response</returns>
    public static LicenseValidationResults Validate(LicenseContent license, String productCode, String productVersion, String productModule) throws Exception
    {
    	//ensure sent values are populated
    	if(productCode.isEmpty()) throw new Exception();
    	if(productVersion.isEmpty()) throw new Exception();
    	if(productModule.isEmpty()) throw new Exception();

        // Overwrite the license values with the one sent.  If it is wrong, the license key won't match.
        license.setProductCode(productCode);
        license.setReleaseNumber(productVersion);
        license.setProductVersion(productVersion);
        license.setModules(productModule);
        
        //HANDLE ALL ERROR SCENARIOS
    	//if licenseKey value is empty return ERROR
        if (license.LicenseKey.length() == 0) {
            return LicenseValidationResults.Error(LicenseValidationResults.ErrorType.KeyNotFound, LICENSE_KEY_NOT_FOUND);
        }
        
        //if customer code is empty, return ERROR
        // Check required data...
        if(license.CustomerCode.isEmpty() || license.EnvironmentCode.isEmpty() || license.ReleaseNumber.isEmpty())
            return LicenseValidationResults.Error(ErrorType.InfoNotFound, LICENSE_INFO_NOT_FOUND);

        // Verify key...
        String key = Digester.Digest(license, false, "xyz", 0);
        if (!key.equals(license.LicenseKey)) {
        	return LicenseValidationResults.Error(ErrorType.Invalid, String.format(LICENSE_KEY_INVALID, license.LicenseKey));

         }
        
        // Verify release...
        if ((productVersion.isEmpty() || license.ProductVersion.isEmpty() || !productVersion.startsWith(license.ProductVersion)))
        {
        	System.out.println("License key is not compatible.  Please request a new key that corresponds with the version of code installed.");
            return LicenseValidationResults.Error(ErrorType.NotCompatible, String.format(LICENSE_KEY_NOT_COMPATIBLE,
                license.LicenseKey, productVersion.length()>0 ? productVersion : "current release"));
        }

        //get today into a Date object for the methods
        Calendar today = Calendar.getInstance();
        Date dt = new Date(); 
		dt = today.getTime();	
		
        // Check for expiration...
        if (license.IsPastGracePeriod(dt))
            return LicenseValidationResults.Expired(license.getExpirationDate());

        // Check for grace period...
        if (license.IsInGracePeriod(dt))
            return LicenseValidationResults.InGracePeriod(license.ExpirationDate, license.EndDate);

        // Warn if nearing expiration...
        if (license.InInWarningPeriod(dt))
            return LicenseValidationResults.InWarningPeriod(license.ExpirationDate, license.EndDate);

        // All validation passed...
        return LicenseValidationResults.Valid();
    }
}
