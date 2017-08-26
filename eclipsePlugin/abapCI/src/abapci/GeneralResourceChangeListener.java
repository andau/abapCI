package abapci;

import org.eclipse.core.resources.*;
import org.eclipse.jface.preference.IPreferenceStore;

import abapci.manager.AUnitTestManager;
import abapci.manager.JenkinsManager;
import abapci.preferences.PreferenceConstants; 

public class GeneralResourceChangeListener implements IResourceChangeListener {
    private AUnitTestManager aUnitTestManager; 
    private JenkinsManager jenkinsManager; 
	
    public GeneralResourceChangeListener() 
    {
    	aUnitTestManager = new AUnitTestManager();
    	jenkinsManager = new JenkinsManager(); 
    }
	public void resourceChanged(IResourceChangeEvent event) {
      switch (event.getType()) {
         case IResourceChangeEvent.POST_CHANGE:
        	 IPreferenceStore prefs = Activator.getDefault().getPreferenceStore(); 
             if (prefs.getBoolean(PreferenceConstants.PREF_ABAP_UNIT_RUN_ON_SAVE)) 
             {
        	    aUnitTestManager.executeAllPackages();
             }
             if (prefs.getBoolean(PreferenceConstants.PREF_JENKINS_RUN_ON_SAVE)) 
             {
        	    jenkinsManager.executeAllPackages();
             }
             
            break; 
        }
   }
}
