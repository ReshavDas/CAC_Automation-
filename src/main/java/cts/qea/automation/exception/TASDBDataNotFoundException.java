package cts.qea.automation.exception;

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

public class TASDBDataNotFoundException extends Exception {
	private static final long serialVersionUID = 7017197706664035749L;

	public TASDBDataNotFoundException() {
		
	}

	public TASDBDataNotFoundException(String message) {
		super(message);
	}

	public TASDBDataNotFoundException(Throwable cause) {
		super(cause);
	}

	public TASDBDataNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public TASDBDataNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
