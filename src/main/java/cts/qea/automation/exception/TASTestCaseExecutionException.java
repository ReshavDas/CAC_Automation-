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

package cts.qea.automation.exception;

public class TASTestCaseExecutionException extends Exception {

	private static final long serialVersionUID = 1L;

	public TASTestCaseExecutionException() {
		
	}

	public TASTestCaseExecutionException(String message) {
		super(message);
	}

	public TASTestCaseExecutionException(Throwable cause) {
		super(cause);
	}

	public TASTestCaseExecutionException(String message, Throwable cause) {
		super(message, cause);
	}

	public TASTestCaseExecutionException(String message,
			Throwable cause,
			boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
