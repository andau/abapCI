package abapci.views.actions.ci;

import java.util.Map;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.ui.PlatformUI;

import abapci.AbapCiPlugin;
import abapci.domain.AbapPackageInfo;
import abapci.domain.TestResultSummary;
import abapci.domain.TestState;
import abapci.handlers.AbapUnitHandler;
import abapci.result.TestResultSummaryFactory;

public class AbapUnitCiAction extends AbstractCiAction {	

	private static final String ECLIPSE_STANDARD_THEME = "org.eclipse.ui.r30";
	private static final String COM_ABAP_CI_CUSTOM_THEME = "com.abapCi.custom.theme";

	public AbapUnitCiAction(String label, String tooltip) {
		this.setText(label);
		this.setToolTipText(tooltip);
		this.setImageDescriptor(AbapCiPlugin.getImageDescriptor("icons/abapci_logo.ico"));
	}

	public void run() {
		
		//TODO errorhandling if called without valid package name 
		//TODO Handling for more than one package 
		
		String firstPackage = null; 
		TestResultSummary testResultSummary = TestResultSummaryFactory.createUndefined(); 
		
		try {
			Map<String, String> packageNames = getSelectedPackages();
            firstPackage = packageNames.entrySet().iterator().next().getValue(); 
		    testResultSummary =  (TestResultSummary) new AbapUnitHandler().execute(new ExecutionEvent(null, packageNames, null, null));
		
		}
		catch(Exception ex) 
		{
			// TODO errorhandling for exception in Jenkins call, e.g. wrong Url, username, password, ...	
		}
		
		UpdateViewerInput(new AbapPackageInfo(firstPackage), AbapCiActionEnum.ABAP_UNIT); 
		
	    String currentTheme = (testResultSummary.getTestState() == TestState.NOK) 
	    		 ?  COM_ABAP_CI_CUSTOM_THEME : ECLIPSE_STANDARD_THEME; 			 
	     
	    if (currentTheme != PlatformUI.getWorkbench().getThemeManager().getCurrentTheme().getLabel())
	    {
	    	PlatformUI.getWorkbench().getThemeManager().setCurrentTheme(currentTheme);
	    }

	}
}
