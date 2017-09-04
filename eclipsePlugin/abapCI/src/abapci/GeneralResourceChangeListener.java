package abapci;

import org.eclipse.core.resources.*;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

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
            	 boolean allUnitTestsOk = aUnitTestManager.executeAllPackages();
            	 if (prefs.getBoolean(PreferenceConstants.PREF_CHANGE_COLOR_ON_FAILED_TESTS))
            	 {
            		 updateTheme(allUnitTestsOk); 
            	 }
        	    
             }
             if (prefs.getBoolean(PreferenceConstants.PREF_JENKINS_RUN_ON_SAVE)) 
             {
        	    jenkinsManager.executeAllPackages();
             }
             
            break; 
        }
   }
	
   private void updateTheme(boolean allTestsOk)
   {
	   if (allTestsOk) 
	   {
		   Display.getDefault().asyncExec(new Runnable() {
			    public void run() {
			 	   PlatformUI.getWorkbench().getThemeManager().setCurrentTheme("org.eclipse.ui.r30"); 			 
			    }
			});		   
	   }
	   else 
	   {
		   Display.getDefault().asyncExec(new Runnable() {
			    public void run() {
			 	   PlatformUI.getWorkbench().getThemeManager().setCurrentTheme("com.abapCi.custom.theme"); 			 
			    }
			});
	   }  	 
   }
}
