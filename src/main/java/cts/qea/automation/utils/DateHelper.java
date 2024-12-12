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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import cts.qea.automation.Config;
import cts.qea.automation.DataProvider;

public class DateHelper {
	private static final String DATE_FORMAT = "dateFormat";
	private static final String TEST_DATA = "testData";
	/**
	 * <b>Description</b> Gets Current date
	 * @param          format format of the date
	 * @return         date Current date with specified format
	 */
	public static String getCurrentDate(String format){		
		DateFormat dateFormat = new SimpleDateFormat(format);
		Date date = new Date();
		return dateFormat.format(date);
	}

	/**
	 * <b>Description</b> Gets Current date and time
	 * @param          format format of the date
	 * @return         date Current date with specified format
	 */
	public static String getCurrentDatenTime(String format){
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(cal.getTime());
	}
	/**
	 * <b>Description</b> Gets Formatted time
	 * @param          time  long time
	 * @return         time  long time
	 */

	public static String getFormattedTime(long time){
		long timeMillis = time;
		long time1 = timeMillis / 1000;
		String seconds = Integer.toString((int)(time1 % 60));
		String minutes = Integer.toString((int)((time1 % 3600) / 60));
		String hours = Integer.toString((int)(time1 / 3600));
		for (int i = 0; i < 2; i++) {
			if (seconds.length() < 2) {
				seconds = "0" + seconds;
			}
			if (minutes.length() < 2) {
				minutes = "0" + minutes;
			}
			if (hours.length() < 2) {
				hours = "0" + hours;
			}
		}
		return hours+": "+minutes+": "+seconds;
	}

	/**
	 * <b>Description</b> Date Converter for excel values to E MMM dd HH:mm:ss Z yyyy format
	 * @param          excelDate  Date value to convert
	 * @return         formatedDate  formatted date
	 */
	public static String dateConverter(String excelDate){	
		String formatedDate = null;
		try{
			DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
			Date date = (Date)formatter.parse(excelDate);

			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			formatedDate = (cal.get(Calendar.MONTH) + 1) + "/"+cal.get(Calendar.DATE) + "/" +         cal.get(Calendar.YEAR);
		}catch(Exception e){
			e.printStackTrace();	
		}
		return formatedDate;
	}

	/**
	 * <b>Description</b> Date Converter for excel values with the specified Date Format
	 * @param          excelDate  Date value to convert
	 * @param          dateFormat format to convert
	 * @return         formatedDate  formatted date
	 */
	@SuppressWarnings("deprecation")
	public static String dateConverter(String excelDate,String dateFormat){
		Date myDate = new Date(excelDate);
		String date ="";
		try{
			DateFormat formatter = new SimpleDateFormat(dateFormat);
			date = formatter.format(myDate);
			date = date.replace("-", "/");		
		}catch(Exception e){
			return "";
		}

		return date;
	}	

