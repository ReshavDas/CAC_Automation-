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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipHelper {
    
    List<String> filesListInDir = new ArrayList<String>();

    public static void main(String[] args) {
        File file = new File("/Users/pankaj/sitemap.xml");
        String zipFileName = "/Users/pankaj/sitemap.zip";
        
        File dir = new File("/Users/pankaj/tmp");
        String zipDirName = "/Users/pankaj/tmp.zip";
        
        zipSingleFile(file, zipFileName);
        
        ZipHelper zipFiles = new ZipHelper();
        zipFiles.zipDirectory(dir, zipDirName);
    }

    /**
     * This method zips the directory
     * @param dir
     * @param zipDirName
     */
    public void zipDirectory(File dir, String zipDirName) {
    	FileOutputStream fos = null;
        ZipOutputStream zos = null;
        try {
            populateFilesList(dir);
            //now zip files one by one create ZipOutputStream to write to the zip file
            fos = new FileOutputStream(zipDirName);
            zos = new ZipOutputStream(fos);
            for(String filePath : filesListInDir){
                System.out.println("Zipping Fine Name: "+filePath);
                //for ZipEntry we need to keep only relative file path, so we used substring on absolute path
                ZipEntry ze = new ZipEntry(filePath.substring(dir.getAbsolutePath().length() + 1, filePath.length()));
                zos.putNextEntry(ze);
                //read the file and write to ZipOutputStream
                FileInputStream fis = new FileInputStream(filePath);
                byte[] buffer = new byte[1024];
                int len;
                while ((len = fis.read(buffer)) > 0) {
                    zos.write(buffer, 0, len);
                }
                zos.closeEntry();
                fis.close();
            }
            zos.finish();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
        	try {
        		if (fos != null) {
        			fos.flush();
        			fos.close();
        		}
        		if (zos != null) {
        			zos.flush();
        			zos.close();
        		}
			} catch (IOException ex) {
				System.out.println("Exception Message: " + ex.getMessage());
			}
            
        }
    }
    
    /**
     * This method populates all the files in a directory to a List
     * @param dir
     * @throws IOException
     */
    private void populateFilesList(File dir) throws IOException {
        File[] files = dir.listFiles();
        for(File file : files){
            if(file.isFile()) filesListInDir.add(file.getAbsolutePath());
            else populateFilesList(file);
        }
    }

    /**
     * This method compresses the single file to zip format
     * @param file
     * @param zipFileName
     */
    public static void zipSingleFile(File file, String zipFileName) {
        try {
            //create ZipOutputStream to write to the zip file
            FileOutputStream fos = new FileOutputStream(zipFileName);
            ZipOutputStream zos = new ZipOutputStream(fos);
            //add a new Zip Entry to the ZipOutputStream
            ZipEntry ze = new ZipEntry(file.getName());
            zos.putNextEntry(ze);
            //read the file and write to ZipOutputStream
            FileInputStream fis = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = fis.read(buffer)) > 0) {
                zos.write(buffer, 0, len);
            }
            
            //Close the zip entry to write to zip file
            zos.closeEntry();
            //Close resources
            zos.close();
            fis.close();
            fos.close();
            System.out.println(file.getCanonicalPath()+" is zipped to "+zipFileName);
            
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        	
        }

    }

}