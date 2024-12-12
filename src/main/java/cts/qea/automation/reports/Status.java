package cts.qea.automation.reports;

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

public enum Status {
	/** Debug: for reporting debug related logs
	 * Debug status is Deprecated and Unused*/
	@Deprecated
	DEBUG,
	/** DONE: for reporting action items and test data used */
	DONE,
	/** PASS: for reporting successful logs without screen shots
	 * This doesn't take Screenshot
	 */
	PASS,
	/** FAIL: for reporting failure logs with screen shots */
	FAIL,
	/** WARN: for reporting warning logs */
	WARN,
	/** Pass: for reporting successful logs with screen shots
	 * This takes Screenshot
	 */
	Pass,
	/** Fail: for reporting failure logs with screen shots */
	Fail,
	/** fail: for reporting failure logs without screen shots */
	fail,//390661
	/** BUSINESSSTEP: for reporting business steps */
	BUSINESSSTEP,
	KEYWORD,
	/** New Status added for 5.0.9 version
	 * SKIP: Test Skipped
	 */
	SKIP
}
