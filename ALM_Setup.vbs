'#######################################################################################################################
'Script Description     : Script to setup automation suite in teh user machine
'#######################################################################################################################
Option Explicit        'Forcing Variable declarations

Dim gblParams:set gblParams = CreateObject("Scripting.Dictionary")

Sub Test_Main(Debug, CurrentTestSet, CurrentTSTest, CurrentRun)

        TDOutput.Clear
        If Debug Then
            MsgBox "This test cannot be run in debug mode! Please choose the test mode...", , "Error"
            Exit Sub
        End If
        LoadParams(CurrentTSTest)
        Dim TestSuiteName : TestSuiteName = gblParams.Item("TestSuiteName")
        Dim TestSuiteBuild : TestSuiteBuild = gblParams.Item("TestSuiteBuild")

        Dim objShell : set objShell = CreateObject("WScript.Shell")
        Dim userFolder: userFolder = objShell.ExpandEnvironmentStrings("%USERPROFILE%") &"\" & TestSuiteName
        Dim tempFolder : tempFolder = objShell.ExpandEnvironmentStrings("%TEMP%")

        Dim objFso : Set objFso = CreateObject("Scripting.FileSystemObject")
        If  Not objFso.FolderExists(userFolder) Then
             objFso.CreateFolder(userFolder)
        Else
            Dim response: response = Msgbox ("Already setup on this machine...Do you want to overwrite?",4)
            if response = 6 Then ' Yes
               objFso.DeleteFolder(userFolder)
               objFso.CreateFolder(userFolder)
            Else
               TDOutput.Print "Setup Failed!!!"
               CurrentRun.Status = "Failed"
               CurrentTSTest.Status = "Failed"
            End if
        End If

        Dim ZipFile : ZipFile = DownloadTestResource("Setup",TestSuiteBuild,tempFolder,True)
        unpackSetup tempFolder & "\" & ZipFile, userFolder


         'Handle run-time errors
        If Err.Number <> 0 Then
                TDOutput.Print "Run-time error [" & Err.Number & "] : " & Err.Description
                CurrentRun.Status = "Failed"
                CurrentTSTest.Status = "Failed"
        Else
                        MsgBox "Setup Completed"
            TDOutput.Print "Setup Completed"
        End If

        PublishCommandLineOutput CurrentRun

End Sub
'#######################################################################################################################
Function DownloadTestResource(resourceFolder,resourceName,saveTo,flag)

        Dim oFilter,oResourceList,oFile,fname,oFolderFilter,resFolder,resFolder2
        Set oFilter = TDConnection.QCResourceFactory.Filter
        Set oFolderFilter = TDConnection.QCResourceFolderFactory.Filter
        If resourceFolder <> "" Then
        oFolderFilter.Filter("RFO_NAME") = "Resources"
        Set resFolder = oFolderFilter.NewList
        If resFolder.Count < 1 then
            Msgbox "Resources folder not found in ALM"
        Else
            oFolderFilter.Clear
            oFolderFilter.Filter("RFO_NAME") = resourceFolder
            Set resFolder2 = oFolderFilter.NewList
            If resFolder2.Count < 1 then
                Msgbox resourceFolder & " folder not found in Test Resources in ALM"
            Else
                oFilter.Filter("RSC_PARENT_ID") = resFolder2.Item(1).ID
            End if
        End if
    End if

    oFilter.Filter("RSC_NAME") = resourceName

    Set oResourceList = oFilter.NewList
    If oResourceList.Count = 1 Then
        Set oFile = oResourceList.Item(1)
        fname = oFile.FileName
        Dim objFso: Set objFso = CreateObject("Scripting.FileSystemObject")
        If Not objFso.FileExists(saveTo &"/" & fname) Then
           oFile.DownloadResource saveTo, True
           TDOutput.Print "Downloaded Resource ["& fname &"]  to target folder " & saveTo
        Else
           if flag = True Then
               oFile.DownloadResource saveTo, True
               TDOutput.Print "Downloaded and Overwrote the Resource ["& fname &"]  in target folder " & saveTo
           Else
               TDOutput.Print "Resource ["& fname &"]  already exist in target folder " & saveTo & " hence not downloaded from ALM"
           End if
        End if
        Set objFso = Nothing
    End if

        DownloadTestResource =fname

End Function

Sub unpackSetup(ZipFile,ExtractTo)
    Dim objShell : set objShell = CreateObject("Shell.Application")
    Dim FilesInZip :     set FilesInZip=objShell.NameSpace(ZipFile).items
        objShell.NameSpace(ExtractTo).CopyHere(FilesInZip)
        Set objShell = Nothing
End Sub

Sub PublishCommandLineOutput(CurrentRun)
        Dim objStep: Set objStep =  CurrentRun.StepFactory.AddItem(Null)
        objStep.Name = "Execution Log"
        objStep.Status = "N/A"
        objStep.Field("ST_DESCRIPTION") = TDOutput.Text
        objStep.Post
        Set objStep = Nothing
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

