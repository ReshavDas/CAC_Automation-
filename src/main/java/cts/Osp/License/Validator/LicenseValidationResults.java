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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/// <summary>
/// Contains the results of the license validation
/// </summary>
public class LicenseValidationResults {
	private static String WARNING_PERIOD_MESSAGE = 
			"Your software license is set to expire on %1$s. You have until %2$s to acquire a new license key. Contact Cognizant through a Service Desk ticket to Optimization Software Products to resolve the issue. If requesting a new license key please indicate the release number the new license key should apply to in your Service Desk ticket.";
	private static String GRACE_PERIOD_MESSAGE = 
			"Your software license expired on %1$s. You have until %2$s to acquire a new license key. Contact Cognizant through a Service Desk ticket to Optimization Software Products to resolve the issue. If requesting a new license key please indicate the release number the new license key should apply to in your Service Desk ticket.";
	
	private static String LICENSE_KEY_EXPIRED = 
			"Your software license expired on %s. Contact Cognizant through a Service Desk ticket to Optimization Software Products to resolve the issue. If requesting a new license key please indicate the release number the new license key should apply to in your Service Desk ticket.";

	public static int licenseDetermination = 0;
	public static String message = "";
	
	private static LicenseValidationResults _validResults() {
		LicenseValidationResults lvr = new LicenseValidationResults();  
		lvr.IsValid = true;
		licenseDetermination = 3;
		//set the message - I don't care to do it here since I am valid
		return lvr;
	}

	/// <summary>
	/// Constructs a new <see cref="LicenseValidationResults"/> instance for a valid
	/// license.
	/// </summary>
	/// <returns>A new <see cref="LicenseValidationResults"/> instance for a valid
	/// license.</returns>
	public static LicenseValidationResults Valid() {
		return LicenseValidationResults._validResults();
	}

	/// <summary>
	/// Constructs a new <see cref="LicenseValidationResults"/> instance for a valid
	/// license
	/// that is nearing expiration.
	/// </summary>
	/// <param name="expirationDate">The license expiration date.</param>
	/// <param name="endDate">The license end date.</param>
	/// <returns>A new <see cref="LicenseValidationResults"/> instance for a valid
	/// license nearing its expiration.</returns>
	public static LicenseValidationResults InWarningPeriod(Date expirationDate, Date endDate)
    {
		//validate the sent values are not null
    	if( expirationDate == null) 
    		throw new NullPointerException("ExpireDate is null");
    	if( endDate == null) 
    		throw new NullPointerException("EndDate is null"); 
    	
    	LicenseValidationResults lvr = new LicenseValidationResults();     	
    	lvr.IsValid = true;
    	lvr.licenseDetermination = 4;
    	

    	//create the warning message
    	String expireDate = expirationDate.toString(); 
    	String graceDate = endDate.toString(); 
    	lvr.message=String.format(WARNING_PERIOD_MESSAGE, expirationDate, graceDate);
        return lvr;
	}

	/// <summary>
	/// Constructs a new <see cref="LicenseValidationResults"/> instance for an
	/// expired license that is within its grace period.
	/// </summary>
	/// <param name="expirationDate">The license expiration date.</param>
	/// <param name="endDate">The license end date.</param>
	/// <returns>A new <see cref="LicenseValidationResults"/> instance for a valid
	/// license within its grace period.</returns>
	public static LicenseValidationResults InGracePeriod(Date expireDate, Date endDate)
    {
		//validate the sent values are not null
    	if(expireDate == null)throw new NullPointerException("ExpireDate is null");
    	if(endDate == null)throw new NullPointerException("EndDate is null");
   
    	LicenseValidationResults lvr = new LicenseValidationResults(); 
    	lvr.IsValid = true;    
    	lvr.licenseDetermination = 2;	
    	
    	//create the grace message  
    	lvr.message=String.format(GRACE_PERIOD_MESSAGE, expireDate, endDate);
		return lvr;
    };

