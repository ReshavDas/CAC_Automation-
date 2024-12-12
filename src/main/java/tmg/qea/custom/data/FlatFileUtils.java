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

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;

import cts.qea.automation.reports.Report;
import cts.qea.automation.reports.Status;
import cts.qea.automation.utils.Validator;

/**
 *
 *
 */
@Deprecated
public class FlatFileUtils {

	public static void findandreplace(String filepath, String find, String replace) {
		Path path = Paths.get(filepath);
		Charset charset = StandardCharsets.UTF_8;

		String content;
		try {
			// User Story 1128099 START
			String securePath = Validator.validateFileName(path.toString());
			// User Story 1128099 END

			content = new String(Files.readAllBytes(Paths.get(securePath)), charset);
			content = content.replaceAll(find, replace);
			Files.write(path, content.getBytes(charset));
		} catch (IOException e) {
			Report.log(e);
			e.printStackTrace();
		}
	}
	
	public static CSVReader getTabedCSVReader(String filename, int skipLines) {
		CSVParser parser = new CSVParserBuilder().withSeparator('\t').withIgnoreQuotations(true).build();
		CSVReader reader = null;
		try {
			reader = new CSVReaderBuilder(new FileReader(filename))
					.withSkipLines(skipLines).withCSVParser(parser).build();
		} catch (FileNotFoundException e) {
			Report.log(e);
			e.printStackTrace();
		}
		return reader;
	}
	
	public static String[] getNextLine(CSVReader reader) throws IOException,CsvValidationException {
		return reader.readNext();
	}
	
	public static CSVReader getCSVReader(String filename, int skipLines) {
		CSVParser parser = new CSVParserBuilder().withSeparator(',').withIgnoreQuotations(true).build();
		CSVReader reader = null;
		try {
			reader = new CSVReaderBuilder(new FileReader(filename))
					.withSkipLines(skipLines).withCSVParser(parser).build();
		} catch (FileNotFoundException e) {
			Report.log(e);
			e.printStackTrace();
		}
		return reader;
	}
	public static void lastFileModified(String filepath) throws Exception {
		try {
	    File fl = new File(filepath);
	    File[] files = fl.listFiles(new FileFilter() {          
	        public boolean accept(File file) {
	            return file.isFile();
	        }
	    });
	    long lastMod = Long.MIN_VALUE;
	    File choice = null;
	    for (File file : files) {
	        if (file.lastModified() > lastMod) {
	            choice = file;
	            lastMod = file.lastModified();
	        }
	    }
	  choice.getAbsolutePath();
		}catch(Exception e) {
			Report.log(e);
			e.printStackTrace();
		}
	}
	public static void movetheFile(String oldfilepath, String newfilepath) {  //oldfilepath = filepath + filename + extension
		                                                                      //newfilepath = filepath 
		try {

			 File oldFile = new File(oldfilepath);
 
            if (oldFile.renameTo(new File(newfilepath + oldFile.getName())))
                Report.log("The file was moved successfully to the new folder", Status.Pass);
            else 
                Report.log("The File was not moved.", Status.Fail);            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
}
