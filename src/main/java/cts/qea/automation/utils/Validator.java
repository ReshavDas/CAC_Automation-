/**
 * 
 */
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

import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.lang.StringEscapeUtils;

/**
 * Validator class for resolving User Story 1128102.
 * 
 * 
 */
public class Validator {

	private static final int MAX_LOOP = 1000;

	/**
	 * Escapes the input array of <code>byte</code> to prevent <b>Stored XSS</b>
	 * issue.
	 * <p/>
	 * <b>Risk</b>
	 * <p/>
	 * A successful XSS exploit would allow an attacker to rewrite web pages and
	 * insert malicious scripts which would alter the intended output.This could
	 * include HTML fragments, CSS styling rules, arbitrary JavaScript, or
	 * references to third party code. An attacker could use this to steal users'
	 * passwords, collect personal data such as credit card details, provide false
	 * information, or run malware. From the victim’s point of view, this is
	 * performed by the genuine website, and the victim would blame the site for
	 * incurred damage. An attacker could use legitimate access to the application
	 * to submit modified data to the application’s data-store. This would then be
	 * used to construct the returned web page, triggering the attack.
	 * <p/>
	 * <b>Cause</b>
	 * <p/>
	 * The application creates web pages that include untrusted data, whether from
	 * user input, the application’s database, or from other external sources. The
	 * untrusted data is embedded directly in the page's HTML, causing the browser
	 * to display it as part of the web page. If the input includes HTML fragments
	 * or JavaScript, these are displayed too, and the user cannot tell that this is
	 * not the intended page. The vulnerability is the result of directly embedding
	 * arbitrary data without first encoding it in a format that would prevent the
	 * browser from treating it like HTML or code instead of plain text. In order to
	 * exploit this vulnerability, an attacker would load the malicious payload into
	 * the data-store, typically via regular forms on other web pages. Afterwards,
	 * the application reads this data from the datastore, and embeds it within the
	 * web page as displayed for another user.
	 * <p/>
	 * <b>General Recommendations (How to avoid it)</b>
	 * <ul>
	 * <li>Fully encode all dynamic data, regardless of source, before embedding it
	 * in output.</li>
	 * <li>Encoding should be context-sensitive. For example: <br/>
	 * - HTML encoding for HTML content <br/>
	 * - HTML Attribute encoding for data output to attribute values <br/>
	 * - JavaScript encoding for server-generated JavaScript</li>
	 * <li>It is recommended to use the platform-provided encoding functionality, or
	 * known security libraries for encoding output.</li>
	 * <li>Implement a Content Security Policy (CSP) with explicit whitelists for
	 * the application's resources only.</li>
	 * <li>As an extra layer of protection, validate all untrusted data, regardless
	 * of source (note this is not a .replacement for encoding). Validation should
	 * be based on a whitelist: accept only data fitting a specified structure,
	 * rather than reject bad patterns. Check for: <br/>
	 * - Data type <br/>
	 * - Size <br/>
	 * - Range <br/>
	 * - Format <br/>
	 * - Expected values</li>
	 * <li>In the <code>Content-Type</code> HTTP response header, explicitly define
	 * character encoding (charset) for the entire page.</li>
	 * <li>Set the <code>HTTPOnly</code> flag on the session cookie for "Defense in
	 * Depth", to prevent any successful XSS exploits from stealing the cookie.</li>
	 * </ul>
	 * 
	 * @param input the input array of <code>byte</code>
	 * @return the escaped array of <code>byte</code>
	 * @see StringEscapeUtils#escapeXml(String)
	 */
	public static byte[] validateByteArrayXml(byte[] input) {
		byte[] output = null;

		String escapeXml = validateStringXml(new String(input));
		output = escapeXml.getBytes();

		return output;
	}

