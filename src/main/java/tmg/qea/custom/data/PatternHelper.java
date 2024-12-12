/**
 * 
 */
package tmg.qea.custom.data;

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
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cts.qea.automation.reports.Report;

/**
 *
 */
@Deprecated
public class PatternHelper {

	public static List<String> parseCurlyBraces(String data) {
		List<String> returndata = new ArrayList<String>();
		Matcher m = Pattern.compile("\\{(.*?)\\}").matcher(data);
	     while(m.find()) {
	    	 returndata.add(m.group(1));
	     }
	     return returndata;
	}
	
	public static List<String> parseSquareBraces(String data) {
		List<String> returndata = new ArrayList<String>();
		Matcher m = Pattern.compile("\\(([^)]+)\\)").matcher(data);
	     while(m.find()) {
	    	 returndata.add(m.group(1));
	     }
	     return returndata;
	}
	
	public static List<String> parseSquare(String data) {
		List<String> returndata = new ArrayList<String>();
		Matcher m = Pattern.compile("\\[(.*?)\\]").matcher(data);
	     while(m.find()) {
	    	 returndata.add(m.group(1));
	     }
	     return returndata;
	}
	
	public static String replaceValueBetweenCurlyBraces(String originaldata, ExcelDataAccess excel, int rowNum) {
		StringBuffer sb= new StringBuffer();
		Matcher m = Pattern.compile("\\{(.*?)\\}").matcher(originaldata);
		 while(m.find()) {
			 String repString = null;
			try {
				repString = excel.getValue(rowNum, m.group(1));
			} catch (Exception e) {
				Report.log(e);
				e.printStackTrace();
			}
			 if (repString !=null)
				 m.appendReplacement(sb, repString);
		 }
		m.appendTail(sb);		
		return sb.toString();
	}
}
