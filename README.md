# ABAP Continuous Integration Plugin for Eclipse 

AbapCi is an Eclipse plugin which provides various Continuous Integration tools for the ABAP development. The plugin is based on the ABAP Development Tool SDK from SAP (ADT) and can be installed over the [Eclipse marketplace](https://marketplace.eclipse.org/content/abap-continuous-integration) and heavily uses the already implemented code quality features of this ADT SDK. 

The purpose of the plugin is to save development time by automating repetitive tasks and to give an immediate feedback if an unit test unexpectedly fails.    

The only prerequisite to use this plugin for developing ABAP is an [ABAP Development Tool (ADT) on Eclipse](https://tools.hana.ondemand.com/#abap) installation. 

## Main Features: 
[1. Automatic unit test runs](#1-automatic-unit-test-runs)<br>
[2. Different coloring for each ABAP project](#2-different-coloring-for-each-abap-project)<br>
[3. Automatic source code formatting](#3-automatic-source-code-formatting)<br>
[4. Shortcut for abapGit](#4-shortcut-for-abapgit)<br>
[5. Automatic ATC runs (experimental)](#5-automatic-atc-runs-experimental)<br> 
[6. Trigger Jenkins from Eclipse (experimental)](#6-trigger-jenkins-from-eclipse-experimental-)<br> 

## Views, Dialogs and Settings 
[View ABAP Continuous Integration](#view-abap-Continuous-integration)
[View ABAP CI Dashboard](#view-abap-ci-dashboard)
[Eclipse Preferences for ABAP CI](#eclipse-preferences-for-abap-ci) 


## 1. Automatic unit test runs 
The  target of this feature is to automatically run the relevant unit tests after the activation of an ABAP development object. 
The result of the unit test run is visualised in two views:

       "ABAP Continuous Integration" for a detail view of the test run and 
       
       "ABAP CI Dashboard" for a compact summary.

The mentiond views can be found after installation of the plugin under the menu entry Window -> Show View -> Other... in the section ABAP Continuous Integration. 

The feature can be enabled and disabled in the Eclipse preferences (Window -> Preferences, section ABAP CI) by activating the checkbox "Run Unit tests after an ABAP object is activated". 
Additionally the checkbox "Change Theme layout on failed tests ..." can be used to indicate failed tests in the Eclipse editor with a display theme change.  

![abapCi Screenshot](https://github.com/andau/abapCI/blob/master/docu/unit_test_standard_ui.png)

Development packages can be added to the automatic unit test run by three ways:

 - by placing the mouse on a development package and selecting the menu entry "Add to CI Run"
 - manually in the Eclipse view ABAP Continuous Integration with the + icon 
 - or if a ABAP class depending to a unmanaged package is activated, a dialog for configuration is shown (only once per package)
 

## 2. Different coloring for each ABAP project 
This feature tries to get the well known SAP GUI coloring for different system/client combinations into Eclipse.

A color can be assigned to a project (equally to a system/client combination) by selecting a project in the project explorer and pressing the menu item "Set coloring for project". Alternatively the color assignment can also be done directly in the Eclipse view "ABAP Colored projects". 

If a color is assigned to a project, the development objects of this project are highlighted with this color. 
Currently only a part of all development objects are colored, eg.: classes, function modules, cds views. 

There are three options for the coloring which can be activated in the Eclipse preferences (Window -> Preferences, section ABAP CI).  
Changing the color of tab header and border (theme change), left ruler and/or right ruler. 

## 3. Automatic source code formatting 
A source code formatting for ABAP objects is already part of the ADT. To get an ABAP development object formatted, the context menu "Source Code -> Format" (or the Shortcut Shift + F1) can be used before saving and activating the objects.

If the automatic source code formatting feature for an development object is enabled, this source code formatting is done automatically when the object is saved or activated - hopefully this saves a lot of time. 

The feature generally can be enabled and disabled in the Eclipse preferences (Window -> Preferences, section ABAP CI) by activating the checkbox "Automatic sourcecode formatting enabled".   

To activate the feature for a specific development object one of the first three lines of the source code must start with the prefix which is set in the preferences (default value #autoformat). 

For example, with the default value the source code of the following class will be autoformatted each time it is saved or activated: 

```
"#autoformat 
CLASS zcl_dummy_class DEFINITION
  PUBLIC
PUBLIC
  FINAL
  CREATE PUBLIC .
... 
```

## 4. Shortcut for abapGit 
This feature provides a rudimentary integration of abapGit into Eclipse. The necessary steps to open abapGit from Eclipse (Open SAP GUI, selecting project, insert transaction code ZABAPGIT) are summarized into one menu entry, icon or shortcut (Ctrl+Shift+K)      
There is currently an project ongoing where a native integration of abapGit into Eclipse is done. [https://github.com/abapGit/ADT_Frontend](https://github.com/abapGit/ADT_Frontend). 

## 5. Automatic ATC runs (experimental) 
<description will be available soon>
       
## 6. Trigger Jenkins from Eclipse(experimental)
<description will be available soon>

## Views, Dialogs and Settings 

# View ABAP Continuous Integration 

# View ABAP CI Dashboard 

# Eclipse Preferences for ABAP CI 
![abapCi Preferences Screenshot](https://github.com/andau/abapCI/blob/master/docu/abap_ci_preferences.png)
1. Automatic unit test runs:<br>
<i>Run Unit tests after an ABAP object is activated</i><br>
<i>Change Theme layout on failed tests (do not use with dark theme)</i><br> 
<i>Show a dialog when a new package for the CI run is detected.</i><br>
<br>
2. Different coloring for each ABAP project<br>
<i>Change color of tab header for colored projects (do not use with dark theme)</i><br>
<i>Change color of left ruler for colored projects</i><br>
<i>Change color of right ruler for colored projects</i><br>
<br>
3. Automatic source code formatting<br>
<i>Automatic sourcecode formatting enabled</i></br>
<i>Mandatory prefix in source code to enable formatter</i></br>
<br>
4. Shortcut for abapGit<br>
<i>This feature is currently enabled by default. The icons and menu items can currently not be suppressed</i><br> 
<i>Package changer for abapGit (not yet implemented on the ABAP backend)</i><br>
<br>
5. Automatic ATC runs (experimental)<br>
<br>
6. Trigger Jenkins from Eclipse (experimental)</i><br>
The 4 settings <i>Jenkins BaseUrl (eg. localhost:8080), Jenkins Username, Jenkins Password, Jenkins Build Token</i> can be used to configure a connection to a Jenkins server.<br>

