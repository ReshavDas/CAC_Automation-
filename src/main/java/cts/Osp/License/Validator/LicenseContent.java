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

public class LicenseContent {

	
	public String ReleaseNumber;
	public String getReleaseNumber() {
		return ReleaseNumber;
	}
	public void setReleaseNumber(String N) {
		this.ReleaseNumber = N;
	}
	/// <summary>
	/// The OSP product code.
	/// </summary>
	public String ProductCode;

	public String getProductCode() {
		return ProductCode;
	}

	public void setProductCode(String N) {
		this.ProductCode = N;
	}

	/// <summary>
	/// The licensed product modules.
	/// </summary>
	public String Modules;

	public String getModules() {
		return Modules;
	}

	public void setModules(String N) {
		this.Modules = N;
	}

	/// <summary>
	/// The licensed customer.
	/// </summary>
	public String CustomerCode;

	public String getCustomerCode() {
		return CustomerCode;
	}

	public void setCustomerCode(String N) {
		this.CustomerCode = N;
	}

	/// <summary>
	/// The licensed customer environment.
	/// </summary>
	/// <remarks>
	/// A value of "ANY" indicates any environment.
	/// </remarks>
	public String EnvironmentCode;

	public String getEnvironmentCode() {
		return EnvironmentCode;
	}

	public void setEnvironmentCode(String N) {
		this.EnvironmentCode = N;
	}

	/// <summary>
	/// The type of license - Term or Perpetual.
	/// </summary>

	public String LicenseType;

	public void setLicenseTypes(String N) {
		this.LicenseType = N;
	}

	public String getLicenseTypes() {
		return this.LicenseType;
	}

	/// <summary>
	/// The expiration date of this license.
	/// </summary>
	/// <remarks>
	/// For perpetual licenses, this defaults to 12/31/2078.
	/// </remarks>
	public Date ExpirationDate;

	public Date getExpirationDate() {
		return ExpirationDate;
	}

	public void setExpirationDate(Date N) {
		this.ExpirationDate = N;
	}

	/// <summary>
	/// The number of concurrent users permitted by this license.
	/// </summary>
	/// <remarks>
	/// A value of 9999 indicates that unlimited concurrent users are permitted.
	/// </remarks>
	public int LicenseCount;

	public int getLicenseCount() {
		return LicenseCount;
	}

	public void setLicenseCount(int N) {
		this.LicenseCount = N;
	}

	/// <summary>
	/// The OSP Product Release (or Version) that is licensed.
	/// </summary>
	/// <remarks>
	/// Only the major and minor version numbers are specified, such as "5.90".
	/// All point releases within this are covered by this license.
	/// </remarks>
	public String ProductVersion;

	public String getProductVersion() {
		return ProductVersion;
	}

	public void setProductVersion(String N) {
		this.ProductVersion = N;
	}

	/// <summary>
	/// The number of days prior to the <see cref="ExpirationDate"/> to start
	/// warning of the nearing expiration.
	/// </summary>
	public int WarningPeriod;

	public int getWarningPeriod() {
		return WarningPeriod;
	}

	public void setWarningPeriod(int N) {
		this.WarningPeriod = N;
	}

	/// <summary>
	/// The number of days after the <see cref="ExpirationDate"/> to allow
	/// continued use of the product.
	/// </summary>
	public int GracePeriod;

	public int getGracePeriod() {
		return GracePeriod;
	}

	public void setGracePeriod(int N) {
		this.GracePeriod = N;
	}

	/// <summary>
	/// The generated license key for verification purposes.
	/// </summary>
	public String LicenseKey;

	public String getLicenseKey() {
		return LicenseKey;
	}

	public void setLicenseKey(String N) {
		this.LicenseKey = N;
	}

	/// <summary>
	/// The last day to use this license - <see cref="ExpirationDate"/> plus <see
	/// cref="GracePeriod"/>.
	/// </summary>
	public Date EndDate;

	public Date getEndDate() {
		Date dt = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(ExpirationDate);
		// add gracePeriodDays
		c.add(Calendar.DATE, GracePeriod);
		dt = c.getTime();
		return dt;
	};

	public void setEndDate(Date N) {
		Date dt = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(ExpirationDate);
		// add gracePeriodDays
		c.add(Calendar.DATE, GracePeriod);
		dt = c.getTime();
		this.EndDate = dt;
	}

	/// <summary>
	/// Indicates if the license is expiring within the warning period.
	/// expire-warninPeriod < today < expire
	/// </summary>
	public Boolean InInWarningPeriod(Date today) {

		Date dt = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(ExpirationDate);
		// add warningPeriodDays
		c.add(Calendar.DAY_OF_MONTH, -WarningPeriod);
		dt = c.getTime();
		
		System.out.println("expire-warning="+ dt.toString()+ " today=" + today.toString()+ " expire="+ ExpirationDate.toString() );

		if (today.after(dt) && today.before(ExpirationDate))
			return true;
		else
			return false;

		// => today > ExpirationDate?.AddDays(-WarningPeriod ?? 0d) && today <=
		// ExpirationDate;
	}

	/// <summary>
	/// Indicates if the license is expiring within its grace period.
	//expireDate < today < expireDate+GracePeriod
	/// </summary>
	public Boolean IsInGracePeriod(Date today) {
		System.out.println(" expire="+ ExpirationDate.toString()+ " today=" + today.toString()+ " EndDate=" + EndDate.toString()  );

		if (today.after(ExpirationDate) && today.before(EndDate))
			return true;
		else
			return false;

		// => today > ExpirationDate && today <= EndDate;

	}

	/// <summary>
	/// Indicates if the license is expired.
	/// </summary>
	public Boolean IsPastGracePeriod(Date today) {
		System.out.println(" expire="+ ExpirationDate.toString()+ " today=" + today.toString()+ " EndDate=" + EndDate.toString()  );
		
		if (today.after(EndDate))
			return true;
		else
			return false;

		// => today > EndDate;
	}
}
