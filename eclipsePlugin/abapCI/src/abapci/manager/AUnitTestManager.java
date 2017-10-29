package abapci.manager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import abapci.domain.AbapPackageTestState;
import abapci.domain.UnitTestResultSummary;
import abapci.domain.TestState;
import abapci.handlers.AbapUnitHandler;
import abapci.views.ViewModel;

public class AUnitTestManager extends AbstractTestManager {


	public TestState executeAllPackages() {
		overallTestState = null;

		List<AbapPackageTestState> packageTestStates = ViewModel.INSTANCE.getPackageTestStates();

		UnitTestResultSummary unitTestResultSummary;

		for (AbapPackageTestState packageTestState : packageTestStates) {
			
	        unitTestResultSummary = new AbapUnitHandler().executePackage(packageTestState.getPackageName());
			TestState testStateForCurrentPackage = unitTestResultSummary.getTestState();
			mergePackageTestStateIntoGlobalTestState(testStateForCurrentPackage);

			String testResultMessage = unitTestResultSummary.getTestState().toString();
			packageTestState.setAUnitInfo(testResultMessage);

			String currentTime = new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime());
			packageTestState.setLastRun(currentTime);
		}

		setAbapPackagesTestState(packageTestStates, overallTestState, TestStateType.UNIT);

		return overallTestState;
	}

}
