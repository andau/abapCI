package abapci.manager;

import java.util.List;
import java.util.stream.Collectors;

import abapci.domain.AbapPackageTestState;
import abapci.domain.TestResult;
import abapci.domain.TestState;
import abapci.domain.UnitTestResultSummary;
import abapci.handlers.AbapUnitHandler;
import abapci.views.ViewModel;

public class AUnitTestManager extends AbstractTestManager {

	public AUnitTestManager(List<String> packageNames) {
		super(packageNames);
	}

	public TestState executeAllPackages() {

		List<AbapPackageTestState> packageTestStates = ViewModel.INSTANCE.getPackageTestStates();

		UnitTestResultSummary unitTestResultSummary;

		packageNames.addAll(packageTestStates.stream().filter(item -> item.getAUnitInfo().equals("UNDEF"))
				.map(item -> item.getPackageName()).collect(Collectors.<String>toList()));

		overallTestState = TestState.UNDEF; 
		for (String packageName : packageNames) {

			unitTestResultSummary = new AbapUnitHandler().executePackage(packageName);

			TestResult testResult = unitTestResultSummary.getTestResult();

			mergePackageTestStateIntoGlobalTestState(testResult.getTestState());

			List<AbapPackageTestState> packageTestStatesNew = packageTestStates.stream()
					.filter(item -> item.getPackageName().equals(packageName)).collect(Collectors.<AbapPackageTestState>toList());
			packageTestStatesNew.forEach(item -> item.setAUnitInfo(testResult));

		}

		setAbapPackagesTestState(packageTestStates, overallTestState, TestStateType.UNIT);

		return overallTestState;
	}

}
