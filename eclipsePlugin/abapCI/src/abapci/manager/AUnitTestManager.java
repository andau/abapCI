package abapci.manager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import abapci.domain.AbapPackageTestState;
import abapci.domain.GlobalTestState;
import abapci.domain.UnitTestResultSummary;
import abapci.domain.TestState;
import abapci.handlers.AbapUnitHandler;
import abapci.views.ViewModel;

public class AUnitTestManager {

	TestState overallTestState;

	public TestState executeAllPackages() {
		overallTestState = null;

		List<AbapPackageTestState> packageTestStates = ViewModel.INSTANCE.getPackageTestStates();

		UnitTestResultSummary unitTestResultSummary = new UnitTestResultSummary("none", overallTestState);

		for (AbapPackageTestState packageTestState : packageTestStates) {
			Map<String, String> packageNames = new HashMap<>();
			packageNames.put("1", packageTestState.getPackageName());

			try {
				unitTestResultSummary = (UnitTestResultSummary) new AbapUnitHandler()
						.execute(new ExecutionEvent(null, packageNames, null, null));
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			TestState testStateForCurrentPackage = unitTestResultSummary.getTestState();
			mergePackageTestStateIntoGlobalTestState(testStateForCurrentPackage);

			String testResultMessage = unitTestResultSummary.getTestState().toString();
			String currentTime = new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime());

			packageTestState.setAbapState(testResultMessage);
			packageTestState.setLastRun(currentTime);

		}

		ViewModel.INSTANCE.setPackageTestStates(packageTestStates);
		GlobalTestState globalTestState = new GlobalTestState(overallTestState);

		ViewModel.INSTANCE.setGlobalTestState(globalTestState);

		return overallTestState;
	}

	private void mergePackageTestStateIntoGlobalTestState(TestState packageTestState) {
		if (overallTestState == null && packageTestState == TestState.OK) {
			overallTestState = packageTestState;
		}

		switch (packageTestState) {
		case UNDEF:
		case NOK:
			overallTestState = packageTestState;
			break;
		case OK:
			overallTestState = (overallTestState == TestState.UNDEF) ? packageTestState : overallTestState;
			break;
		}

	}
}
