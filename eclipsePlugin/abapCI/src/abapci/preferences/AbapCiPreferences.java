package abapci.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import abapci.AbapCiPlugin;

public class AbapCiPreferences extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	final static Color HEADER_COLOR = new Color(Display.getCurrent(), 0, 0, 255);

	public AbapCiPreferences() {
		super(GRID);
	}

	public void createFieldEditors() {

		addHeaderLabelWithSpaceBefore(getFieldEditorParent(), "1. Automatic unit test runs");

		addField(new BooleanFieldEditor(PreferenceConstants.PREF_UNIT_RUN_ON_SAVE,
				"&Run Unit tests after an ABAP object is activated", getFieldEditorParent()));

		addField(new BooleanFieldEditor(PreferenceConstants.PREF_CHANGE_COLOR_ON_FAILED_TESTS,
				"&Change Theme layout on failed tests (do not use with dark theme)", getFieldEditorParent()));

		createHelperDialogsChapter();

		addField(new ColorFieldEditor(PreferenceConstants.PREF_UNIT_TEST_OK_BACKGROUND_COLOR,
				"Backgroundcolor for OK Unit Test State used in ABAP CI dashboard", getFieldEditorParent()));

		addField(new ColorFieldEditor(PreferenceConstants.PREF_UNIT_TEST_FAIL_BACKGROUND_COLOR,
				"Backgroundcolor for FAIL Unit Test State used in ABAP CI dashboard", getFieldEditorParent()));

		addHeaderLabelWithSpaceBefore(getFieldEditorParent(), "2. Different coloring for each ABAP project");
		createColorChangeChapter();

		addHeaderLabelWithSpaceBefore(getFieldEditorParent(), "3. Automatic source code formatting");
		createSourceCodeFormattingChapter();

		addHeaderLabelWithSpaceBefore(getFieldEditorParent(), "4. Shortcut for abapGit");
		createAbapGitChapter();

		addHeaderLabelWithSpaceBefore(getFieldEditorParent(), "5. Automatic ATC runs (experimental)");
		createAtcChapter();

		addHeaderLabelWithSpaceBefore(getFieldEditorParent(), "6. Trigger Jenkins from Eclipse (experimental)");
		createJenkinsChapter();

		// Unit test level selection seems currently not supported ( at least with 7.50)
		// - therefore its deactived for the moment
		// addHeaderLabelWithSpaceBefore(getFieldEditorParent(), "Abap Unit Test
		// severity and duration");
		// createAbapUnitDetailsChapter();

	}

	private void createAtcChapter() {

		Label atcInfoLabel1 = new Label(getFieldEditorParent(), SWT.NONE);
		atcInfoLabel1.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 3, 1));
		atcInfoLabel1.setText("    Can cause big data volume in table SATC_AC_RESULTVT,");

		Label atcInfoLabel2 = new Label(getFieldEditorParent(), SWT.NONE);
		atcInfoLabel2.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 3, 1));
		atcInfoLabel2.setText("    reorganize the table with program SATC_AC_CLEANUP continuously!");

		addField(new BooleanFieldEditor(PreferenceConstants.PREF_ATC_RUN_INITIAL,
				"&Run ATC after Unit Teststate changes state to OK (experimental)", getFieldEditorParent()));

		addField(new BooleanFieldEditor(PreferenceConstants.PREF_ATC_RUN_DELTA_ACTIVATED_OBJECTS,
				"&Run ATC for each activated object", getFieldEditorParent()));

		addField(new StringFieldEditor(PreferenceConstants.PREF_ATC_VARIANT, "&Run ATC with variant:",
				getFieldEditorParent()));

		addField(new ColorFieldEditor(PreferenceConstants.PREF_ATC_TEST_FAIL_BACKGROUND_COLOR,
				"Backgroundcolor for FAIL ATC State used in ABAP CI dashboard", getFieldEditorParent()));

	}

	private void addHeaderLabelWithSpaceBefore(Composite fieldEditorParent, String headerText) {
		addHeaderLabel(fieldEditorParent, "");
		addHeaderLabel(fieldEditorParent, headerText);
	}

	private void addHeaderLabel(Composite fieldEditorParent, String headerText) {
		Label emptyLabel0 = new Label(fieldEditorParent, SWT.NONE);
		emptyLabel0.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 3, 1));
		emptyLabel0.setText(headerText);
		emptyLabel0.setForeground(HEADER_COLOR);

	}

	private void createSourceCodeFormattingChapter() {

		addField(new BooleanFieldEditor(PreferenceConstants.PREF_SOURCE_CODE_FORMATTING_ENABLED,
				"&Automatic sourcecode formatting enabled", getFieldEditorParent()));
		addField(new StringFieldEditor(PreferenceConstants.PREF_SOURCE_CODE_FORMATTING_PREFIX,
				"&Mandatory prefix in source code to enable formatter", getFieldEditorParent()));
		addField(new BooleanFieldEditor(PreferenceConstants.PREF_SOURCE_CODE_CLEANUP_NOT_USED_VARIABLES,
				"&Automatically cleanup not used variabels (when formatting)", getFieldEditorParent()));

	}

	private void createColorChangeChapter() {
		addField(new BooleanFieldEditor(PreferenceConstants.PREF_COLORED_PROJECTS_TAB_HEADER_ENABLED,
				"&Change color of tab header for colored projects (do not use with dark theme)",
				getFieldEditorParent()));

		addField(new BooleanFieldEditor(PreferenceConstants.PREF_COLORED_PROJECTS_LEFT_RULER_ENABLED,
				"&Change color of left ruler for colored projects", getFieldEditorParent()));

		addField(new BooleanFieldEditor(PreferenceConstants.PREF_COLORED_PROJECTS_RIGHT_RULER_ENABLED,
				"&Change color of right ruler for colored projects", getFieldEditorParent()));
	}

	private void createJenkinsChapter() {

		addField(new BooleanFieldEditor(PreferenceConstants.PREF_JENKINS_RUN_AFTER_UNIT_TESTS_TURN_GREEN,
				"&Run Jenkins Builds when file saved", getFieldEditorParent()));

		addField(new StringFieldEditor(PreferenceConstants.PREF_JENKINS_URL, "&Jenkins BaseUrl (eg. localhost:8080):",
				getFieldEditorParent()));
		addField(new StringFieldEditor(PreferenceConstants.PREF_JENKINS_USERNAME, "&Jenkins Username:",
				getFieldEditorParent()));
		addField(new StringFieldEditor(PreferenceConstants.PREF_JENKINS_PASSWORD, "&Jenkins Password:",
				getFieldEditorParent()));
		addField(new StringFieldEditor(PreferenceConstants.PREF_JENKINS_BUILD_TOKEN, "&Jenkins Build Token:",
				getFieldEditorParent()));
	}

	private void createAbapGitChapter() {
		addField(new BooleanFieldEditor(PreferenceConstants.PREF_ABAP_GIT_PACKAGE_CHANGE_ENABLED,
				"&Package changer for abapGit (not yet implemented on the ABAP backend)", getFieldEditorParent()));

	}

	private void createHelperDialogsChapter() {

		addField(new BooleanFieldEditor(PreferenceConstants.PREF_DIALOG_NEW_PACKAGE_FOR_CI_RUN_ENABLED,
				"&Show a dialog when a new package for the CI Run is detected", getFieldEditorParent()));
	}

	@SuppressWarnings("unused")
	private void createAbapUnitDetailsChapter() {

		addField(new BooleanFieldEditor(PreferenceConstants.PREF_UNIT_RUN_CRITICAL_TESTS_ENABLED, "&run critical tests",
				getFieldEditorParent()));

		addField(new BooleanFieldEditor(PreferenceConstants.PREF_UNIT_RUN_DANGEROUS_TESTS_ENABLED,
				"&run dangerous tests", getFieldEditorParent()));

		addField(new BooleanFieldEditor(PreferenceConstants.PREF_UNIT_RUN_HARMLESS_TESTS_ENABLED, "&run harmless tests",
				getFieldEditorParent()));

		Label emptyLabelAbapUnitDetails1 = new Label(getFieldEditorParent(), SWT.NONE);
		emptyLabelAbapUnitDetails1.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 3, 1));

		addField(new BooleanFieldEditor(PreferenceConstants.PREF_UNIT_RUN_DURATION_LONG_TESTS_ENABLED,
				"&run long tests", getFieldEditorParent()));

		addField(new BooleanFieldEditor(PreferenceConstants.PREF_UNIT_RUN_DURATION_MEDIUM_TESTS_ENABLED,
				"&run medium tests", getFieldEditorParent()));

		addField(new BooleanFieldEditor(PreferenceConstants.PREF_UNIT_RUN_DURATION_SHORT_TESTS_ENABLED,
				"&run short tests", getFieldEditorParent()));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
		setPreferenceStore(AbapCiPlugin.getDefault().getPreferenceStore());
		setDescription("General settings for ABAP Continuous Integration");
	}

}