package abapci.views.actions.ci;

import java.util.Map;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.ui.PlatformUI;

import com.sap.adt.atc.model.atcworklist.IAtcWorklist;

import abapci.AbapCiPlugin;
import abapci.domain.AbapPackageInfo;
import abapci.handlers.AbapAtcHandler;

public class AbapAtcCiAction extends AbstractCiAction {	

	private static final String ECLIPSE_STANDARD_THEME = "org.eclipse.ui.r30";
	private static final String COM_ABAP_CI_CUSTOM_THEME = "com.abapCi.custom.theme";

	public AbapAtcCiAction(String label, String tooltip) {
		this.setText(label);
		this.setToolTipText(tooltip);
		this.setImageDescriptor(AbapCiPlugin.getImageDescriptor("icons/abapci_logo.ico"));
	}

	public void run() {
		
		String firstPackage = null; 
		IAtcWorklist atcWorklist;
		int numErrors = 0; 
		
		try {
			Map<String, String> packageNames = getSelectedPackages();
            firstPackage = packageNames.entrySet().iterator().next().getValue(); 
		    atcWorklist = (IAtcWorklist) new AbapAtcHandler().execute(new ExecutionEvent(null, packageNames, null, null));
		    
		    numErrors = atcWorklist.getObjects().getObject().size(); 
		}
		catch(Exception ex) 
		{
			// TODO errorhandling for exception 
		}
		
		updateViewerInput(new AbapPackageInfo(firstPackage), AbapCiActionEnum.ABAP_ATC); 
		
	    String currentTheme = (numErrors > 0) 
	    		 ?  COM_ABAP_CI_CUSTOM_THEME : ECLIPSE_STANDARD_THEME; 			 
	     
	    if (currentTheme != PlatformUI.getWorkbench().getThemeManager().getCurrentTheme().getLabel())
	    {
	    	PlatformUI.getWorkbench().getThemeManager().setCurrentTheme(currentTheme);
	    }

	}
}
