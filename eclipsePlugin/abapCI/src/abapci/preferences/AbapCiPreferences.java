package abapci.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import abapci.AbapCiPlugin;

public class AbapCiPreferences extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	final static Color HEADER_COLOR = new Color(Display.getCurrent(), 0, 0, 255);
	PreferencesUiHelper preferencesUiHelper;

	public AbapCiPreferences() {
		super(GRID);
		preferencesUiHelper = new PreferencesUiHelper();
	}

	@Override
	public void createFieldEditors() {

		preferencesUiHelper.addHeaderLabelWithSpaceBefore(getFieldEditorParent(), "1. Automatic Unit test run");
		createUnitTestChapter();

		preferencesUiHelper.addHeaderLabelWithSpaceBefore(getFieldEditorParent(),
				"2. Automatic ATC run (! Reorganize table SATC_AC_RESULTVT continously !)");
		createAtcChapter();

		preferencesUiHelper.addHeaderLabelWithSpaceBefore(getFieldEditorParent(),
				"3. Visualisation of SourceCode State on UI");
		createSourceCodeVisualisationChapter();

		preferencesUiHelper.addHeaderLabelWithSpaceBefore(getFieldEditorParent(),
				"4. Automatic source code formatting");
		createSourceCodeFormattingChapter();

		preferencesUiHelper.addHeaderLabelWithSpaceBefore(getFieldEditorParent(), "5. Shortcut for abapGit");
		createAbapGitChapter();

		preferencesUiHelper.addHeaderLabelWithSpaceBefore(getFieldEditorParent(),
				"6. Trigger Jenkins from Eclipse (experimental)");
		createJenkinsChapter();

		preferencesUiHelper.addHeaderLabelWithSpaceBefore(getFieldEditorParent(),
				"7. Plugin development configuration");
		createDeveloperChapter();

		// Unit test level selection seems currently not supported ( at least with 7.50)
		// - therefore its deactived for the moment
		// addHeaderLabelWithSpaceBefore(getFieldEditorParent(), "Abap Unit Test
		// severity and duration");
		// createAbapUnitDetailsChapter();

	}

	private void createUnitTestChapter() {
		addField(new BooleanFieldEditor(PreferenceConstants.PREF_UNIT_RUN_ON_SAVE,
				"Run Unit tests after an ABAP object was activated", getFieldEditorParent()));

		addField(new BooleanFieldEditor(PreferenceConstants.PREF_UNIT_RUN_ACTIVATED_OBJECTS_ONLY,
				"Run Unit tests only for the activated ABAP objects, not for whole package", getFieldEditorParent()));
	}

	private void createAtcChapter() {

		addField(new BooleanFieldEditor(PreferenceConstants.PREF_ATC_RUN_ACTIVATED_OBJECTS_ONLY,
				"Run ABAP Test Cockpit for activated ABAP objects", getFieldEditorParent()));

		addField(new StringFieldEditor(PreferenceConstants.PREF_ATC_VARIANT, "Run ATC with variant:",
				getFieldEditorParent()));

	}

	private void createSourceCodeVisualisationChapter() {

		addField(new ColorFieldEditor(PreferenceConstants.PREF_UNIT_TEST_OK_BACKGROUND_COLOR,
				"Background color for 'OK' Sourcecode State", getFieldEditorParent()));

		addField(new ColorFieldEditor(PreferenceConstants.PREF_UNIT_TEST_FAIL_BACKGROUND_COLOR,
				"Background color for 'UNIT TESTS FAIL' Sourcecode State", getFieldEditorParent()));

		addField(new ColorFieldEditor(PreferenceConstants.PREF_ATC_TEST_FAIL_BACKGROUND_COLOR,
				"Background color for 'ATC ERRORS' Sourcecode State", getFieldEditorParent()));

		addField(new BooleanFieldEditor(PreferenceConstants.PREF_CHANGE_STATUS_BAR_BACKGROUND_COLOR,
				"Change background color of the Eclipse statusbar", getFieldEditorParent()));

		addField(new BooleanFieldEditor(PreferenceConstants.PREF_VISUALISATION_STATUS_BAR_WIDGET_ENABLED,
				"Show widget with source code state and info in Eclipse statusbar", getFieldEditorParent()));

		addField(new BooleanFieldEditor(PreferenceConstants.PREF_VISUALISATION_STATUS_CHANGE_THEME_ENABLED,
				"Change theme layout (works only with Standard Theme and needs Eclipse Color Theme Plugin)",
				getFieldEditorParent()));

		preferencesUiHelper.addEmptyLabel(getFieldEditorParent());

		addField(new BooleanFieldEditor(PreferenceConstants.PREF_TDD_MODE,
				"Show TDD Labels for source code state output", getFieldEditorParent()));

		addField(new IntegerFieldEditor(PreferenceConstants.PREF_TDD_MIN_REQUIRED_SECONDS,
				"Minimal time the TDD cycle will remain in the refactor state", getFieldEditorParent()));

		preferencesUiHelper.addEmptyLabel(getFieldEditorParent());

		createHelperDialogsChapter();

	}

	private void createSourceCodeFormattingChapter() {

		addField(new BooleanFieldEditor(PreferenceConstants.PREF_SOURCE_CODE_FORMATTING_ENABLED,
				"Automatic sourcecode formatting enabled", getFieldEditorParent()));
		addField(new StringFieldEditor(PreferenceConstants.PREF_SOURCE_CODE_FORMATTING_PREFIX,
				"Mandatory prefix in source code to enable formatter", getFieldEditorParent()));
		addField(new BooleanFieldEditor(PreferenceConstants.PREF_SOURCE_CODE_CLEANUP_NOT_USED_VARIABLES,
				"Automatically cleanup not used variabels (when formatting enabled)", getFieldEditorParent()));

	}

	private void createJenkinsChapter() {

		addField(new BooleanFieldEditor(PreferenceConstants.PREF_JENKINS_RUN_AFTER_UNIT_TESTS_TURN_GREEN,
				"Run Jenkins Builds when file saved", getFieldEditorParent()));

		addField(new StringFieldEditor(PreferenceConstants.PREF_JENKINS_URL, "Jenkins BaseUrl (eg. localhost:8080):",
				getFieldEditorParent()));
		addField(new StringFieldEditor(PreferenceConstants.PREF_JENKINS_USERNAME, "Jenkins Username:",
				getFieldEditorParent()));
		addField(new StringFieldEditor(PreferenceConstants.PREF_JENKINS_PASSWORD, "Jenkins Password:",
				getFieldEditorParent()));
		addField(new StringFieldEditor(PreferenceConstants.PREF_JENKINS_BUILD_TOKEN, "Jenkins Build Token:",
				getFieldEditorParent()));
	}

	private void createAbapGitChapter() {
		addField(new BooleanFieldEditor(PreferenceConstants.PREF_ABAP_GIT_PACKAGE_CHANGE_ENABLED,
				"Package changer for abapGit (not yet implemented on the ABAP backend)", getFieldEditorParent()));

	}

	private void createHelperDialogsChapter() {

		addField(new BooleanFieldEditor(PreferenceConstants.PREF_DIALOG_NEW_PACKAGE_FOR_CI_RUN_ENABLED,
				"Show a dialog when a not configured package for the CI Run is detected", getFieldEditorParent()));
	}

	private void createDeveloperChapter() {
		addField(new BooleanFieldEditor(PreferenceConstants.PREF_DEVELOPER_JAVA_SIMU_MODE_ENABLED,
				"Activate Java Simulation mode", getFieldEditorParent()));

		addField(new BooleanFieldEditor(PreferenceConstants.PREF_DEVELOPER_TRACING_ENABLED, "Enable tracing",
				getFieldEditorParent()));

		preferencesUiHelper.addEmptyLabel(getFieldEditorParent());

		final Label warningLabel1 = new Label(getFieldEditorParent(), SWT.NONE);
		warningLabel1.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 3, 1));
		warningLabel1.setText("Do not activate without the necessary changes in ADT.");

		final Label warningLabel2 = new Label(getFieldEditorParent(), SWT.NONE);
		warningLabel2.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 3, 1));
		warningLabel2.setText("See Unit test AtcLaunchShortcutTest for further description");

		addField(new BooleanFieldEditor(PreferenceConstants.PREF_ATC_ANNOTATION_HANDLING_ENABLED,
				"Dynamic editor annotations for ATC findings (experimental)", getFieldEditorParent()));

	}

	@Override
	public void init(IWorkbench workbench) {
		setPreferenceStore(AbapCiPlugin.getDefault().getPreferenceStore());
		setDescription("General settings for ABAP Continuous Integration");
	}

}