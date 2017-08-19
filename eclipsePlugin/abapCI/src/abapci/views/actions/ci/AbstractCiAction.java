package abapci.views.actions.ci;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.ui.PlatformUI;
import org.eclipse.jface.action.Action;

import abapci.Domain.AbapPackageInfo;
import abapci.Domain.RunInfo;
import abapci.handlers.JenkinsHandler;

public abstract class AbstractCiAction extends Action {
	private static final String NOT_YET_CALLED = "not yet called";
	private TableViewer viewer; 
	
	String lastResult = NOT_YET_CALLED; 
	
	public AbstractCiAction(TableViewer viewer) {
		this.viewer = viewer; 
	}


	protected void UpdateViewerInput(AbapPackageInfo abapPackageInfo, AbapCiActionEnum ciActionType) 
	{
		String[] currentPackages = (String[]) viewer.getInput();
		
		if (ciActionType == AbapCiActionEnum.JENKINS) 
		{
			abapPackageInfo.getJenkinsRunInfo().setExecutionResult("Jenkins executed"); 
		}
		else 
		{
			abapPackageInfo.getAbapUnitRunInfo().setExecutionResult("ABAP Unittests executed"); 
		}
				
		
		for(int i = 0; i < currentPackages.length; i++) 
		{
			if (currentPackages[i] == abapPackageInfo.getPackageName()) 
			{
				currentPackages[i] = abapPackageInfo.getPackageName() + " " + abapPackageInfo.getPackageRunInfos(); 
			}
		}
		
		viewer.setInput(currentPackages);

	}
	
	protected Map<String, String> getSelectedPackages() {

		ISelection selection = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.getSelection();
		String packageName = ((StructuredSelection) selection).getFirstElement().toString();

		Map<String, String> packageNames = new HashMap<String, String>();
		packageNames.put("1", packageName);

		return packageNames;
	}
	
	public String getLastResult() 
	{
		return lastResult; 
	}
}
