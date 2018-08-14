# ABAP Continuous Integration Plugin for Eclipse 

AbapCi is an Eclipse plugin which provides various Continuous Integration tools for the ABAP development. The plugin is based on the ABAP Development Tool SDK from SAP and can be installed over the [Eclipse marketplace](https://marketplace.eclipse.org/content/abap-continuous-integration). 

The only prerequisite to use this plugin for developing ABAP is an ![ABAP Development Tool (ADT) on Eclipse](https://tools.hana.ondemand.com/#abap) installation. 

## Main Features: 
1. Automatic unit test runs 
2. Different coloring for each ABAP project 
3. Automatic source code formatting 
4. Shortcut for abapGit 
5. Automatc ATC runs (experimental) 


## ad 1. Automatic unit test runs 
The  target of this feature is to automatically run the relevant unit tests after an ABAP development object is changed and activated. 
The result of the unit test run is visualised in two views:

       "ABAP Continuous Integration" for a detail view of the test run and 
       
       "ABAP CI Dashboard" for a compact summary.

The feature can be enabled and disabled in the Eclipse preferences (Window -> Preferences, section ABAP CI) by activating the checkbox "Run Unit tests after an ABAP object is activated". 

The views can be found after installation of the plugin under the menu entry Window -> Show View -> Other... in the section ABAP Continuous Integration. 

[abapCi Screenshot](https://github.com/andau/abapCI/blob/master/docu/unit_test_standard_ui.png)

Development packages can be added to the automatic unit test run by placing the mouse on a development package and selecting the menu entry "Add to CI Run". 
The CI Run executes all unit tests over an ADT functionality for all packages of the current selected ABAP development project. 

## ad 2. Different coloring for each ABAP project 
This feature tries to get the well known SAP GUI coloring for different projects into Eclipse.

A color can be assigned to a project by placing the mouse cursor on a project and selecting the menu item "Set coloring for project" or directly in the Eclipse view "ABAP Colored projects". If a color is assigned to a project, the development objects of this project are highlighted with this color. 
Currently only a part of all development objects are colored, eg.: classes, function modules, cds views. 

There are three options for the coloring which can be activated in the Eclipse preferences (Window -> Preferences, section ABAP CI).  
Change color of tab header, left ruler and/or right ruler. 

## ad 3. Automatic source code formatting 
Source code formatting is already build into ADT. To get an ABAP development object formatted, the context menu "Source Code -> Format" (or the Shortcut Shift + F1) can be used before saving and activating the objects.
If the automatic source code formatting feature for an development object is enabled, this source code formatting is done automatically when the object is saved or activated - hopefully this saves a lot of time. 

The feature can be enabled and disabled in the Eclipse preferences (Window -> Preferences, section ABAP CI) by activating the checkbox "Automatic sourcecode formatting enabled".   

To activate the feature for a specific development object in the first three lines of the source code the prefix which is set in the preferences (default value #autoformat) has be inserted. 

For example, with the default value the source code of the following class will be autoformatted when saved or activated: 

"#autoformat 
CLASS zcl_dummy_class DEFINITION
  PUBLIC
PUBLIC
  FINAL
  CREATE PUBLIC .
... 

## ad 4. Shortcut for abapGit 
This feature provides a rudimentary integration of abapGit into Eclipse. 
There is currently an project ongoing where a native integration of abapGit into Eclipse is done. [https://github.com/abapGit/ADT_Frontend](https://github.com/abapGit/ADT_Frontend). 

## ad 5. Automatic ATC runs (experimental) 
<description will be available soon>
