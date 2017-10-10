package abapci.manager;

import java.util.List;
import com.sap.adt.atc.model.atcworklist.IAtcWorklist;

import abapci.domain.AbapPackageTestState;
import abapci.domain.TestState;
import abapci.handlers.AbapAtcHandler;
import abapci.utils.AtcResultAnalyzer;
import abapci.views.ViewModel;

public class AtcTestManager {

	public TestState executeAllPackages() {
		TestState testState = TestState.UNDEF;

		List<AbapPackageTestState> packageTestStates = ViewModel.INSTANCE.getPackageTestStates();

		IAtcWorklist atcWorklist = null;

		for (AbapPackageTestState packageTestState : packageTestStates) {
			try {
				atcWorklist = new AbapAtcHandler().executePackage(packageTestState.getPackageName());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			String atcOutputLabel = AtcResultAnalyzer.getOutputLabel(atcWorklist);
			packageTestState.setAtcInfo(atcOutputLabel);

		}

		ViewModel.INSTANCE.setPackageTestStates(packageTestStates);

		return testState;

	}
}
