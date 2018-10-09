# ABAP Continuous Integration Plugin for Eclipse 

AbapCI is an Open Source Eclipse plugin which provides various Continuous Integration (CI) tools for the ABAP development with Eclipse. The plugin is based on the CI features of the ABAP Development Tool SDK from SAP (ADT) and can be installed over the [Eclipse marketplace](https://marketplace.eclipse.org/content/abap-continuous-integration). 

The main purposes of the plugin are<br>
         * **to save development time** - by automating repetitive tasks and<br>
         * **to deliver immediate feedback**  -  for Unit tests and ATC findings.<br> 

The only prerequisite to use this plugin for developing ABAP is an [ABAP Development Tool (ADT) on Eclipse](https://tools.hana.ondemand.com/#abap) installation. 

## Main Features: 
[1. Automatic unit test runs](#1-automatic-unit-test-runs)<br>
[2. Automatic ATC runs (experimental)](#2-automatic-atc-runs-experimental)<br> 
[3. Visualisation of Source code state on UI](#3-visualisation-of-source-code-state-on-ui)<br>
[4. Different coloring for each ABAP project](#4-different-coloring-for-each-abap-project)<br>
[5. Automatic source code formatting](#5-automatic-source-code-formatting)<br>
[6. Shortcut for abapGit](#6-shortcut-for-abapgit)<br>
[7. Trigger Jenkins from Eclipse (experimental)](#7-trigger-jenkins-from-eclipse-experimental-)<br> 

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
 
 If the ABAP development package hierarchy is used, its currently recommended to add each subpackage (and if possible not the main package). See [Issue 5](https://github.com/andau/abapCI/issues/5)

## 2. Automatic ATC runs 
This feature runs automatically ATC checks for all modified and activated ABAP objects. 
By enabling the option "Run ATC for each activated ABAP object" this feature is enabled. The used variant in the ATC run can be defined in the option "Run ATC with variant". 

ATC runs are generating data in the table SATC_AC_RESULTVT, thus the ABAP program SATC_AC_CLEANUP should be run in each test environment regularly (manually or by a job). This feature tries to minimize the item generation by executing the ATC checks only for the set of activated ABAP objects. 

## 3. Visualisation of Source code state on UI
In this section the indication of Unit test failures and ATC findings can be configured. 
The options are: 

Test Driven Development mode (TDD mode): If this checkbox is activated in the ABAP CI dashboard the TDD phases (WRITE TEST, WRITE CODE, REFACTOR) will be shown instead of the standard labels OK, TEST FAILS, ATC FINDINGS. In both cases the colors of the three phases can be configured with the next three preferences. 
Additionally to the background color of the ABAP CI dashboard also a highlighting of the Eclipse statusbar and/or a change of the full Eclipse theme can be activated. The latter is not recommended especially for users of the Dark theme. 

The last checkbox controls if an input dialog is shown if an object is activated which belongs to a package that is not already part of the ABAP CI run. 

## 4. Different coloring for each ABAP project 
This feature tries to get the well known SAP GUI coloring for different system/client combinations into Eclipse.

A color can be assigned to a project (equally to a system/client combination) by selecting a project in the project explorer and pressing the menu item "Set coloring for project". Alternatively the color assignment can also be done directly in the Eclipse view "ABAP Colored projects". 

If a color is assigned to a project, the development objects of this project are highlighted with this color. 
Currently only a part of all development objects are colored, eg.: classes, function modules, cds views. 

There are three options for the coloring which can be activated in the Eclipse preferences (Window -> Preferences, section ABAP CI).  
Changing the color of tab header and border (theme change), left ruler and/or right ruler. 

## 5. Automatic source code formatting 
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
If every ABAP development object (which is edited in text mode) should be autoformatted regardless any prefixes in the comments, the special filter value '<NO_FILTER>' can be used. 

## 6. Shortcut for abapGit 
This feature provides a rudimentary integration of abapGit into Eclipse. The necessary steps to open abapGit from Eclipse (Open SAP GUI, selecting project, insert transaction code ZABAPGIT) are summarized into one menu entry, icon or shortcut (Ctrl+Shift+K)      
There is currently an project ongoing where a native integration of abapGit into Eclipse is done. [https://github.com/abapGit/ADT_Frontend](https://github.com/abapGit/ADT_Frontend). 
       
## 7. Trigger Jenkins from Eclipse(experimental)
<description will be available soon>

## Views, Dialogs and Settings 

### View ABAP Continuous Integration
Explaination of this view to be followed 

### View ABAP CI Dashboard 
Explaination of this view to be followed 

### Eclipse Preferences for ABAP CI 
![abapCi Preferences Screenshot](https://github.com/andau/abapCI/blob/master/docu/abap_ci_preferences.png)

1. Automatic unit test runs:<br>
With the first checkbox `Run Unit tests after an ABAP object is activated` the feature can be activated.<br>
The second setting `Change Theme layout on failed tests (do not use with dark theme)` can be used to trigger a change of the Eclipse editor color settings to indicate failed tests.<br> 
The last setting `Show a dialog when a new package for the CI run is detected.` is a helper function to add the ABAP packages  to the CI run. Each time an object is activated which belongs to a package not yet configured for the CI run, this dialog is shown. The dialog is shown for each package only onces, as there are two options (activate or deactivate) in the dialog and in any case a configuration for the package will be set.   

2. Different coloring for each ABAP project<br>
For coloring the source code editors there are three options.  
        `Change color of tab header for colored projects (do not use with dark theme)`<br>
        `Change color of left ruler for colored projects`<br>
        `Change color of right ruler for colored projects`<br>

3. Automatic source code formatting<br>
The feature can be enabled or disabled with the first checkbox in this section `Automatic sourcecode formatting enabled`</br>
The configuration setting `Mandatory prefix in source code to enable formatter`contains the prefix with the starting string that one of the first three lines of the source code has to match to trigger the automatic source code formatting. The default value is #autoformat.

4. Shortcut for abapGit<br>
This feature is currently enabled by default. The icons and menu items can currently not be suppressed<br> 
The checkbox `Package changer for abapGit (not yet implemented on the ABAP backend)` should currently stay disabled as it leads to a error message because of the missing functionality in the ABAP backend. The only situation where it can be used at the moment is for testing purposes while developing the backend part of this feature <br>

5. Automatic ATC runs (experimental)<br>

6. Trigger Jenkins from Eclipse (experimental)</i><br>
The 4 settings `Jenkins BaseUrl (eg. localhost:8080)`, `Jenkins Username`, `Jenkins Password`, `Jenkins Build Token<` can be used to configure a connection to a Jenkins server.<br>

