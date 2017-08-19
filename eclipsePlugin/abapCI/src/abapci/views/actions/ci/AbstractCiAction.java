package abapci.views.actions.ci;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.ui.PlatformUI;
import org.eclipse.jface.action.Action;

import abapci.Domain.AbapPackageInfo;
import abapci.Domain.AbapPackageTestState;
import abapci.views.ModelProvider;

public abstract class AbstractCiAction extends Action {
	private static final String NOT_YET_CALLED = "not yet called";
	private TableViewer viewer; 
	
	String lastResult = NOT_YET_CALLED; 
	
	public AbstractCiAction(TableViewer viewer) {
		this.viewer = viewer; 
	}


	protected void UpdateViewerInput(AbapPackageInfo abapPackageInfo, AbapCiActionEnum ciActionType) 
	{
		java.util.List<AbapPackageTestState> viewerAbapPackageTestStates = ModelProvider.INSTANCE.getPersons(); 
		
		if (ciActionType == AbapCiActionEnum.JENKINS) 
		{
			abapPackageInfo.getJenkinsRunInfo().setExecutionResult("Jenkins executed"); 
		}
		else 
		{
			abapPackageInfo.getAbapUnitRunInfo().setExecutionResult("ABAP Unittests executed"); 
		}
				
		
		for(AbapPackageTestState abapPackageTestState : viewerAbapPackageTestStates) 
		{
			if (abapPackageTestState.getPackageName() == abapPackageInfo.getPackageName()) 
			{
				abapPackageTestState.setJenkinsState(abapPackageInfo.getJenkinsRunInfo().getExecutionResult()); 
				abapPackageTestState.setAbapState(abapPackageInfo.getAbapUnitRunInfo().getExecutionResult()); 
			}
		}

		
		viewer.setInput(viewerAbapPackageTestStates);

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
