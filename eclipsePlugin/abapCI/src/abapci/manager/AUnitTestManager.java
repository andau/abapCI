package abapci.manager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import com.sap.adt.atc.model.atcworklist.IAtcWorklist;

import abapci.domain.AbapPackageTestState;
import abapci.domain.GlobalTestState;
import abapci.domain.TestResultSummary;
import abapci.domain.TestState;
import abapci.handlers.AbapAtcHandler;
import abapci.handlers.AbapUnitHandler;
import abapci.views.ViewModel;

public class AUnitTestManager {

	public boolean executeAllPackages() 
	{
	       List<AbapPackageTestState> packageTestStates = ViewModel.INSTANCE.getPackageTestStates(); 
	       
	       TestState overallTestState = TestState.UNDEF; 
	       TestResultSummary testResultSummary = new TestResultSummary("none", overallTestState);
       
	       for(AbapPackageTestState packageTestState : packageTestStates)
	       {
	    	   Map<String, String> packageNames = new HashMap<String, String>(); 
	    	   packageNames.put("1", packageTestState.getPackageName()); 
	    	   
	    	   try {
	    		   testResultSummary = (TestResultSummary) new AbapUnitHandler().execute(new ExecutionEvent(null, packageNames, null, null));
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	   overallTestState = testResultSummary.getTestState(); 
	    	   
	    	   String testResultMessage = testResultSummary.getTestState().toString();
	       	   String currentTime = new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime());
	       	   
	       	   packageTestState.setAbapState(testResultMessage);
	    	   packageTestState.setLastRun(currentTime);
	    	   
	       }
	       
	       ViewModel.INSTANCE.setPackageTestStates(packageTestStates);
	       GlobalTestState globalTestState = new GlobalTestState(overallTestState);  
	       
	       ViewModel.INSTANCE.setGlobalTestState(globalTestState); 
	              
		   return overallTestState == TestState.OK;  
	}
}
	
