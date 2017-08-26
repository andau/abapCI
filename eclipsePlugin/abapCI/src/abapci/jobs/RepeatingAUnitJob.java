package abapci.jobs;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.prefs.Preferences;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

import abapci.Activator;
import abapci.Domain.AbapPackageTestState;
import abapci.Domain.TestResultSummary;
import abapci.handlers.AbapUnitHandler;
import abapci.manager.AUnitTestManager;
import abapci.preferences.PreferenceConstants;
import abapci.views.ViewModel;

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