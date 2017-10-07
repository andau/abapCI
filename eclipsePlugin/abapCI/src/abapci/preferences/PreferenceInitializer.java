package abapci.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import abapci.AbapCiPlugin;


public class PreferenceInitializer extends AbstractPreferenceInitializer {

	public void initializeDefaultPreferences() {
		IPreferenceStore store = AbapCiPlugin.getDefault().getPreferenceStore();
		
		store.setDefault(PreferenceConstants.PREF_ABAP_UNIT_DEV_PROJECT, ""); 
		store.setDefault(PreferenceConstants.PREF_ABAP_UNIT_RUN_INTERVAL, 0);
		store.setDefault(PreferenceConstants.PREF_ABAP_UNIT_RUN_ON_SAVE, true);
		store.setDefault(PreferenceConstants.PREF_ATC_RUN_AFTER_UNIT_TESTS_TURN_GREEN, false);
		
		
		store.setDefault(PreferenceConstants.PREF_JENKINS_URL, "<jenkins_host>:<jenkins_port>"); 
		store.setDefault(PreferenceConstants.PREF_JENKINS_USERNAME, "jenkins_username"); 
		store.setDefault(PreferenceConstants.PREF_JENKINS_PASSWORD, "jenkins_password");
		store.setDefault(PreferenceConstants.PREF_JENKINS_BUILD_TOKEN, "PUT_YOUR_OWN_TOKEN_FOR_ABAP_CI"); 
		store.setDefault(PreferenceConstants.PREF_JENKINS_RUN_AFTER_UNIT_TESTS_TURN_GREEN,  false);
		store.setDefault(PreferenceConstants.PREF_CHANGE_COLOR_ON_FAILED_TESTS,  false);
	}

}
