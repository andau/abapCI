package abapci.jobs;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.preference.IPreferenceStore;
import abapci.Activator;
import abapci.manager.AUnitTestManager;
import abapci.preferences.PreferenceConstants;

public class RepeatingAUnitJob extends Job {
    private boolean running = true;
    private AUnitTestManager aUnitTestManager; 
    public RepeatingAUnitJob() {
       super("Repeating AUnit Job");
       aUnitTestManager = new AUnitTestManager(); 
    }
    
    @Override
    protected IStatus run(IProgressMonitor monitor) {
       schedule(100);
       
       IPreferenceStore prefs = Activator.getDefault().getPreferenceStore(); 
       int runningInterval = prefs.getInt(PreferenceConstants.PREF_ABAP_UNIT_RUN_INTERVAL);
       
       if (runningInterval > 0)
       {
           schedule(runningInterval * 60 * 100 );
           aUnitTestManager.executeAllPackages();
       }

       return Status.OK_STATUS;
    }
    public boolean shouldSchedule() {
       return running;
    }
    public void stop() {
       running = false;
    }
	
    
 }