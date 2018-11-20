# ABAP Continuous Integration Plugin for Eclipse 

AbapCI is an Open Source Eclipse plugin which provides various Continuous Integration (CI) tools for the ABAP development with Eclipse. The plugin is based on the CI features of the ABAP Development Tool SDK from SAP (ADT) and can be installed over the [Eclipse marketplace](https://marketplace.eclipse.org/content/abap-continuous-integration). 

The main purpose of the plugin is<br> 
         * **to save development time** - by automating repetitive tasks and<br>
         * **to deliver immediate feedback**  -  for Unit tests and ATC findings.<br> 

The only prerequisite to use this plugin for developing with ABAP is an [ABAP Development Tool (ADT) on Eclipse](https://tools.hana.ondemand.com/#abap) installation. 

## Main Features: 
[1. Automatic unit test run](#1-automatic-unit-test-run)<br>
[2. Automatic ATC run](#2-automatic-atc-run)<br> 
[3. Visualisation of Source code state on UI](#3-visualisation-of-source-code-state-on-ui)<br>
[4. Automatic source code formatting](#4-automatic-source-code-formatting)<br>
[5. Shortcut for abapGit](#5-shortcut-for-abapgit)<br>
[6. Trigger Jenkins from Eclipse (experimental)](#6-trigger-jenkins-from-eclipse-experimental)<br> 
[7. Plugin development configuration](#7-plugin-development-configuration)<br>
[8. Different coloring for each ABAP project](#8-different-coloring-for-each-abap-project)<br>

## Views, Dialogs and Settings 
[View ABAP Continuous Integration](#view-abap-continuous-integration)
[View ABAP CI Dashboard](#view-abap-ci-dashboard)
[Eclipse Preferences for ABAP Continuous Integration](#eclipse-preferences-for-abap-continuous-integration) 
[Eclipse Preferences for ABAP Colored Projects](#eclipse-preferences-for-abap-colored-projects)

## 1. Automatic unit test run 
The  target of this feature is to automatically run the relevant unit tests after the activation of an ABAP development object. 
The result of the unit test run are visualised as source code state on the UI. OK (default color white) and UNIT TESTS FAIL (default color light red). As default setting this information is shown in a status bar widget. 

There are various other visualisation options and also two views where details can be displayed: 

       "ABAP Continuous Integration" for a detail view of the test run and 
       "ABAP CI Dashboard" for a compact summary.

The mentiond views can be found after installation of the plugin under the menu entry Window -> Show View -> Other... in the section ABAP Continuous Integration. 

The feature can be enabled and disabled in the Eclipse preferences (Window -> Preferences, section ABAP) by activating the corresponding checkboxes. Details can be found in the preferences section below.    

![abapCi Screenshot](https://github.com/andau/abapCI/blob/master/docu/unit_test_standard_ui.png)

Development packages can be added to the automatic unit test run by three ways:

 - by placing the mouse on a development package and selecting the menu entry "Add to CI Run"
 - manually in the Eclipse view ABAP Continuous Integration with the + icon 
 - or if a ABAP class depending to a unmanaged package is activated, a dialog for configuration is shown only once per package
 
 If the ABAP development package hierarchy is used, its currently recommended to add each subpackage (and if possible not the main package). See [Issue 5](https://github.com/andau/abapCI/issues/5)

## 2. Automatic ATC run
This feature runs automatically ATC checks for all modified and activated ABAP classes or programs. 
By enabling the option "Run ATC for each activated ABAP object" this feature is enabled. The used variant in the ATC run can be defined in the option "Run ATC with variant". 

ATC runs are generating data in the table SATC_AC_RESULTVT the  same way they are generated when the ATC checks are performed manually, thus the ABAP program SATC_AC_CLEANUP should be run in each test environment regularly (manually or by a job). This feature tries to minimize the item generation by executing the ATC checks only for the set of activated ABAP objects. 

## 3. Visualisation of Source code state on UI
In this section the indication of Unit test failures and ATC findings can be configured. The central visualisation option is a defined color for the source code state (the default colors are WHITE (source code state OK), RED (UNIT TESTS fail) and blue (ATC errors exists). 
This color can be assigned to the entire status bar or a widget on the left side of the status bar (default configuration). Further the color can be assigned to the annotation bar of the text editor of ABAP classes and is used as background color of the view ABAP Continous Integration.   

The actual source code state is also visualised with a text output (OK, UNIT TESTS FAIL or ATC ERRORS). If desired this outputs can be changed to TDD labels. This is done by activation the checkbox Test Driven Development mode (TDD mode). 

The coloring for the three states can be configured also in this section. 

Additionally to the background color of the ABAP CI dashboard also a highlighting of the Eclipse statusbar and/or a change of the full Eclipse theme can be done. But thsi works only for the Standard Theme and needs an additional plugin "Eclipse Color Theme". Beside that this feature is not recommended especially for users of the Dark theme. 

The last checkbox controls if an input dialog is shown if an object is activated which belongs to a package that is not already part of the ABAP CI run. 

## 4. Automatic source code formatting 
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

## 5. Shortcut for abapGit 
This feature provides a rudimentary integration of abapGit into Eclipse. The necessary steps to open abapGit from Eclipse (Open SAP GUI, selecting project, insert transaction code ZABAPGIT) are summarized into one menu entry, icon or shortcut (Ctrl+Shift+K)      
There is currently an project ongoing where a native integration of abapGit into Eclipse is done. [https://github.com/abapGit/ADT_Frontend](https://github.com/abapGit/ADT_Frontend). 
       
## 6. Trigger Jenkins from Eclipse (experimental)
The planned functionality of this part is to be able to trigger Jenkins jobs for related projects, for example SAP UI5 projects. The feature is working but not yet completed. 

## 7. Plugin development configuration
In these section some configurations are bundled that are useful for implementing this plugin. 

## 8. Different coloring for each ABAP project 
This feature tries to get the well known SAP GUI coloring for different system/client combinations into Eclipse.

A color can be assigned to a project (equally to a system/client combination) by selecting a project in the project explorer and pressing the menu item "Set coloring for project". Alternatively the color assignment can also be done directly in the Eclipse view "ABAP Colored projects". 

If a color is assigned to a project and the coloring is not suppressed, this color will be displayed on the Eclipse UI each time a development object of this project is active.  

There are several options for coloring which are configurable in the Eclipse preferences (Window -> Preferences, section ABAP CI).  

## Views, Dialogs and Settings 

### View ABAP Continuous Integration
In this view the configuration for the ABAP packages which are included / excluded are listed and can be modified. Additionally here the details about number of successful and failed unit tests and number of checked files with ABAP Test Cockpit (ATC) with the result are shown for each package. 

### View ABAP CI Dashboard 
The CI Dashboard is a small view with the essential information about the Source code state. The background color of this view is defined in the preferences. 
Beside the background color also the label indicate the current source code state. If desired the labels can be adaptted in the Eclipse preferences to support Test Driven Development.   

### Eclipse Preferences for ABAP Continuous Integration 
![abapCi Preferences Screenshot](https://github.com/andau/abapCI/blob/master/docu/abap_ci_preferences.png)

1. Automatic unit test runs<br>
`Run Unit tests after an ABAP object was activated` - if this checkbox is activated after each activation of an ABAP development object the unit tests  for the entire package for that the ABAP development object belongs are run<br>
`Run Unit tests only for the activated ABAP objects, not for whole package` - by activating this checkbox unit tests are only executed for the activated ABAP objects. This first feature overrules the first feature if checked.<br> 

2. Automatic ATC run<br>
If the option in this section is activated for each activated ABAP object an ATC check is performed. Currently programs and classes are supported, function modules are not supported. 
`Run ABAP Test Cockpit for activated ABAP objects` - if this checkbox is activated after each activation of an ABAP development object the ATC checks for this development object are performed.<br> 
`Run ATC with variant` - with this text field the check variant which should be used in the ATC run can be configured. If not changed the default variant is used which should be set as standard on the ABAP development system.<br>  

3. Visualisation of Source code state on UI<br>
In this part the colors and text labels which are used to indicate the current source code state can be configured. <br>
<br>
`Background color for 'OK' Sourcecode State` - this color indicates that there are no Unit test failures and no ATC errors<br>
`Background color for 'UNIT TESTS FAIL' Sourcecode State` - this color is triggered if there is at least one failing Unit Test<br>
`Background color for 'ATC ERRORS' Sourcecode State` - this color is triggered if there is at least on ATC error and not failing Unit Test<br>
<br>
`Change background color of the Eclipse statusbar` - if this checkbox is enabled the whole status bar is colored with the current Source code state color <br>
`Show widget with source code state and info in Eclipse statusbar` - this option is activated by default, it shows a widget in the status bar with information of the current source code state and as background color the above defined colors for OK, UNIT TEST FAIL, ATC ERRORS are used<br>
<br>
`Change theme layout (works only with Standard Theme and needs Eclipse Color Theme Plugin)` - if this check box is activated, the editor label is colored with RED (when there is an Unit Test error, BLUE (when there is an ATC error) or with the default color. This option only works correctly with the Standard theme and has as prerequisite the installation of the Eclipse Color Theme plugin.<br>
! Please do not use this option if you use a Dark Theme <br>
<br>
`Show TDD Labels for source code state output` - if this option is activated the labels in the ABAP Dashboard and in the status bar widget will be displayed as 'WRITE TEST', 'WRITE CODE' and 'REFACTOR'<br>
`Minimal time the TDD cycle will remain in the refactor state` - this option defines the minimum time the TDD cycle will stay in the REFACTOR phase. The REFACTOR phase will be active until there are no remaining ATC errors, but if there are no ATC errors at all this time comes into play. Thus it is ensured that the REFACTOR phase will not be skipped.<br>
<br>
The last setting `Show a dialog when a new package for the CI run is detected.` is a helper dialog to add the ABAP packages  to the CI run. Each time an object is activated which belongs to a package that is not yet configured for the CI run, this dialog is shown. The dialog is shown for each package only onces, as there are two options (activate or deactivate) in the dialog and in any case a configuration for the package will be set.<br>

4. Automatic source code formatting<br>
`Automatic sourcecode formatting enabled` - when this option is selected the ABAP development objects are automatically formatted with the Pretty Printer on each save action.<br>
`Mandatory prefix in source code to enable formatter` - not each development object is automatically formatted. To ensure that for example legacy ABAP classes are not formatted, only ABAP development objects which contains the here defined prefix in one of the first three lines of the source code are formatted with the pretty printer. The default value is #autoformat.<br> 
Do format every ABAP development object this field has to be set to the value <NO_FILTER>.<br>

5. Shortcut for abapGit<br>
This feature is currently enabled by default. The icons and menu items can currently not be suppressed.<br>
<br> 
The checkbox `Package changer for abapGit (not yet implemented on the ABAP backend)` should currently stay disabled as it leads to a error message because of the missing functionality in the ABAP backend. The only situation where it can be used at the moment is for testing purposes while developing the backend part of this feature <br>

6. Trigger Jenkins from Eclipse (experimental)<br>
The 4 settings `Jenkins BaseUrl (eg. localhost:8080)`, `Jenkins Username`, `Jenkins Password`, `Jenkins Build Token<` can be used to configure a connection to a Jenkins server.<br>
This feature is useful if you want to trigger an external package, for exampe an SAP UI project that is related to the ABAP project you are currently working on. 

7. Plugin development configuration<br>

### Eclipse Preferences for ABAP Colored Projects 
8. Different coloring for each ABAP project<br>

For coloring the source code editors there are several options. If a coloring here overlaps with the coloring for the source code state. Then the coloring for the source code state is preferred.  

Colored status bar <br>
`Add a colored widget to the status bar` - this option enables a colored widget with a label of the project name. The assigned color to the project is used as background color<br>
`Change color of entire status bar` - this option colors the whole status bar with the color assigned to the project <br>
 <br>     
Colored Editor title icon<br>
`Add a rectangle to the right bottom of the editor title icon (experimental)` - if this checkbox is checked an colored rectangle is added to the title icon. This should enable to see immediately which tab belongs to which project. Currently this is not working perfectly<br>
`Width of rectangle in percent of the icon width` - width of the rectangle - 0 ... not visible, 100 ... entire width of the icon starting from the right<br>
`Height of rectangle in percent of the icon height`- height of the rectangle - 0 ... not visible, 100 ... entire height of the icon starting from the bottom<br>
 <br>
 Colored Texteditors<br>
`Change color of the left ruler of text editors` - colors the left ruler of text editors with the assigned color to the project<br>
`Change color of the right ruler of text editors` - colors the right ruler of text editors with the assigned color to the project (available only with the standard theme)<br>
 
        
