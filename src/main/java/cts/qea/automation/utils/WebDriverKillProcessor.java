package cts.qea.automation.utils;

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

import java.io.IOException;

public class WebDriverKillProcessor {
	
	public static void killOpenedWebBroser() {
		try {
//			Runtime.getRuntime().exec("taskkill /im chrome.exe /f /t");
//			Runtime.getRuntime().exec("taskkill /im chromedriver.exe /f /t");
//			System.out.println("Kill opened Chrome Browser....Done");
//			Runtime.getRuntime().exec("taskkill /im iexplore.exe /f /t");
			Runtime.getRuntime().exec("taskkill /im msedge.exe /f /t");
			System.out.println("Close opened browser....Done");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String ar[]) {
		killOpenedWebBroser();
	}

}
