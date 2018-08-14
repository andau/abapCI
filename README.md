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

The views can be found after installation of the plugin under the menu entry Window -> Show View -> Other... in the section ABAP Continuous Integration. 

![abapCi Screenshot](https://github.com/andau/abapCI/blob/master/docu/unit_test_standard_ui.png)

Development packages can be added to the automatic unit test run by placing the mouse on a development package and selecting the menu entry "Add to CI Run". 
The CI Run executes all Unit tests over an ADT functionality for all packages of the current selected ABAP development project. 

## ad 2. Different coloring for each ABAP project 
This feature tries to get the well known SAP GUI coloring for different projects into Eclipse. In the Eclipse view "ABAP Colored projects" a color can be defined for each Project. The development objects are marked with the assigned color to the project they belong to. 
Currently only a part of all development objects are colored, eg.: classes, function modules, cds views. 


## ad 3. Automatic source code formatting 
<description will be available soon>

## ad 4. Shortcut for abapGit 
<description will be available soon>

## ad 5. Automatic ATC runs (experimental) 
<description will be available soon>
