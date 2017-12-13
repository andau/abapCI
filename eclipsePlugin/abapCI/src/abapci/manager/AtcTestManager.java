package abapci.manager;

import java.util.List;
import java.util.stream.Collectors;

import com.sap.adt.atc.model.atcworklist.IAtcWorklist;

import abapci.domain.AbapPackageTestState;
import abapci.domain.TestResult;
import abapci.domain.TestState;
import abapci.handlers.AbapAtcHandler;
import abapci.utils.AtcResultAnalyzer;
import abapci.views.ViewModel;

public class AtcTestManager extends AbstractTestManager {

	public AtcTestManager(List<String> packageNames) {
		super(packageNames);
	}

	public TestState executeAllPackages() {

		List<AbapPackageTestState> packageTestStates = ViewModel.INSTANCE.getPackageTestStates();

		IAtcWorklist atcWorklist = null;

		packageNames.addAll(packageTestStates.stream().filter(item -> item.getAtcInfo().equals("UNDEF"))
				.map(item -> item.getPackageName()).collect(Collectors.<String>toList()));

		for (String packageName : packageNames) {
			try {
				atcWorklist = new AbapAtcHandler().executePackage(packageName);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			TestResult testResult = AtcResultAnalyzer.getTestResult(atcWorklist);
			mergePackageTestStateIntoGlobalTestState(testResult.getTestState());

			List<AbapPackageTestState> packageTestStatesNew = packageTestStates.stream()
					.filter(item -> item.getPackageName().equals(packageName)).collect(Collectors.<AbapPackageTestState>toList());
			packageTestStatesNew.forEach(item -> item.setAtcInfo(testResult));

		}

		setAbapPackagesTestState(packageTestStates, overallTestState, TestStateType.ATC);

		return overallTestState;

	}
}
