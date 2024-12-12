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

package cts.qea.automation.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ExcelFileChooser extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JFileChooser fileChooser;

	/**
	 * Launch the application.
	 */
	public static void chooseExcelFile(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				ExcelFileChooser frame = new ExcelFileChooser();
				frame.setVisible(true);
			} catch (Exception e) {
			}
		});
	}

	/**
	 * this method can be called if you want to open the file choose.
	 * 
	 * @returns If the user chooses a file the absolute path will be returned, else
	 *          null will be returned
	 */
	public ArrayList<String> openAndSelectAFile() {

		int userSelectedOption = fileChooser.showOpenDialog(this);
		ArrayList<String> filesPath = new ArrayList<>();
		if (userSelectedOption == JFileChooser.APPROVE_OPTION) {
			File[] selectedFiles = fileChooser.getSelectedFiles();
			for (File currentFile : selectedFiles) {
				filesPath.add(currentFile.getAbsolutePath());
			}
		}
		return filesPath;

	}

	/**
	 * Create the frame.
	 */
	public ExcelFileChooser() {
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		fileChooser = new JFileChooser();
		fileChooser.setMultiSelectionEnabled(true);
		fileChooser.setAcceptAllFileFilterUsed(false);
		contentPane.add(fileChooser, BorderLayout.CENTER);
		FileNameExtensionFilter fileFilter = new FileNameExtensionFilter("Excel docs (*.xlsx)", "xlsx");
		fileChooser.setFileFilter(fileFilter);
	}
}