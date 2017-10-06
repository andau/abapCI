package abapci.feature;

import org.eclipse.jface.preference.IPreferenceStore;

import abapci.AbapCiPlugin;
import abapci.preferences.PreferenceConstants;

public class FeatureDecision 
{
	
	private static IPreferenceStore prefs; 
	
    public FeatureDecision() 
    {
    	if (FeatureDecision.prefs == null) 
    		FeatureDecision.prefs = AbapCiPlugin.getDefault().getPreferenceStore();     		
    }

    public FeatureDecision(IPreferenceStore prefs) 
    {
    	FeatureDecision.prefs = prefs;     		
    }
    
	public boolean runUnitTestsOnSave() 
    {
       return prefs.getBoolean(PreferenceConstants.PREF_ABAP_UNIT_RUN_ON_SAVE);	
    }

	public boolean changeColorOnFailedTests() {
		return prefs.getBoolean(PreferenceConstants.PREF_CHANGE_COLOR_ON_FAILED_TESTS);
	}

	public boolean runAtcAfterUnitTestTurnOk() {
		return prefs.getBoolean(PreferenceConstants.PREF_ATC_RUN_AFTER_UNIT_TESTS_TURN_GREEN);
	}

	public boolean runJenkinsAfterUnitTestTurnOk() {
		return prefs.getBoolean(PreferenceConstants.PREF_JENKINS_RUN_AFTER_UNIT_TESTS_TURN_GREEN);
	}
	
}
