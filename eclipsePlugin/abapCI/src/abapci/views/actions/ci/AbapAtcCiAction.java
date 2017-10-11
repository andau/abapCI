package abapci.views.actions.ci;

import java.util.List;
import java.util.Map;

import com.sap.adt.atc.model.atcworklist.IAtcWorklist;

import abapci.AbapCiPlugin;
import abapci.domain.AbapPackageTestState;
import abapci.domain.TestState;
import abapci.handlers.AbapAtcHandler;
import abapci.manager.DevelopmentProcessManager;
import abapci.manager.ThemeUpdateManager;
import abapci.utils.AtcResultAnalyzer;
import abapci.views.ViewModel;

public class AbapAtcCiAction extends AbstractCiAction {

	public AbapAtcCiAction(String label, String tooltip) {
		this.setText(label);
		this.setToolTipText(tooltip);
		this.setImageDescriptor(AbapCiPlugin.getImageDescriptor("icons/abapci_logo.ico"));
	}

	@Override
	public void run() {

		String firstPackage = null;
		IAtcWorklist atcWorklist = null;

		try {
			Map<String, String> packageNames = getSelectedPackages();
			firstPackage = packageNames.entrySet().iterator().next().getValue();

			atcWorklist = new AbapAtcHandler().executePackage(firstPackage);
		}

		catch (Exception ex) {
			// TODO errorhandling for exception
		}

		TestState atcTestState = AtcResultAnalyzer.getTestState(atcWorklist);
		DevelopmentProcessManager developmentProcessManager = new DevelopmentProcessManager();
		developmentProcessManager.setAtcTeststate(atcTestState);

		ThemeUpdateManager themeUpdateManager = new ThemeUpdateManager();
		themeUpdateManager.updateTheme(developmentProcessManager.getSourcecodeState());

		List<AbapPackageTestState> packageTestStates = ViewModel.INSTANCE.getPackageTestStates();

		for (AbapPackageTestState packageTestState : packageTestStates) {
			if (firstPackage == packageTestState.getPackageName()) {
				String atcOutputLabel = AtcResultAnalyzer.getOutputLabel(atcWorklist);
				packageTestState.setAtcInfo(atcOutputLabel);
			}
		}

		ViewModel.INSTANCE.updatePackageTestStates(); 

	}
}
