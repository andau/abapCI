package abapci.jobs;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

import abapci.Domain.AbapPackageTestState;
import abapci.Domain.TestResultSummary;
import abapci.handlers.AbapUnitHandler;
import abapci.views.ViewModel;

public class RepeatingAUnitJob extends Job {
    private boolean running = true;
    public RepeatingAUnitJob() {
       super("Repeating AUnit Job");
    }
    
    @Override
    protected IStatus run(IProgressMonitor monitor) {
       schedule(6000);
       List<AbapPackageTestState> packageTestStates = ViewModel.INSTANCE.getPackageTestStates(); 
       
       boolean allTestsOk = true; 
       
       TestResultSummary testResultSummary = new TestResultSummary("none", -1);
       
       for(AbapPackageTestState packageTestState : packageTestStates)
       {
    	   Map<String, String> packageNames = new HashMap<String, String>(); 
    	   packageNames.put("1", packageTestState.getPackageName()); 
    	   
    	   try {
			testResultSummary = (TestResultSummary) new AbapUnitHandler().execute(new ExecutionEvent(null, packageNames, null, null));
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			allTestsOk = false; 
		}
    	   if (testResultSummary.getNumErrors() > 0)
    	   {
    		   allTestsOk = false;    
    	   }; 
    	   String testResultMessage = testResultSummary.getNumErrors() == 0 ? "OK" : "NOK";
    	   
       	   String currentTime = new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime());
       	   
       	   packageTestState.setAbapState(testResultMessage);
    	   packageTestState.setLastRun(currentTime);
    	   
       }
       ViewModel.INSTANCE.setPackageTestStates(packageTestStates);
              
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
	   

       return Status.OK_STATUS;
    }
    public boolean shouldSchedule() {
       return running;
    }
    public void stop() {
       running = false;
    }
	
    
 }