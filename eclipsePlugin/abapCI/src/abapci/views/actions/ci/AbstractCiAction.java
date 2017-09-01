package abapci.views.actions.ci;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.PlatformUI;

import abapci.Domain.AbapPackageInfo;
import abapci.Domain.AbapPackageTestState;
import abapci.views.ViewModel;

public abstract class AbstractCiAction extends Action {
	private static final String NOT_YET_CALLED = "not yet called";
	
	String lastResult = NOT_YET_CALLED; 
	

	protected void UpdateViewerInput(AbapPackageInfo abapPackageInfo, AbapCiActionEnum ciActionType) 
	{
		java.util.List<AbapPackageTestState> abapPackageTestStates = ViewModel.INSTANCE.getPackageTestStates(); 
		
		if (ciActionType == AbapCiActionEnum.JENKINS) 
		{
			abapPackageInfo.getJenkinsRunInfo().setExecutionResult("Jenkins executed"); 
		}
		else 
		{
			abapPackageInfo.getAbapUnitRunInfo().setExecutionResult("ABAP Unittests executed"); 
		}
				
		
		for(AbapPackageTestState abapPackageTestState : abapPackageTestStates) 
		{
			if (abapPackageTestState.getPackageName() == abapPackageInfo.getPackageName()) 
			{
				abapPackageTestState.setJenkinsState(abapPackageInfo.getJenkinsRunInfo().getExecutionResult()); 
				abapPackageTestState.setAbapState(abapPackageInfo.getAbapUnitRunInfo().getExecutionResult()); 
			}
		}

		
		ViewModel.INSTANCE.setPackageTestStates(abapPackageTestStates);

	}
	
	protected Map<String, String> getSelectedPackages() {

		ISelection selection = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.getSelection();
		AbapPackageTestState abapPackageTestState = (AbapPackageTestState)((StructuredSelection) selection).getFirstElement();

		Map<String, String> packageNames = new HashMap<String, String>();
		packageNames.put("1", abapPackageTestState.getPackageName());

		return packageNames;
	}
	
	public String getLastResult() 
	{
		return lastResult; 
	}
}