	/**
	 * Escapes the input <code>String</code> to prevent <b>Stored XSS</b>
	 * issue.
	 * <p/>
	 * <b>Risk</b>
	 * <p/>
	 * A successful XSS exploit would allow an attacker to rewrite web pages and
	 * insert malicious scripts which would alter the intended output.This could
	 * include HTML fragments, CSS styling rules, arbitrary JavaScript, or
	 * references to third party code. An attacker could use this to steal users'
	 * passwords, collect personal data such as credit card details, provide false
	 * information, or run malware. From the victim’s point of view, this is
	 * performed by the genuine website, and the victim would blame the site for
	 * incurred damage. An attacker could use legitimate access to the application
	 * to submit modified data to the application’s data-store. This would then be
	 * used to construct the returned web page, triggering the attack.
	 * <p/>
	 * <b>Cause</b>
	 * <p/>
	 * The application creates web pages that include untrusted data, whether from
	 * user input, the application’s database, or from other external sources. The
	 * untrusted data is embedded directly in the page's HTML, causing the browser
	 * to display it as part of the web page. If the input includes HTML fragments
	 * or JavaScript, these are displayed too, and the user cannot tell that this is
	 * not the intended page. The vulnerability is the result of directly embedding
	 * arbitrary data without first encoding it in a format that would prevent the
	 * browser from treating it like HTML or code instead of plain text. In order to
	 * exploit this vulnerability, an attacker would load the malicious payload into
	 * the data-store, typically via regular forms on other web pages. Afterwards,
	 * the application reads this data from the datastore, and embeds it within the
	 * web page as displayed for another user.
	 * <p/>
	 * <b>General Recommendations (How to avoid it)</b>
	 * <ul>
	 * <li>Fully encode all dynamic data, regardless of source, before embedding it
	 * in output.</li>
	 * <li>Encoding should be context-sensitive. For example: <br/>
	 * - HTML encoding for HTML content <br/>
	 * - HTML Attribute encoding for data output to attribute values <br/>
	 * - JavaScript encoding for server-generated JavaScript</li>
	 * <li>It is recommended to use the platform-provided encoding functionality, or
	 * known security libraries for encoding output.</li>
	 * <li>Implement a Content Security Policy (CSP) with explicit whitelists for
	 * the application's resources only.</li>
	 * <li>As an extra layer of protection, validate all untrusted data, regardless
	 * of source (note this is not a .replacement for encoding). Validation should
	 * be based on a whitelist: accept only data fitting a specified structure,
	 * rather than reject bad patterns. Check for: <br/>
	 * - Data type <br/>
	 * - Size <br/>
	 * - Range <br/>
	 * - Format <br/>
	 * - Expected values</li>
	 * <li>In the <code>Content-Type</code> HTTP response header, explicitly define
	 * character encoding (charset) for the entire page.</li>
	 * <li>Set the <code>HTTPOnly</code> flag on the session cookie for "Defense in
	 * Depth", to prevent any successful XSS exploits from stealing the cookie.</li>
	 * </ul>
	 * 
	 * @param input the <code>String</code>
	 * @return the escaped <code>String</code>
	 * @see StringEscapeUtils#escapeXml(String)
	 */
	public static String validateStringXml(String input) {
		String output = null;

		output = StringEscapeUtils.escapeXml(input);

		return output;
	}

	/**
	 * Validates input <code>listSize</code> to prevent <b>Unchecked Input for Loop
	 * Condition</b> issue.
	 * <p/>
	 * <b>Risk</b>
	 * <p/>
	 * An attacker could input a very high value, potentially causing a denial of
	 * service (DoS).
	 * <p/>
	 * <b>Cause</b>
	 * <p/>
	 * The application performs some repetitive task in a loop, and defines the
	 * number of times to perform the loop according to user input. A very high
	 * value could cause the application to get stuck in the loop and to be unable
	 * to continue to other operations.
	 * <p/>
	 * <b>General Recommendations (How to avoid it)</b>
	 * <p/>
	 * Ideally, don’t base a loop on user-provided data. If it is necessary to do
	 * so, the user input must be first validated and its range should be limited.
	 * 
	 * @param listSize the list size in <code>int</code>
	 * @return the list size
	 */
	public static int validateLoopCount(int listSize) {
		int loopCount = listSize;
		if (loopCount > MAX_LOOP) {
			loopCount = MAX_LOOP;
		}

		return loopCount;
	}

	/**
	 * Validates the file name to prevent <b>Input Path Not Canonicalized</b> issue.
	 * </p>
	 * <b>Risk</b>
	 * <p/>
	 * An attacker could define arbitrary file path for the application to use,
	 * potentially leading to: <br/>
	 * - Stealing sensitive files, such as configuration or system files <br/>
	 * - Overwriting files such as program binaries, configuration files, or system
	 * files <br/>
	 * - Deleting critical files, causing denial of service (DoS).
	 * </p>
	 * <b>Cause</b> The application uses user input in the file path for accessing
	 * files on the application server’s local disk.
	 * </p>
	 * <b>General Recommendations (How to avoid it)</b>
	 * <ul>
	 * <li>Ideally, avoid depending on dynamic data for file selection.</li>
	 * <li>Validate all input, regardless of source. Validation should be based on a
	 * whitelist: accept only data fitting a specified structure, rather than reject
	 * bad patterns. Check for: <br/>
	 * - Data type <br/>
	 * - Size <br/>
	 * - Range <br/>
	 * - Format <br/>
	 * - Expected values</li>
	 * <li>Accept dynamic data only for the filename, not for the path and
	 * folders.</li>
	 * <li>Ensure that file path is fully canonicalized.</li>
	 * <li>Explicitly limit the application to use a designated folder that is
	 * separate from the applications binary folder.</li>
	 * <li>Restrict the privileges of the application’s OS user to necessary files
	 * and folders. The application should not be able to write to the application
	 * binary folder, and should not read anything outside of the application folder
	 * and data folder.
	 * 
	 * @param filename the file name
	 * @return the validated file name
	 */
	public static String validateFileName(String filename) {
		Path p = Paths.get(filename);
		return p.getFileName().toString();
	}
}