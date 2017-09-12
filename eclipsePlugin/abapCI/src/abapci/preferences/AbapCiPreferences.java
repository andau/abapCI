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

import abapci.Activator;

public class AbapCiPreferences extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public AbapCiPreferences() {
		super(GRID);
	}

	public void createFieldEditors() {

		Label emptyLabel0 = new Label(getFieldEditorParent(), SWT.NONE);
		emptyLabel0.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 3, 1));
		
		addField(new StringFieldEditor(PreferenceConstants.PREF_ABAP_UNIT_URL, "&ABAP Unit test Url:",
				getFieldEditorParent()));
		addField(new StringFieldEditor(PreferenceConstants.PREF_ABAP_UNIT_USERNAME, "&ABAP Unit test Username:",
				getFieldEditorParent()));
		addField(new StringFieldEditor(PreferenceConstants.PREF_ABAP_UNIT_PASSWORD, "&ABAP Unit test Password:",
				getFieldEditorParent()));

		addField(new IntegerFieldEditor(PreferenceConstants.PREF_ABAP_UNIT_RUN_INTERVAL, "&ABAP Unit test interval (minutes):",
				getFieldEditorParent()));

		addField(new BooleanFieldEditor(PreferenceConstants.PREF_ABAP_UNIT_RUN_ON_SAVE, "&Run ABAP Unit tests when file saved",
				getFieldEditorParent()));

		Label emptyLabel1 = new Label(getFieldEditorParent(), SWT.NONE);
		emptyLabel1.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 3, 1));
		
		addField(new StringFieldEditor(PreferenceConstants.PREF_JENKINS_URL, "&Jenkins BaseUrl (eg. localhost:8080):",
				getFieldEditorParent()));
		addField(new StringFieldEditor(PreferenceConstants.PREF_JENKINS_USERNAME, "&Jenkins Username:",
				getFieldEditorParent()));
		addField(new StringFieldEditor(PreferenceConstants.PREF_JENKINS_PASSWORD, "&Jenkins Password:",
				getFieldEditorParent()));
		addField(new StringFieldEditor(PreferenceConstants.PREF_JENKINS_BUILD_TOKEN, "&Jenkins Build Token:",
				getFieldEditorParent()));
		addField(new BooleanFieldEditor(PreferenceConstants.PREF_JENKINS_RUN_ON_SAVE, "&Run Jenkins Builds when file saved",
				getFieldEditorParent()));

		Label emptyLabel2 = new Label(getFieldEditorParent(), SWT.NONE);
		emptyLabel2.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 3, 1));

		
		addField(new BooleanFieldEditor(PreferenceConstants.PREF_CHANGE_COLOR_ON_FAILED_TESTS, "&Change Theme layout on failed tests",
				getFieldEditorParent()));

		Label emptyLabel3 = new Label(getFieldEditorParent(), SWT.NONE);
		emptyLabel3.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 3, 1));
		
		addField(new BooleanFieldEditor(PreferenceConstants.PREF_ABAP_UNIT_SIMULATE, "&Simulate ABAP Unit Endpoint",
				getFieldEditorParent()));
		

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("General settings for ABAP Continuous Integration");
	}

}