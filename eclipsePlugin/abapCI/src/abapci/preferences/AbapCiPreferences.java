package abapci.preferences;

import org.eclipse.jface.preference.*;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbench;
import abapci.Activator;

/**
 * This class represents a preference page that is contributed to the
 * Preferences dialog. By subclassing <samp>FieldEditorPreferencePage</samp>, we
 * can use the field support built into JFace that allows us to create a page
 * that is small and knows how to save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They are stored in the
 * preference store that belongs to the main plug-in class. That way,
 * preferences can be accessed directly via the preference store.
 */

public class AbapCiPreferences extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public AbapCiPreferences() {
		super(GRID);
	}

	/**
	 * Creates the field editors. Field editors are abstractions of the common
	 * GUI blocks needed to manipulate various types of preferences. Each field
	 * editor knows how to save and restore itself.
	 */
	public void createFieldEditors() {

		
		//addField(new BooleanFieldEditor(PreferenceConstants.PREF_ABAP_UNIT_INTEGRATE, "&Use ABAP Unittest:",
		//		getFieldEditorParent()));
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

		//addField(new BooleanFieldEditor(PreferenceConstants.PREF_JENKINS_INTEGRATE, "&Use Jenkins",
		//		getFieldEditorParent()));
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