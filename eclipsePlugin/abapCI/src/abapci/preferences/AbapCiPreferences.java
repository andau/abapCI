package abapci.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import abapci.AbapCiPlugin;

public class AbapCiPreferences extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public AbapCiPreferences() {
		super(GRID);
	}

	public void createFieldEditors() {

		Label emptyLabel0 = new Label(getFieldEditorParent(), SWT.NONE);
		emptyLabel0.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 3, 1));
		
		addField(new BooleanFieldEditor(PreferenceConstants.PREF_UNIT_RUN_ON_SAVE, "&Run ABAP Unit tests when object activated",
				getFieldEditorParent()));
		addField(new StringFieldEditor(PreferenceConstants.PREF_DEV_PROJECT, "&ABAP Development Project:",
				getFieldEditorParent()));

		addField(new IntegerFieldEditor(PreferenceConstants.PREF_UNIT_RUN_INTERVAL, "&ABAP Unit test interval (minutes):",
				getFieldEditorParent()));
		
		addField(new BooleanFieldEditor(PreferenceConstants.PREF_ATC_RUN_AFTER_UNIT_TESTS_TURN_GREEN, "&Run ATC when after successful Unit testrun",
				getFieldEditorParent()));

		addField(new StringFieldEditor(PreferenceConstants.PREF_ATC_VARIANT, "&Run ATC with variant:",
				getFieldEditorParent()));

		createSourceCodeFormattingChapter(); 
		
        createColorChangeChapter(); 
		
        createJenkinsChapter(); 
        		
	}


	private void createSourceCodeFormattingChapter() {
		Label emptyLabelSourceCode = new Label(getFieldEditorParent(), SWT.NONE);
		emptyLabelSourceCode.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 3, 1));

		
		addField(new BooleanFieldEditor(PreferenceConstants.PREF_SOURCE_CODE_FORMATTING_ENABLED, "&Automatic sourcecode formatting enabled",
				getFieldEditorParent()));
		addField(new StringFieldEditor(PreferenceConstants.PREF_SOURCE_CODE_FORMATTING_PREFIX, "&Mandatory prefix in source code to enable formatter",
				getFieldEditorParent()));
		
	}

	private void createColorChangeChapter() {
		Label emptyLabel2 = new Label(getFieldEditorParent(), SWT.NONE);
		emptyLabel2.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 3, 1));

		
		addField(new BooleanFieldEditor(PreferenceConstants.PREF_CHANGE_COLOR_ON_FAILED_TESTS, "&Change Theme layout on failed tests",
				getFieldEditorParent()));

		Label emptyLabel3 = new Label(getFieldEditorParent(), SWT.NONE);
		emptyLabel3.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 3, 1));
		
		Label emptyLabel4 = new Label(getFieldEditorParent(), SWT.NONE);
		emptyLabel4.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 3, 1));

		addField(new BooleanFieldEditor(PreferenceConstants.PREF_COLORED_PROJECTS_TAB_HEADER_ENABLED, "&Change color of tab header for colored projects",
				getFieldEditorParent()));

		addField(new BooleanFieldEditor(PreferenceConstants.PREF_COLORED_PROJECTS_LEFT_RULER_ENABLED, "&Change color of left ruler for colored projects",
				getFieldEditorParent()));

		addField(new BooleanFieldEditor(PreferenceConstants.PREF_COLORED_PROJECTS_RIGHT_RULER_ENABLED, "&Change color of right ruler for colored projects",
				getFieldEditorParent()));		
	}

	private void createJenkinsChapter() {
		Label emptyLabel1 = new Label(getFieldEditorParent(), SWT.NONE);
		emptyLabel1.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 3, 1));
		
		addField(new BooleanFieldEditor(PreferenceConstants.PREF_JENKINS_RUN_AFTER_UNIT_TESTS_TURN_GREEN, "&Run Jenkins Builds when file saved",
				getFieldEditorParent()));

		addField(new StringFieldEditor(PreferenceConstants.PREF_JENKINS_URL, "&Jenkins BaseUrl (eg. localhost:8080):",
				getFieldEditorParent()));
		addField(new StringFieldEditor(PreferenceConstants.PREF_JENKINS_USERNAME, "&Jenkins Username:",
				getFieldEditorParent()));
		addField(new StringFieldEditor(PreferenceConstants.PREF_JENKINS_PASSWORD, "&Jenkins Password:",
				getFieldEditorParent()));
		addField(new StringFieldEditor(PreferenceConstants.PREF_JENKINS_BUILD_TOKEN, "&Jenkins Build Token:",
				getFieldEditorParent()));
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
		setPreferenceStore(AbapCiPlugin.getDefault().getPreferenceStore());
		setDescription("General settings for ABAP Continuous Integration");
	}

}