	/**
	 * <b>Description</b> calculates Date with the specified type of Date (APPDATE, PCDATE)
	 * @param          excelValue  test data
	 * @param          typeOfDate  type of date (APPDATE, PCDATE)
	 */
	public static String calculateDate(String excelValue,String typeOfDate){
		String dateValue = StringUtils.EMPTY;
		try{
			String year = "";
			String month = "";
			String day = "";  
			String [] values = {"",""};
			if(excelValue.contains("+")) {
				values = excelValue.split("\\+");
			}
			if(excelValue.contains("-")){
				values = excelValue.split("-");
			}
			int len = values[1].length();
			if(len == 9){
				year = values[1].substring(0,2);
				month = values[1].substring(3,5);
				day = values[1].substring(6,8);
			} else if(len == 6){
				String first3 = values[1].substring(0,3);
				if(first3.contains("Y")){
					year = values[1].substring(0,2);
					month = values[1].substring(3,5);                      
				}else if(first3.contains("M")){
					month = values[1].substring(0,2);                     
					day = values[1].substring(3,5);                       
				}                 
			} else if(len == 3){
				if(values[1].contains("Y")){
					year = values[1].substring(0,2);
				}else if(values[1].contains("M")){         
					month = values[1].substring(0,2);               
				}else if (values[1].contains("D")){
					day =values[1].substring(0,2);
				}           
			} else if(len == 4){
				if(values[1].contains("Y")){
					year = values[1].substring(0,3);
				}else if(values[1].contains("M")){         
					month = values[1].substring(0,3);               
				}else if (values[1].contains("D")){
					day =values[1].substring(0,3);
				}           
			} else if(len == 5){
				if(values[1].contains("Y")){
					year = values[1].substring(0,4);
				}else if(values[1].contains("M")){         
					month = values[1].substring(0,4);               
				}else if (values[1].contains("D")){
					day =values[1].substring(0,4);
				}           
			} else if(len == 2){
				if(values[1].contains("Y")){
					year = values[1].substring(0,1);
				}else if(values[1].contains("M")){         
					month = values[1].substring(0,1);               
				}else if (values[1].contains("D")){
					day =values[1].substring(0,1);
				}           
			}

			if(StringUtils.isBlank(day)){
				day="0";
			}
			if(StringUtils.isBlank(month)){
				month = "0";
			}
			if(StringUtils.isBlank(year)){
				year="0";
			}
			int finalDay = Integer.parseInt(day);
			int finalMonth = Integer.parseInt(month);
			int finalYear = Integer.parseInt(year);
			if(excelValue.contains("-")){
				finalDay = - Integer.parseInt(day);
				finalMonth = - Integer.parseInt(month);
				finalYear = - Integer.parseInt(year);
			}
			if(typeOfDate.equalsIgnoreCase("PCDate")){                
				dateValue = addDaysToSysDate(finalDay,finalMonth,finalYear);
			}else if (typeOfDate.equalsIgnoreCase("FieldName")){
				dateValue = addDaysToFieldValue("",Integer.parseInt(day),Integer.parseInt(month),Integer.parseInt(year));
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return dateValue;
	}

	/**
	 * <b>Description</b> Adds no of dates to System Date
	 * @param          noOfDay  No. of days
	 * @param          noOfMonths  No. of Months
	 * @param          noOfYear  No. of Years
	 */
	public static String addDaysToSysDate(int noOfDay, int noOfMonths, int noOfYear) {
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DATE, noOfDay);		
			calendar.add(Calendar.MONTH, noOfMonths);
			calendar.add(Calendar.YEAR, noOfYear);
			return sdf.format(calendar.getTime());
		}catch(Exception e){
			e.printStackTrace();
			return "";
		}
	}

	public  static String addDaysToFieldValue(String fieldDate,int noOfDay, int noOfMonths, int noOfYear) {
		try{			
			String [] values = fieldDate.split("/");
			String day = values[1];
			String month = values[0];
			String year = values[2];
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
			Calendar calendar = Calendar.getInstance(); 
			calendar.set(Calendar.DATE, Integer.parseInt(day));		
			calendar.set(Calendar.MONTH, Integer.parseInt(month)-1);		
			calendar.set(Calendar.YEAR, Integer.parseInt(year));		
			calendar.add(Calendar.DATE, noOfDay);		
			calendar.add(Calendar.MONTH, noOfMonths);
			calendar.add(Calendar.YEAR, noOfYear);
			return sdf.format(calendar.getTime());
		}catch(Exception e){
			e.printStackTrace();
			return "";
		}
	}
	public  static String addDaysToFieldValuewithoutseperator(String fieldDate,int noOfDay, int noOfMonths, int noOfYear) {
		try{			
			String [] values = fieldDate.split("/");
			String day = values[1];
			String month = values[0];
			String year = values[2];
			SimpleDateFormat sdf = new SimpleDateFormat("MMddyyyy");
			Calendar calendar = Calendar.getInstance(); 
			calendar.set(Calendar.DATE, Integer.parseInt(day));		
			calendar.set(Calendar.MONTH, Integer.parseInt(month)-1);		
			calendar.set(Calendar.YEAR, Integer.parseInt(year));		
			calendar.add(Calendar.DATE, noOfDay);		
			calendar.add(Calendar.MONTH, noOfMonths);
			calendar.add(Calendar.YEAR, noOfYear);
			return sdf.format(calendar.getTime());
		}catch(Exception e){
			e.printStackTrace();
			return "";
		}
	}
	public static String changeDateFormatter(String field , final String oldFormat , final String newFormat ) {
		String newDateString = null;

		SimpleDateFormat sdf = new SimpleDateFormat(oldFormat);
		Date d;
		try {
			d = sdf.parse(field);
			sdf.applyPattern(newFormat);
			newDateString = sdf.format(d);
		} catch (ParseException e) {
			e.printStackTrace();
			
		}
		return newDateString;
		
		
	}

