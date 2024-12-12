'#######################################################################################################################
'Script Description     : Script to invoke the corresponding test script in the local machine
'Test Tool/Version      : VAPI-XP
'Test Tool Settings     : N.A.
'Author                 : Cognizant
'Date Created           : 26/12/2012
'Date Modified          : 23/11/2018  For EH customization
'#######################################################################################################################
Option Explicit        'Forcing Variable declarations

Dim gblParams:set gblParams = CreateObject("Scripting.Dictionary")

'#######################################################################################################################
'Function Description   : Main function which is the entry point of execution
'Input Parameters       : Debug, CurrentTestSet, CurrentTSTest, CurrentRun
'Return Value           : None
'Author                 : Cognizant
'Date Created           : 26/12/2012
'#######################################################################################################################
Sub Test_Main(Debug, CurrentTestSet, CurrentTSTest, CurrentRun)
        TDOutput.Clear

        If Debug Then
            MsgBox "This test cannot be run in debug mode! Please choose the test mode...", , "Error"
            Exit Sub
        End If
        LoadParams(CurrentTSTest)
        Dim TestSuiteName : TestSuiteName = gblParams.Item("TestSuiteName")
        Dim DriverScriptName : DriverScriptName = gblParams.Item("DriverScriptName")
        Dim TestType : TestType = gblParams.Item("TestType")
        Dim ExecutionEnvironment: ExecutionEnvironment = gblParams.Item("ExecutionEnvironment")
        Dim TestEnvironment : TestEnvironment = gblParams.Item("TestEnvironment")

        Dim objShell : set objShell = CreateObject("WScript.Shell")
        Dim userProfile : userProfile = objShell.ExpandEnvironmentStrings("%USERPROFILE%")
        Dim userDomain : userDomain = objShell.ExpandEnvironmentStrings("%USERDOMAIN%")

        Dim strRelativePath : strRelativePath =  userProfile & "\" & TestSuiteName
        Dim driverFile : driverFile = strRelativePath & "\" & DriverScriptName
        Dim objCurrentTestSet: Set objCurrentTestSet = TDConnection.TestSetFactory.Item(CurrentTestSet.ID)
        Dim objFso :  Set objFso = CreateObject("Scripting.FileSystemObject")

        CurrentRun.Status = "Not Completed"
        CurrentTSTest.Status = "Not Completed"

        If ExecutionEnvironment <> "" Then
                If (userDomain <> "EMBLEM" And  ExecutionEnvironment="EMBLEM") Then
                    TDOutput.Print "This script/cycle should be executed in EMBLEM VDI Environment"
                    CurrentRun.Status = "Failed"
                    CurrentTSTest.Status = "Failed"
                    PublishCommandLineOutput CurrentRun
                    Exit Sub
                End If
                If (userDomain <> "SERVICES" And  ExecutionEnvironment="HOC2") Then
                    TDOutput.Print "This script/cycle should be executed in Trizetto HOC2 Environment"
                    CurrentRun.Status = "Failed"
                    CurrentTSTest.Status = "Failed"
                    PublishCommandLineOutput CurrentRun
                    Exit Sub
                End If
        End If

        If Not objFso.FileExists(driverFile) Then
           TDOutput.Print "Driver script not found " & driverFile
           CurrentRun.Status = "Failed"
           CurrentTSTest.Status = "Failed"
           PublishCommandLineOutput CurrentRun
           Exit Sub
        End If

        'Associate required libraries
        Dim objMyFile: Set objMyFile =objFso.OpenTextFile(driverFile, 1) ' 1 - For Reading
        Execute objMyFile.ReadAll()
        Set objMyFile = Nothing
        Set objFso = Nothing

        Dim objDriverScript: Set objDriverScript = New DriverScript
        objDriverScript.RelativePath = strRelativePath
        objDriverScript.Debug = Debug
        Set objDriverScript.CurrentTestSet = CurrentTestSet
        Set objDriverScript.CurrentTSTest = CurrentTSTest
        Set objDriverScript.CurrentRun = CurrentRun
        Set objDriverScript.TestResource = TDConnection.QCResourceFactory
        Set objDriverScript.TestResourceFolder =   TDConnection.QCResourceFolderFactory
        Set objDriverScript.GlobalParams = gblParams

      '  On Error Resume Next
        Dim strTestStatus
        strTestStatus = objDriverScript.DriveTestExecution()
        CurrentRun.Status = strTestStatus
        CurrentTSTest.Status = strTestStatus

        'Handle run-time errors
        If Err.Number <> 0 Then
                TDOutput.Print "Run-time error [" & Err.Number & "] : " & Err.Description
                CurrentRun.Status = "Failed"
                CurrentTSTest.Status = "Failed"
        End If

        PublishCommandLineOutput CurrentRun
End Sub
'#######################################################################################################################

'#######################################################################################################################
'Function Description   : Function to Load the ALM Parameters in to Global Dictionary Object
'Input Parameters       : CurrentRun
'Return Value           : None
'Author                 : Cognizant
'Date Created           : 03/08/2018
'#######################################################################################################################

Sub LoadParams(CurrentTSTest)
    Dim lstParams: Set lstParams = CurrentTSTest.ParameterValueFactory.NewList("")
    Dim objParam
    For Each objParam in lstParams
        'TDOutput.Print objParam.Name  & "->" & RemoveHTMLStrip(objParam.ActualValue)
        gblParams.add objParam.Name, RemoveHTMLStrip(objParam.ActualValue)
    Next

    Set objParam = Nothing
    Set lstParams = Nothing
End Sub
'#######################################################################################################################

'#######################################################################################################################
'Function Description   : Function to remove the HTML tags in the Param values
'Input Parameters       : CurrentRun
'Return Value           : None
'Author                 : Cognizant
'Date Created           : 08/08/2018
'#######################################################################################################################

Function RemoveHTMLStrip(gobjsetHTMLParamValues)
        'Strips the HTML tags from gobjsetHTMLParamValues
         Dim objRegExp,strHTMLParameterValues
         Set objRegExp = New Regexp

         objRegExp.IgnoreCase = True
         objRegExp.Global = True
         objRegExp.Pattern = "<(.|\n)+?>"

         'Replace all HTML tag matches with the empty string
         strHTMLParameterValues = objRegExp.Replace(gobjsetHTMLParamValues, "")
                'Replace all < and > with &lt; and &gt;
         strHTMLParameterValues = Replace(strHTMLParameterValues, "<", "&lt;")
         strHTMLParameterValues = Replace(strHTMLParameterValues, ">", "&gt;")
         RemoveHTMLStrip = strHTMLParameterValues    'Return the value of gstrHTMLParameterValues
         Dim regEx
             Set regEx = New RegExp
                 regEx.Global = true
         regEx.IgnoreCase = True
         regEx.Pattern = "\s{2,}"
         RemoveHTMLStrip = Trim(regEx.Replace(RemoveHTMLStrip, " "))

                  Set objRegExp = Nothing
                  Set regEx = Nothing
                End Function


'#######################################################################################################################
'Function Description   : Function to publish the command line output into the test run results
'Input Parameters       : CurrentRun
'Return Value           : None
'Author                 : Cognizant
'Date Created           : 26/12/2012
'#######################################################################################################################
Sub PublishCommandLineOutput(CurrentRun)
        Dim objStep: Set objStep =  CurrentRun.StepFactory.AddItem(Null)
        objStep.Name = "Execution Log"
        objStep.Status = "N/A"
        objStep.Field("ST_DESCRIPTION") = TDOutput.Text
        objStep.Post
        Set objStep = Nothing
End Sub
'#######################################################################################################################