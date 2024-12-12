package cts.qea.automation.annotations;

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

import java.lang.annotation.*;

/**
 * <b>Description</b> Test case Description
 *  
 * 
 */

@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface Author {
	String[] value();
}
