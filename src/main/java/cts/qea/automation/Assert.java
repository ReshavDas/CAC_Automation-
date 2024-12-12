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

import org.hamcrest.Matcher;

import cts.qea.automation.reports.Report;
import cts.qea.automation.reports.Status;

public class Assert{
	protected static WrappedWebDriver driver;
	/**
	 * A wrapper to org.junit.Assert.assertTrue with logging
	 * @param message
	 * @param condition
	 * @throws AssertionError
	 */
	public static void assertTrue(ValidationType vType,String message, boolean condition) throws AssertionError{
		try {
			
			org.junit.Assert.assertTrue(message, condition);
			report(vType, message, null);
			
		} catch (AssertionError e){
			report(vType, message, e);
		}
	}

	/**
	 * A wrapper to org.junit.Assert.assertEqual with logging
	 * @param message to be printed in report
	 * @param expected value
	 * @param actual value
	 * @throws AssertionError
	 */
	public static void assertEquals(ValidationType vType, String message, Object expected, Object actual) throws AssertionError{
		if(expected == null || expected.equals("")) expected = "BLANK";
		if(actual == null || actual.equals("")) actual = "BLANK";
		try {
			org.junit.Assert.assertEquals(message, expected, actual);
			// Skip if if(vType == ValidationType.NONE)
			if(vType == ValidationType.NONE)
				return;
			Report.log(message + ".\n" + " Expected [" + expected + "]. Actual[" + actual + "]",Status.PASS);
		}catch (AssertionError e){
			if(vType == ValidationType.NONE)
				return;
			Report.log(message + ".\n" + " Expected [" + expected + "]. Actual[" + actual + "]",Status.FAIL);
			if(vType == ValidationType.ASSERT) {
				throw e;
			}
		}
	}

	/**
	 * A wrapper to org.junit.Assert.assertFalse with logging
	 * @param message
	 * @param condition
	 * @throws AssertionError
	 * 
	 */
	public static void assertFalse(ValidationType vType,String message, boolean condition) throws AssertionError{
		try {
			org.junit.Assert.assertFalse(message, condition);
			report(vType, message, null);
		} catch (AssertionError e){
			report(vType, message, e);
		}
	}

	/**
	 * A wrapper to org.junit.Assert.assertNotNull with logging
	 * @param message
	 * @param object
	 * @throws AssertionError
	 */
	public static void assertNotNull(ValidationType vType,String message, Object object) throws AssertionError{
		try {
			org.junit.Assert.assertNotNull(message, object);
			report(vType, message, null);
		} catch (AssertionError e){
			report(vType, message, e);
		}
	}

	/**
	 * A wrapper to org.junit.Assert.assertNull with logging
	 * @param message
	 * @param object
	 * @throws AssertionError
	 */
	public static void assertNull(ValidationType vType, String message, Object object) throws AssertionError{
		try {
			org.junit.Assert.assertNull(message, object);
			report(vType, message, null);
		} catch (AssertionError e){
			report(vType, message, e);
		}
	}

	/**
	 * A wrapper to org.junit.Assert.assertSame with logging
	 * @param message
	 * @param expected
	 * @param actual
	 * @throws AssertionError
	 */
	public static void assertSame(ValidationType vType, String message, Object expected, Object actual) throws AssertionError{
		if(expected == null || expected.equals("")) expected = "BLANK";
		if(actual == null || actual.equals("")) actual = "BLANK";
		try {
			org.junit.Assert.assertSame(message, expected, actual);
			Report.log(message,Status.Pass);
		}catch (AssertionError e){
			Report.log(e.getMessage(),Status.FAIL);
			if(vType == ValidationType.ASSERT) {
				throw e;
			}
		}
	}

	/**
	 * A wrapper to org.junit.Assert.assertNotSame with logging
	 * @param message
	 * @param expected
	 * @param actual
	 * @throws AssertionError
	 */
	public static void assertNotSame(ValidationType vType, String message, Object expected, Object actual) throws AssertionError{
		if(expected == null || expected.equals("")) expected="BLANK";
		if(actual == null || actual.equals("")) actual="BLANK";
		try {
			org.junit.Assert.assertNotSame(message, expected, actual);
			Report.log(message,Status.PASS);
		} catch (AssertionError e){
			Report.log(e.getMessage(),Status.FAIL);
			if(vType == ValidationType.ASSERT) {
				throw e;
			}
		}
	}

	/**
	 * A wrapper to org.junit.Assert.assertThat with logging
	 * @param message
	 * @param actual
	 * @param matcher
	 * @throws AssertionError
	 */
	public static <T> void assertThat(ValidationType vType, String message, T actual, Matcher<T> matcher) throws AssertionError{
		try {
			// Junit version changed 4.12 to 4.13.2
			//org.junit.Assert.assertThat(message, actual, matcher);	
			org.hamcrest.MatcherAssert.assertThat(message,actual,matcher);
			Report.log(message,Status.PASS);
		} catch (AssertionError e){
			Report.log(e.getMessage(),Status.FAIL);
			if(vType == ValidationType.ASSERT) {
				throw e;
			}
		}
	}

	private static void report(ValidationType vType, String message, AssertionError exp) {
		if(exp == null) {
			switch(vType) {
			case ASSERT:
			case VERIFY:
				Report.log(message,Status.PASS);
				break;
			case Assert:
			case Verify:
				Report.log(message,Status.Pass);
				break;
			case NONE:
				break;
			}
		} else {
			switch(vType) {
			case ASSERT:
			case Assert:
				Report.log(message,Status.Fail);
				throw exp;
			case VERIFY:
			case Verify:
				Report.log(message,Status.Fail);
				break;
			case NONE:
				break;
			}
		}
	}
	
	/**
     * Fails a test with the given message.
     *
     * @param message the identifying message for the {@link AssertionError} (<code>null</code>
     * okay)
     * @see AssertionError
     */
    static public void fail(String message) {
        if (message == null) {
            throw new AssertionError();
        }
        throw new AssertionError(message);
    }

    /**
     * Fails a test with no message.
     */
    static public void fail() {
        fail(null);
    }
}

