package cts.qea.automation.runners;

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

import java.util.ArrayList;

import cts.qea.automation.TestCase;
import cts.qea.automation.annotations.Tags;

public class TestCaseFilter {

	private ArrayList<String> includeTag = new ArrayList<String>();
	private ArrayList<String> excludeTag = new ArrayList<String>();

	TestCaseFilter(){

	}

	/**
	 * <b>Description</b>Tag name which is to be included in the filter 
	 * @param		tagName tag name which is to be Included
	 */
	public void includeTag(String tagName){
		includeTag.add(tagName);
	}

	/**
	 * <b>Description</b>Tag name which is to be Excluded in the filter 
	 * @param		tagName tag name which is to be Excluded
	 */
	public void excludeTag(String tagName){
		excludeTag.add(tagName);
	}

	/**
	 * <b>Description</b>Returns boolean depending upon argument test case with the excluded and included tags 
	 * @param		test tag name which is to be Excluded
	 */
	public boolean isMatching(Class<TestCase> test){
		try{
			String [] tagValues = test.getClass().getAnnotation(Tags.class).value();		    
			for(int i=0;i<tagValues.length;i++){
				if(excludeTag.contains(tagValues[i]))
					return false;			
			}			
			for(int i=0;i<tagValues.length;i++){			 
				if(includeTag.contains(tagValues[i]))
					return true;				 
			}
		}catch(Exception e){
			e.printStackTrace();
		} 
		return false; //or false
	}

}
