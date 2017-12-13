package abapci.views.actions.ci;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import com.sap.adt.atc.model.atcworklist.IAtcWorklist;

import abapci.AbapCiPlugin;
import abapci.domain.AbapPackageTestState;
import abapci.domain.InvalidItem;
import abapci.domain.TestResult;
import abapci.handlers.AbapAtcHandler;
import abapci.utils.AtcResultAnalyzer;
import abapci.views.ViewModel;

public class AtcCiAction extends AbstractCiAction {

	public AtcCiAction(String label, String tooltip) {
		this.setText(label);
		this.setToolTipText(tooltip);
		this.setImageDescriptor(AbapCiPlugin.getImageDescriptor("icons/atc.png"));
	}

	@Override
	public void run() {

		TestResult testResult;
		List<AbapPackageTestState> packageTestStates = ViewModel.INSTANCE.getPackageTestStates();

		for (Iterator<Entry<String, String>> iter = getSelectedPackages().entrySet().iterator(); iter.hasNext();) {

			String packageName = iter.next().getValue();

			try {
				IAtcWorklist atcWorklist = new AbapAtcHandler().executePackage(packageName);
				testResult = AtcResultAnalyzer.getTestResult(atcWorklist);

			} catch (Exception ex) {
				testResult = new TestResult(false, new ArrayList<InvalidItem>());
			}

			for (AbapPackageTestState packageTestState : packageTestStates) {
				if (packageName == packageTestState.getPackageName()) {
					packageTestState.setAtcInfo(testResult);
				}

			}
			
			ViewModel.INSTANCE.updatePackageTestStates();
		}
	}
}
