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

@SuppressWarnings("serial")
public class ActionNotSupportedException extends RuntimeException{
	
	/**
	 * <b>Description</b>Throws Action Not Supported Exception, for the FORM actions such as Save ,Submit ,Reset
	 *  
	 * 
	 */
	
	public ActionNotSupportedException(String problem){		
    	 super(problem);
     }
	
}