	public static String calculateGroupDate(String excelValue,DataProvider data){
		String dateValue = StringUtils.EMPTY;
		try{
			String year = "";
			String month = "";
			String day = "";		
			String exlGroupName ="";
			String exlfieldName ="";
			try{
				String [] values  = excelValue.split("\\|");
				exlGroupName = values[0];
				String []values1 = 	values[1].split("\\+");		 
				exlfieldName = values1[0];
				if(values1[1].length() == 9){
					year = values1[1].substring(0,2);
					month = values1[1].substring(3,5);
					day = values1[1].substring(6,8);
				}else if(values1[1].length() == 6){
					String first3 = values1[1].substring(0,3);
					if(first3.contains("Y")){
						year = values1[1].substring(0,2);
						month = values1[1].substring(3,5);             		 
					}else if(first3.contains("M")){
						month = values1[1].substring(0,2);            		
						day = values1[1].substring(3,5);   
					}            	
				}else if(values1[1].length() == 3){
					if(values1[1].contains("Y")){
						year = values1[1].substring(0,2);
					}else if(values1[1].contains("M")){    	 
						month = values1[1].substring(0,2);   		
					}else if (values1[1].contains("D")){
						day =values1[1].substring(0,2);
					}		
				}
			}catch(Exception e){
				e.printStackTrace();
				dateValue = "";
			}
			if(StringUtils.isEmpty(day)){
				day="0";
			}
			if(StringUtils.isEmpty(month)){
				month = "0";
			}
			if(StringUtils.isEmpty(year)){
				year="0";
			}
			String fieldDate = null;
			fieldDate = (String) data.get(exlGroupName,exlfieldName); 
			dateValue =	addDaysToFieldValue( fieldDate, Integer.parseInt(day),Integer.parseInt(month),Integer.parseInt(year));

		}catch (Exception e){
			e.printStackTrace();
		}
		return dateValue;
	}

	/**
	 * <b>Description</b> Gets Latest current time in milli seconds
	 */
	public static long getLastsetTimeinmili(){
		Calendar cal = Calendar.getInstance();
		return cal.getTimeInMillis();
	}

	/**
	 * <b>Description</b> Gets Current Fiscal Year
	 */
	public static String getFiscalYear(int firstMonth){
		Calendar cal = Calendar.getInstance();
		int month = cal.get(Calendar.MONTH);
		int year = cal.get(Calendar.YEAR);
		int fiscalyear= (month >= firstMonth) ? year : year - 1;
		return String.valueOf(fiscalyear);
	}
	public  static String addLastDayOfMonthToFieldValue(String fieldDate,int noOfDay, int noOfMonths, int noOfYear) {
		try{			
			String [] values = fieldDate.split("/");
			String day = values[1];
			String month = values[0];
			String year = values[2];
			SimpleDateFormat sdf = new SimpleDateFormat(Config.getEnvDetails(TEST_DATA, DATE_FORMAT));
			Calendar calendar = Calendar.getInstance(); 
			calendar.set(Calendar.DATE, Integer.parseInt(day));		
			calendar.set(Calendar.MONTH, Integer.parseInt(month)-1);		
			calendar.set(Calendar.YEAR, Integer.parseInt(year));		
			calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
			return sdf.format(calendar.getTime());
		}catch(Exception e){
			e.printStackTrace();
			return "";
		}
	}
	public static String DateFormatter(String startDate) {
		DateFormat formatter = new SimpleDateFormat(Config.getEnvDetails(TEST_DATA, DATE_FORMAT));
		Date date = null;
		try {
			date = (Date) formatter.parse(startDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
//		SimpleDateFormat newFormat = new SimpleDateFormat(Config.getEnvDetails(TEST_DATA, DATE_FORMAT));
		return formatter.format(date);

	}
	/*
	 * This Method Converts DB Date Format (yyyy/mm/dd HH:MM:SS ) to Normal Date Format dd/mm/yyyy
	 * Parameters: Date in DB format (yyyy/mm/dd HH:MM:SS)
	 * Return Value: Date(dd/mm/yyyy)
	 */
	
	public static String ChangeDBDateFormatToDMY(String DB_Date) {
		
		LocalDateTime datetime = LocalDateTime.parse(DB_Date, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S"));
        String DMY_Format = datetime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		return DMY_Format;
	}
	
	/*
	 * This Method Converts DB Date Format (yyyy/mm/dd HH:MM:SS ) to Normal Date Format dd/mm/yyyy
	 * Parameters: Date in DB format (yyyy/mm/dd HH:MM:SS)
	 * Return Value: Date(dd/mm/yyyy)
	 */
	public static String ChangeDBDateFormatToMMDDYYYY(String DB_Date) {
        LocalDateTime datetime = LocalDateTime.parse(DB_Date, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S"));
        String DMY_Format = datetime.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        return DMY_Format;
    }
}