	/// <summary>
	/// Constructs a new <see cref="LicenseValidationResults"/> instance for an
	/// expired license.
	/// </summary>
	/// <param name="expirationDate">The license expiration date.</param>
	/// <returns>A new <see cref="LicenseValidationResults"/> instance for an
	/// expired license.</returns>
	public static LicenseValidationResults Expired(Date expirationDate) {
		if (expirationDate == null)
			throw new NullPointerException();

		LicenseValidationResults lvr = new LicenseValidationResults(); 
		
		lvr.IsValid = false;
		lvr.licenseDetermination = 5;	
		
		//get expirationDate to a string format
		String expireDate = expirationDate.toString();
    	lvr.message=String.format(LICENSE_KEY_EXPIRED, expireDate);
		return lvr;		
	}

	/// <summary>
	/// Constructs a new <see cref="LicenseValidationResults"/> instance for an
	/// invalid license.
	/// </summary>
	/// <param name="errorType">The type of error encountered.</param>
	/// <param name="message">The formatted message text.</param>
	/// <returns>A new <see cref="LicenseValidationResults"/> instance for an
	/// invalid license.</returns>
	// LicenseValidationResults.Error(ErrorType.Invalid,
	/// String.format(LICENSE_KEY_INVALID, license.LicenseKey));

	public static LicenseValidationResults Error(ErrorType errorType, String message) {
		if (message.isEmpty())
			throw new NullPointerException();
		if (errorType == null)
			throw new NullPointerException();
		
		LicenseValidationResults lvr = new LicenseValidationResults();
		
		lvr.IsValid = false;		
		lvr.licenseDetermination = 1;	
    	lvr.message= message; 

		return lvr;
	}



	/// <summary>
	/// Indicates if the license is valid and not fully expired (past grace period).
	/// </summary>
	public static Boolean IsValid;

	public Boolean getIsValid() {
		return IsValid;
	}

	public void setIsValid(Boolean N) {
		IsValid = N;
	}



/// <summary>
/// Encodes the error type.
/// </summary>
	public enum ErrorType {
		/// <summary>
		/// No error.
		/// </summary>
		None,

		/// <summary>
		/// License Key is not not found or empty.
		/// </summary>
		KeyNotFound,

		/// <summary>
		/// Required license fields are not found or empty.
		/// </summary>
		InfoNotFound,

		/// <summary>
		/// The license key is incorrect.
		/// </summary>
		Invalid,

		/// <summary>
		/// The license key is not compatible with the specified product version.
		/// </summary>
		NotCompatible,

		/// <summary>
		/// The license key has expired and is past the grace period.
		/// </summary>
		Expired
	}
	
	public static String   convertDateToString(Date date)
    {
		String pattern = "MM/dd/yyyy";  //consider putting this as an argument
       
		DateFormat df = new SimpleDateFormat(pattern);

        return df.format(date);
    }
	
}  //end of class

/// <summary>
/// Defines the Grace Period message.
/// </summary>
/// <param name="DaysCrossed">Days beyond the license expiration date.</param>
/// <param name="DaysRemaining">Days remaining until the license end date, after which users are locked out until renewed.</param>
/// <param name="Message">The formatted warning message text.</param>

class GraceMessage{
	String graceMessage;
	int graceDaysCrossed;
	int graceDaysRemaining;
	
	public GraceMessage(int daysCrossed, int daysRemaining, String message) {
		this.setGraceMessage(daysCrossed,daysRemaining, message);
	}
	private void setGraceMessage(int daysCrossed, int daysRemaining, String message) {	
		this.graceMessage=message;
		this.graceDaysCrossed=daysCrossed;
		this.graceDaysRemaining=daysRemaining;
	}
	public GraceMessage getGraceMessage() {
		return this;
	}
};

class ErrorMessage{
	cts.Osp.License.Validator.LicenseValidationResults.ErrorType errortype;
	String errorMessage;
	
	public ErrorMessage(cts.Osp.License.Validator.LicenseValidationResults.ErrorType errortype, String message) {
		this.setErrorMessage(errortype, message);
	}
	private void setErrorMessage(cts.Osp.License.Validator.LicenseValidationResults.ErrorType errortype, String message) {
		this.errorMessage=message;
		this.errortype=errortype;
	}
	public ErrorMessage getErrorMessage() {
		return this;
	}
};
