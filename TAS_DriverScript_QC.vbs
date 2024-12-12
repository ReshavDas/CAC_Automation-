'#######################################################################################################################
'Script Description		: Script to invoke the TAS test script in the local machine
'Test Tool/Version		: VAPI-XP
'Test Tool Settings		: N.A.
'Application Automated	: Facets
'Author					: Cognizant/360661
'Date Created			: 05/03/2020
'Date Modified          : -
'#######################################################################################################################
Option Explicit	'Forcing Variable declarations

'#######################################################################################################################
'Class Description   	: DriverScript class
'Author					: Cognizant/360661
'Date Created			: 05/03/2020
'#######################################################################################################################
Class DriverScript

	'Following variables will be set by the calling program (ALM script)
	Private m_strRelativePath   'Project base folder
	Private m_blnDebug, m_objCurrentTestSet, m_objCurrentTSTest, m_objCurrentRun	'ALM entity references
	Private m_objTestResource
	Private m_objTestResourceFolder
	Private m_objGlobalParams
	
	Private m_strTestStatus
	Private m_strReportPath  ' Generated report folder path
	
	'###################################################################################################################
	Public Property Let RelativePath(strRelativePath)
		m_strRelativePath = strRelativePath
	End Property
	'###################################################################################################################
	
	'###################################################################################################################
	Public Property Let Debug(blnDebug)
		m_blnDebug = blnDebug
	End Property
	'###################################################################################################################
	
	'###################################################################################################################
	Public Property Set CurrentTestSet(objCurrentTestSet)
		Set m_objCurrentTestSet = objCurrentTestSet
	End Property
	'###################################################################################################################
	
	'###################################################################################################################
	Public Property Set CurrentTSTest(objCurrentTSTest)
		Set m_objCurrentTSTest = objCurrentTSTest
	End Property
	'###################################################################################################################
	
	'###################################################################################################################
	Public Property Set CurrentRun(objCurrentRun)
		Set m_objCurrentRun = objCurrentRun
	End Property
	'###################################################################################################################
	
	'###################################################################################################################
	Public Property Set TestResource(tr)
		Set m_objTestResource = tr
	End Property
	'###################################################################################################################
	
	'###################################################################################################################
	Public Property Set TestResourceFolder(trFolder)
		Set m_objTestResourceFolder = trFolder
	End Property
	'###################################################################################################################
	
	'###################################################################################################################
	Public Property Set GlobalParams(gblParams)
		Set m_objGlobalParams = gblParams
	End Property
	'###################################################################################################################
	
	'###################################################################################################################
	'Function Description   : Function to drive the test execution
	'Input Parameters 		: None
	'Return Value    		: None
	'Author					: Cognizant
	'Date Created			: 11/10/2012
	'###################################################################################################################
	
   	Public Function DriveTestExecution()
	
		m_strReportPath = SetUpResultFolder()
		'AttachReportToTestRun m_strReportPath
		TDOutput.Print "Report Folders Setup completed"
		m_strTestStatus = ExecuteTest()
		
		Dim objFso: Set objFso = CreateObject("Scripting.FileSystemObject")
		AttachReportToTestRun m_strReportPath
		
		Set objFso = Nothing		
		
		If m_strTestStatus = 0 Then
			DriveTestExecution = "Passed"
		Else
			DriveTestExecution = "Failed"
		End If	
	
	End Function
	'###################################################################################################################
	Private Function SetUpResultFolder()
		Dim objFso: Set objFso = CreateObject("Scripting.FileSystemObject")
		
		Dim strResultPath
		strResultPath = m_strRelativePath & "\report" 
		
		Dim strBackupResultPath	
		strBackupResultPath = m_strRelativePath & "\reportbackup" 
		
		'Create folder if it does not exist
		If objFso.FolderExists (strResultPath) Then
			objFso.CopyFolder strResultPath, strBackupResultPath, True
			objFso.DeleteFolder strResultPath, True
			Dim ZipFile
			ZipFile = m_strRelativePath & "\result.zip" 
			If objFso.FileExists(ZipFile) Then
				objFso.DeleteFile ZipFile
			End If
		End If
		
		SetUpResultFolder = strResultPath
		
		'Release all objects
		Set objFso = Nothing
	End Function
	
	'###################################################################################################################
	Private Sub AttachReportToTestRun(strFilePath)
	
		Dim ZipFile
		ZipFile = m_strRelativePath & "\result.zip" 
		TDOutput.print ZipFile
		packZipFile strFilePath, ZipFile
		
		Dim objFso: Set objFso = CreateObject("Scripting.FileSystemObject")
		If Not objFso.FileExists(ZipFile) Then
			TDOutput.Print "The file to be attached (" & ZipFile & ") is not found!"
			m_strTestStatus = 2	'Any non-zero value is considered as a failure
			Exit Sub
		End If
		Set objFso = Nothing
		
		Dim objFoldAttachments: Set objFoldAttachments = m_objCurrentRun.Attachments
		Dim objFoldAttachment: Set objFoldAttachment = objFoldAttachments.AddItem(Null)
		objFoldAttachment.FileName = ZipFile
		objFoldAttachment.Type = 1
		objFoldAttachment.Post
		
		Set objFoldAttachment = Nothing
		Set objFoldAttachments = Nothing
	End Sub
	'###################################################################################################################

	Private Sub packZipFile(FolderName, ZipFile)
		
		TDOutput.Print FolderName
		
		Dim objShell: Set objShell = CreateObject("Shell.Application")
               
        'Open ZipFile For Output As #1
        'Print #1, Chr$(80) & Chr$(75) & Chr$(5) & Chr$(6) & String(18, 0)
        'Close #1
		
		CreateObject("Scripting.FileSystemObject").CreateTextFile(ZipFile,True).Write "PK" & Chr(5) & Chr(6) & String(18, vbNullchar)
		
		TDOutput.Print "Done1"
        
        objShell.Namespace(ZipFile).CopyHere (objShell.Namespace(FolderName).items)
        'WScript.sleep 2000
        Set objShell = Nothing
	End Sub
	'###################################################################################################################
	Private Sub AttachFolderToTestRun(strFolderPath)
		'TDOutput.Print "Processing folder " & strFolderPath
		Dim objFso: Set objFso = CreateObject("Scripting.FileSystemObject")
		If Not objFso.FolderExists(strFolderPath) Then
			TDOutput.Print "The folder to be attached (" & strFolderPath & ") is not found!"
			m_strTestStatus = 2	'Any non-zero value is considered as a failure
			Exit Sub
		End If
		
		Dim objFolder: Set objFolder = objFso.GetFolder(strFolderPath)
		Dim objFileList: Set objFileList = objFolder.Files
		'TDOutput.Print "Got the files list " '& objFileList.count
		Dim objFile
		
		For each objFile in objFileList
		'TDOutput.Print "Processing file " & objFile.Path
			AttachFileToTestRun objFile.Path
		Next
		Dim subfolder
		' For sub folder processing
		For Each subfolder in objFolder.SubFolders
		'	TDOutput.Print "Processing folder " & subfolder
			Set objFileList = subfolder.Files

			For each objFile in objFileList
		'	TDOutput.Print "Processing file " & objFile.Path
				AttachFileToTestRun objFile.Path
			Next
		Next
		'Release all objects
		Set objFile = Nothing
		Set objFileList = Nothing
		Set objFolder = Nothing
		Set objFso = Nothing
	End Sub
	'###################################################################################################################
	'###################################################################################################################
	Private Sub AttachFileToTestRun(strFilePath)
		Dim objFso: Set objFso = CreateObject("Scripting.FileSystemObject")
		If Not objFso.FileExists(strFilePath) Then
			TDOutput.Print "The file to be attached (" & strFilePath & ") is not found!"
			m_strTestStatus = 2	'Any non-zero value is considered as a failure
			Exit Sub
		End If
		Set objFso = Nothing
		
		Dim objFoldAttachments: Set objFoldAttachments = m_objCurrentRun.Attachments
		Dim objFoldAttachment: Set objFoldAttachment = objFoldAttachments.AddItem(Null)
		objFoldAttachment.FileName = strFilePath
		objFoldAttachment.Type = 1
		objFoldAttachment.Post
		
		Set objFoldAttachment = Nothing
		Set objFoldAttachments = Nothing
	End Sub
	'###################################################################################################################
	
	'###################################################################################################################

	Private Function ExecuteTest()
	Dim testExecResult
	Dim autoExecutableFile ,strScriptPath
	
		'Download Automation Executable
		TDOutput.Print "Executing TAS Test"
		strScriptPath = m_strRelativePath & "\Start.bat" 
				
		testExecResult = XTools.run(strScriptPath, "", -1, True)
		ExecuteTest = testExecResult
	End Function
	
	'###################################################################################################################
End Class
'#######################################################################################################################