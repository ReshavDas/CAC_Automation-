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

import java.io.*;
import java.util.*;
import org.ini4j.Ini;
import cts.qea.automation.*;

public class IniHelper {

	/**
	 * <b>Description</b> Save Data Provider to Ini file
	 * @param             fileName    Ini File Name
	 * @param             dp          Data Provider
	 * @throws            Exception   Exception
	 * 
	 */	

	public  static void save(String fileName,DataProvider dp) throws Exception{		
		Ini ini = new Ini();				
		File file =new File(fileName);
		if(!file.exists()){
			file.createNewFile();
		}
		Iterator<String> groupNamesIterator = dp.groupNameIterator();	
		while (groupNamesIterator.hasNext()) {
			String groupName = groupNamesIterator.next().toString();	
			Iterator<String> columnIterator = dp.columnIterator(groupName);	
			while(columnIterator.hasNext()){
				String columnName = columnIterator.next().toString();
				String value = dp.get(groupName,columnName).toString();				
				ini.put(groupName,columnName, value);}
		}	
		ini.store(new FileWriter(fileName));
	}

	/**
	 * <b>Description</b> Load Data Provider with the data in the supplied File Name
	 *  
	 * @param             fileName    Ini File Name
	 * @throws            Exception   Exception
	 * 
	 */	
	public  static DataProvider load(String fileName) throws Exception {
		DataProvider dp = new DataProvider();
		Ini ini = new Ini();				
		File file = new File(fileName);
		if(!file.exists()){
			return dp;
		}					
		ini.load(new FileReader(fileName));		
		Set<String> groupNames = ini.keySet();	 
		Iterator<String> groupNamesIterator = groupNames.iterator();	  
		while(groupNamesIterator.hasNext()){
			String groupName  = groupNamesIterator.next().toString();	
			Map<String, String> map = ini.get(groupName);
			Set<String> columnNames =  map.keySet();
			Iterator<String> columnNamesIterator = columnNames.iterator();
			while(columnNamesIterator.hasNext()){
				String columnName = columnNamesIterator.next().toString();
				String value = ini.get(groupName, columnName);
				dp.set(groupName, columnName, value);				 
			}
		}		
		return dp;
	}

}
