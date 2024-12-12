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

import java.util.UUID;
import javax.xml.bind.annotation.*;

public class SummaryElement {
	@XmlAttribute String runid;
	@XmlElement String title;
	@XmlElement String startTime;
	@XmlElement String endTime;
	@XmlElement String environment;
	@XmlElement String build;
	@XmlElement int tcpass;
	@XmlElement int tcfail;
	@XmlElement int tcwarning;
	@XmlElement int tctotal;
	
	public SummaryElement(){
		runid = UUID.randomUUID().toString();
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	
	public void setStartTime(String time){
		this.startTime = String.valueOf(time);
	}
	
	public void setEndTime(String time){
		this.endTime = String.valueOf(time);
	}
	
	public void setEnv(String env){
		this.environment = env;
	}
	
	public void setBuild(String var){
		this.build = var;
	}
	
	public void incPassCnt() { tcpass++; }
	public void incFailCnt() { tcfail++; }
	public void incWarnCnt() { tcwarning++; }
	public void incTotalCnt() { tctotal++; }
}
