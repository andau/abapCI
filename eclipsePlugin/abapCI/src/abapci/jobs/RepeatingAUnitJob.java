package abapci.jobs;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

import abapci.AbapCiPlugin;
import abapci.manager.AUnitTestManager;
import abapci.manager.JenkinsManager;
import abapci.preferences.PreferenceConstants;

public class RepeatingAUnitJob extends Job {
    
	//TODO Make thread save 
	public static boolean ScheduleNextJob; 
	
	private boolean running = true;
    private AUnitTestManager aUnitTestManager;
    private JenkinsManager jenkinsManager; 
    
    private boolean abapUnitRunOnSave;
    private boolean changeColorOnFailedTests;
    private boolean jenkinsRunOnSave; 

    public RepeatingAUnitJob() {
       
       super("Repeating AUnit Job");
       aUnitTestManager = new AUnitTestManager();
       jenkinsManager = new JenkinsManager(); 

       IPreferenceStore prefs = AbapCiPlugin.getDefault().getPreferenceStore(); 
       abapUnitRunOnSave = prefs.getBoolean(PreferenceConstants.PREF_ABAP_UNIT_RUN_ON_SAVE);
       changeColorOnFailedTests = prefs.getBoolean(PreferenceConstants.PREF_CHANGE_COLOR_ON_FAILED_TESTS);
       jenkinsRunOnSave = prefs.getBoolean(PreferenceConstants.PREF_JENKINS_RUN_ON_SAVE);
    }
    
    @Override
    protected IStatus run(IProgressMonitor monitor) {       
       schedule(1000);  	
       if (ScheduleNextJob)
       {
    	   ScheduleNextJob = false; 
    	   runTestsForAllResources();
       }
       
       return Status.OK_STATUS;
    }
    
    public boolean shouldSchedule() {
       return running;
    }
    
    public void stop() {
       running = false;
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