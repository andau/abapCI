package abapci.manager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import abapci.Domain.AbapPackageTestState;
import abapci.handlers.JenkinsHandler;
import abapci.views.ViewModel;

public class JenkinsManager {

	public void executeAllPackages() 
	{
	       List<AbapPackageTestState> packageTestStates = ViewModel.INSTANCE.getPackageTestStates(); 
	       
	       	       
	       for(AbapPackageTestState packageTestState : packageTestStates)
	       {
	    	   Map<String, String> packageNames = new HashMap<String, String>(); 
	    	   packageNames.put("1", packageTestState.getPackageName()); 
	    	   
	    	   try {
				new JenkinsHandler().execute(new ExecutionEvent(null, packageNames, null, null));
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	   
	       	   String currentTime = new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime());
	       	   
	       	   packageTestState.setJenkinsState("last started: " + currentTime);
	    	   packageTestState.setLastRun(currentTime);
	    	   
	       }
	       ViewModel.INSTANCE.setPackageTestStates(packageTestStates);   
	}
}
