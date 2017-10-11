package abapci.views.actions.ci;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import abapci.AbapCiPlugin;
import abapci.domain.AbapPackageTestState;
import abapci.domain.UnitTestResultSummary;
import abapci.handlers.AbapUnitHandler;
import abapci.result.TestResultSummaryFactory;
import abapci.views.ViewModel;

public class AbapUnitCiAction extends AbstractCiAction {	

	public AbapUnitCiAction(String label, String tooltip) {
		this.setText(label);
		this.setToolTipText(tooltip);
		this.setImageDescriptor(AbapCiPlugin.getImageDescriptor("icons/abapci_logo.ico"));
	}

	public void run() {
				
		String firstPackage = null; 
		UnitTestResultSummary unitTestResultSummary = TestResultSummaryFactory.createUndefined(); 
		
		try {
			Map<String, String> packageNames = getSelectedPackages();
            firstPackage = packageNames.entrySet().iterator().next().getValue();
            
		    unitTestResultSummary =  new AbapUnitHandler().executePackage(firstPackage);
		
		}

		catch(Exception ex) 
		{
			unitTestResultSummary = TestResultSummaryFactory.createUndefined(firstPackage); 
		}
		
		List<AbapPackageTestState> packageTestStates = ViewModel.INSTANCE.getPackageTestStates();

		for (AbapPackageTestState packageTestState : packageTestStates) {
			if (firstPackage == packageTestState.getPackageName()) {
				packageTestState.setAUnitInfo(unitTestResultSummary.getTestState().toString());

				String currentTime = new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime());
				packageTestState.setLastRun(currentTime);
			}			
		}
		
		ViewModel.INSTANCE.updatePackageTestStates(); 

	}
}
