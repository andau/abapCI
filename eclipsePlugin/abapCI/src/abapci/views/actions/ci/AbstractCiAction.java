package abapci.views.actions.ci;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.PlatformUI;

import abapci.domain.AbapPackageInfo;
import abapci.domain.AbapPackageTestState;
import abapci.views.ViewModel;

abstract class AbstractCiAction extends Action {
	private static final String NOT_YET_CALLED = "not yet called";
	
	private String lastResult = NOT_YET_CALLED; 
	

	protected void updateViewerInput(AbapPackageInfo abapPackageInfo, AbapCiActionEnum ciActionType) 
	{
		java.util.List<AbapPackageTestState> abapPackageTestStates = ViewModel.INSTANCE.getPackageTestStates(); 
		
		switch(ciActionType) 
		{
		  case JENKINS: 
			  abapPackageInfo.getJenkinsRunInfo().setExecutionResult("Jenkins executed"); 
			  break; 
		  case ABAP_UNIT: 
		      abapPackageInfo.getAbapUnitRunInfo().setExecutionResult("ABAP Unittests executed"); 			  
			  break;
		  case ABAP_ATC: 
		      abapPackageInfo.getAbapUnitRunInfo().setExecutionResult("ABAP ATC executed"); 			  
			  break;
	      default: 
	    	  throw new UnsupportedOperationException("Not yet implemented"); 
		}
				
		
		for(AbapPackageTestState abapPackageTestState : abapPackageTestStates) 
		{
			if (abapPackageTestState.getPackageName() == abapPackageInfo.getPackageName()) 
			{
				abapPackageTestState.setJenkinsInfo(abapPackageInfo.getJenkinsRunInfo().getExecutionResult()); 
				abapPackageTestState.setAUnitInfo(abapPackageInfo.getAbapUnitRunInfo().getExecutionResult()); 
				abapPackageTestState.setAtcInfo(abapPackageInfo.getAbapAtcRunInfo().getExecutionResult()); 
			}
		}

		
		ViewModel.INSTANCE.setPackageTestStates(abapPackageTestStates);

	}
	
	protected Map<String, String> getSelectedPackages() {

		ISelection selection = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.getSelection();
		AbapPackageTestState abapPackageTestState = (AbapPackageTestState)((StructuredSelection) selection).getFirstElement();

		Map<String, String> packageNames = new HashMap<>();
		packageNames.put("1", abapPackageTestState.getPackageName());

		return packageNames;
	}
	
	public String getLastResult() 
	{
		return lastResult; 
	}
}
