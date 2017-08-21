package abapci.views.actions.ci;

import java.util.Map;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.ui.PlatformUI;

import abapci.Domain.AbapPackageInfo;
import abapci.Domain.TestResultSummary;
import abapci.handlers.AbapUnitHandler;

public class AbapUnitCiAction extends AbstractCiAction {	

	public AbapUnitCiAction(String label, String tooltip) {
		this.setText(label);
		this.setToolTipText(tooltip);
	}

	public void run() {
		
		//TODO errorhandling if called without valid package name 
		//TODO Handling for more than one package 
		
		String firstPackage = null; 
		TestResultSummary testResultSummary = null; 
		
		try {
			Map<String, String> packageNames = getSelectedPackages();
            firstPackage = packageNames.entrySet().iterator().next().getValue(); 
		    testResultSummary = (TestResultSummary) new AbapUnitHandler().execute(new ExecutionEvent(null, packageNames, null, null));
		
		}
		catch(Exception ex) 
		{
			// TODO errorhandling for exception in Jenkins call, e.g. wrong Url, username, password, ...	
		}
		
		UpdateViewerInput(new AbapPackageInfo(firstPackage), AbapCiActionEnum.ABAP_UNIT); 
		
	     PlatformUI.getWorkbench().getThemeManager().getCurrentTheme(); 

	     if (testResultSummary != null && testResultSummary.getNumErrors() > 0) 
		 {
		     PlatformUI.getWorkbench().getThemeManager().setCurrentTheme("com.abapCi.custom.theme"); 			 
		 }
		 else
		 {
		     PlatformUI.getWorkbench().getThemeManager().setCurrentTheme("org.eclipse.ui.r30"); 			 
		 }	     
	 	
	}
}
