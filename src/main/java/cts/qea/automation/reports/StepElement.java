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

import javax.xml.bind.annotation.*;


public class StepElement {
	@XmlAttribute public String stepid;
	@XmlAttribute public String tcid;
	public String description;
	public String status;
	public String time;
	public String screenshot;
	public String screenShotPath;
}
