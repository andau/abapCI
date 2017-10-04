package abapci.feature;

import org.eclipse.jface.preference.IPreferenceStore;

import abapci.AbapCiPlugin;
import abapci.preferences.PreferenceConstants;

public class FeatureDecision 
{
	
	private IPreferenceStore prefs; 
	
    public FeatureDecision() 
    {
    	this.prefs = AbapCiPlugin.getDefault().getPreferenceStore();     		
    }

    public FeatureDecision(IPreferenceStore prefs) 
    {
    	this.prefs = prefs;     		
    }
    
	public boolean runUnitTestsOnSaveFeatureEnabled() 
    {
       return prefs.getBoolean(PreferenceConstants.PREF_ABAP_UNIT_RUN_ON_SAVE);	
    }

	public boolean changeColorOnFailedTestsFeatureEnabled() {
		return prefs.getBoolean(PreferenceConstants.PREF_CHANGE_COLOR_ON_FAILED_TESTS);
	}

	public boolean runAtcOnSaveFeatureEnabled() {
		return prefs.getBoolean(PreferenceConstants.PREF_ATC_RUN_AFTER_UNIT_TESTS);
	}

	public boolean runJenkinsOnSaveFeatureEnabled() {
		return prefs.getBoolean(PreferenceConstants.PREF_JENKINS_RUN_ON_SAVE);
	}
}
