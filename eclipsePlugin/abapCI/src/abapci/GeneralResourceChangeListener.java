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
    private boolean abapUnitRunOnSave; 
    private boolean changeColorOnFailedTests; 
    private boolean jenkinsRunOnSave; 
	
    public GeneralResourceChangeListener() 
    {
    	aUnitTestManager = new AUnitTestManager();
    	jenkinsManager = new JenkinsManager();
   	    
    	IPreferenceStore prefs = Activator.getDefault().getPreferenceStore(); 
    	
    	abapUnitRunOnSave  = prefs.getBoolean(PreferenceConstants.PREF_ABAP_UNIT_RUN_ON_SAVE); 
    	changeColorOnFailedTests = prefs.getBoolean(PreferenceConstants.PREF_CHANGE_COLOR_ON_FAILED_TESTS); 
    	jenkinsRunOnSave = prefs.getBoolean(PreferenceConstants.PREF_JENKINS_RUN_ON_SAVE);
    }
    
	public void resourceChanged(IResourceChangeEvent event) {
		switch (event.getType()) {
		
         case IResourceChangeEvent.POST_CHANGE:
          	IResourceDelta delta = event.getDelta(); 
         	if (delta == null) return; 

         	IResourceDelta[] resourceDeltas = delta.getAffectedChildren(
    				IResourceDelta.CHANGED |
    				IResourceDelta.ADDED |
    				IResourceDelta.REMOVED);
         	
         	if (resourceDeltas.length > 0)
         	{
         		runTestsForAllResources();
         	    //TODO add possibility to run tests only for affected packages - resourceDeltas[i]....
         	}
         
            break;
         default: 
        	 return; 
		}
   }
	
   private void runTestsForAllResources() {
	   
		if (abapUnitRunOnSave) 
		{
			boolean allUnitTestsOk = aUnitTestManager.executeAllPackages();
		  	if (changeColorOnFailedTests)
		  	{
		  		updateTheme(allUnitTestsOk); 
		  	}            		
		}
	        
	   if (jenkinsRunOnSave) 
	   {
	    jenkinsManager.executeAllPackages();
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
