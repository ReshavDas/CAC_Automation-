package cts.qea.automation;

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

import java.util.*;
import java.util.concurrent.*;

public class UserList {

	/**
	 * @param args
	 */
	static volatile ConcurrentHashMap <String, LinkedHashMap<String, LinkedBlockingQueue <UserInfo>>> users = null;
	static volatile boolean initStatus = false;

	public static void initUsers(){
		if(initStatus) return;
		if(users == null){
			users = new ConcurrentHashMap <String, LinkedHashMap<String, LinkedBlockingQueue <UserInfo>>>();
		}
		initStatus = true;
	}

	/**
	 * Used to get login user name and password for the application user by passing 
	 * @param appName : application name
	 * @param role: role where define in User details in envConfig sheet
	 * @return
	 * @throws InterruptedException
	 */
	public final static synchronized UserInfo takeUser(String appName, String role) throws InterruptedException {
		role = role.toLowerCase();
		UserInfo userDetails = null;
		try {
			//configure the time from config file
			userDetails = users.get(appName).get(role).poll(10, TimeUnit.MINUTES);
		} catch (Exception e) {
			userDetails = null;
			throw new RuntimeException("No Active Users Available with Role: "+role);
		}
		return userDetails;
	}
    
	/**
	 * used to add new user
	 * @param appName
	 * @param role
	 * @param userDetails
	 */
	public final static synchronized void putUser(String appName, String role, UserInfo userDetails){
		// Initialize users object
		role = role.toLowerCase();
		initUsers();
		if(!users.containsKey(appName)) users.put(appName, new LinkedHashMap<String, LinkedBlockingQueue <UserInfo>>());
		if(!users.get(appName).containsKey(role)) users.get(appName).put(role, new LinkedBlockingQueue <UserInfo>());
		users.get(appName).get(role).add(userDetails);
	}
}