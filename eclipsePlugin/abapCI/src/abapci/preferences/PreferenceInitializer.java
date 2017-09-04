package abapci.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import abapci.Activator;


public class PreferenceInitializer extends AbstractPreferenceInitializer {

	public void initializeDefaultPreferences() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		
		store.setDefault(PreferenceConstants.PREF_ABAP_UNIT_URL, "<sap_host>:<sap_port>/sap/opu/odata/ZABAP_CI_EP_SRV/AbapCiSummarySet"); 
		store.setDefault(PreferenceConstants.PREF_ABAP_UNIT_USERNAME,  "sapUsername");
		store.setDefault(PreferenceConstants.PREF_ABAP_UNIT_PASSWORD,  "sapPassword");
		store.setDefault(PreferenceConstants.PREF_ABAP_UNIT_RUN_INTERVAL, 0);
		store.setDefault(PreferenceConstants.PREF_ABAP_UNIT_RUN_ON_SAVE, true);
		
		store.setDefault(PreferenceConstants.PREF_JENKINS_URL, "<jenkins_host>:<jenkins_port>"); 
		store.setDefault(PreferenceConstants.PREF_JENKINS_USERNAME, "jenkins_username"); 
		store.setDefault(PreferenceConstants.PREF_JENKINS_PASSWORD, "jenkins_password");
		store.setDefault(PreferenceConstants.PREF_JENKINS_BUILD_TOKEN, "PUT_YOUR_OWN_TOKEN_FOR_ABAP_CI"); 
		store.setDefault(PreferenceConstants.PREF_JENKINS_RUN_ON_SAVE,  false);
		store.setDefault(PreferenceConstants.PREF_CHANGE_COLOR_ON_FAILED_TESTS,  false);
	}

}
