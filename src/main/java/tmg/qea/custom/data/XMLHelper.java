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

import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import cts.qea.automation.reports.Report;
import cts.qea.automation.utils.Validator;

/**
 *
 *
 */
@Deprecated
public class XMLHelper {

	private static final String SERVER_PATH = ""; //TODO Need to add valid path value. This can be set in EnvConfig file.
	private String xml;
	private DocumentBuilderFactory dbf;
	private DocumentBuilder db;
	private Document document;
	private XPathFactory xpf;
	private XPath xp;

	public XMLHelper(String filename) {
		try {
			// User Story 1128099 START
			String validPath = Validator.validateFileName(filename);
			// User Story 1128099 END

			xml = new String(Files.readAllBytes(Paths.get(SERVER_PATH + validPath)));
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			document = db.parse(new InputSource(new StringReader(xml)));
			xpf = XPathFactory.newInstance();
			xp = xpf.newXPath();
		} catch (Exception e) {
			Report.log(e);
			e.printStackTrace();
		}
	}

	public String getElementValue(String element, int position) {
		try {
			return xp.evaluate("//" + element + "[position()=" + position + "]/text()", document.getDocumentElement());
		} catch (Exception e) {
			Report.log(e);
			e.printStackTrace();
		}
		return null;
	}

}
