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

import cts.qea.automation.TestCase;
import cts.qea.automation.WrappedWebDriver;
import cts.qea.automation.WrappedWiniumDriver;
import cts.qea.automation.utils.DateHelper;

import javax.xml.bind.annotation.XmlAttribute;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TestCaseElement {
	@XmlAttribute public String tcid;
	@XmlAttribute public String uname;
	@XmlAttribute public String runid;

	public final String name;
	public String description;
	public final String iteration;
	public final String day;
	public final String totaldays;
	public final String starttime;
	public String browserName;
	public String browserVersion;
	public String buildVersion;
	public Status status;
	public String endtime;
	public int pass = 0, fail=0, warning=0, skip=0;
	public List<MetaInfo> metainfo;

	//not part of XML
	public int rStepCounter=0, bStepCounter=0;
	public String resultFolderPath;
	public String resultFileName;

	@SuppressWarnings("unused")
	private static class MetaInfo {
		public String key;
		public String value;
	}

	private TestCase tc;

	//dummy function to supress JAXB error while generating HTML
	//TODO: to be removed
	public TestCaseElement(){
		tcid = UUID.randomUUID().toString();
		name = "undefined_"+tcid;
		description="undefined";
		iteration="undefined";
		day="undefined";
		totaldays="undefined";
		browserName = "undefined";
		browserVersion = "undefined";
		buildVersion = "undefined";
		starttime = DateHelper.getCurrentDatenTime("hh:mm:ss");
	}

	public TestCaseElement(TestCase tc, String runid) {
		tcid = UUID.randomUUID().toString();
		this.runid = runid;
		name = tc.getAttribute("name");;
		description = tc.getAttribute("description");
		iteration = tc.getAttribute("iteration");

		if(name.equalsIgnoreCase(iteration)){
			uname = name;
		}else{
			uname = name+"_"+iteration;
		}

		day = tc.getAttribute("day");
		totaldays = tc.getAttribute("totalDays");
		starttime = DateHelper.getCurrentDatenTime("hh:mm:ss");
		status = Status.DONE;
		browserName = tc.getAttribute("browserName");
		browserVersion = tc.getAttribute("browserVersion");
		buildVersion = tc.getAttribute("buildVersion");

		metainfo = new ArrayList<MetaInfo>();
		try {
			this.tc = tc;
		} catch (Exception e) { /*nothing to worry*/ }
	}

	public void addMetaInfo(String key, String value){
		MetaInfo meta = new MetaInfo();
		meta.key = key;
		meta.value = value;
		metainfo.add(meta);
	}

	public WrappedWebDriver getWebDriver(){
		try {
			return tc.getWebDriver();
		} catch (Exception e) {
		}
		return null;
	}
	
	public WrappedWiniumDriver getWiniumDriver(){
		try {
			return tc.getWiniumDriver();
		} catch (Exception e) {
		}
		return null;
	}
}
