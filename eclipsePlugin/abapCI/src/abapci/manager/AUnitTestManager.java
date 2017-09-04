package abapci.manager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

import abapci.Domain.AbapPackageTestState;
import abapci.Domain.TestResultSummary;
import abapci.handlers.AbapUnitHandler;
import abapci.views.ViewModel;

public class AUnitTestManager {

	public boolean executeAllPackages() 
	{
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
	              
		   return allTestsOk; 
		   }
}
