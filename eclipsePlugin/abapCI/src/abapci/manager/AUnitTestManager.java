package abapci.manager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import abapci.domain.AbapPackageTestState;
import abapci.domain.UnitTestResultSummary;
import abapci.domain.TestState;
import abapci.handlers.AbapUnitHandler;
import abapci.views.ViewModel;

public class AUnitTestManager extends AbstractTestManager {

	public AUnitTestManager(List<String> packageNames) {
		super(packageNames);
	}

	public TestState executeAllPackages() {

		List<AbapPackageTestState> packageTestStates = ViewModel.INSTANCE.getPackageTestStates();

		UnitTestResultSummary unitTestResultSummary;

		packageNames.addAll(packageTestStates.stream().filter(item -> item.getAtcInfo().equals("UNDEF"))
				.map(item -> item.getPackageName()).collect(Collectors.<String>toList()));

		for (String packageName : packageNames) {

			unitTestResultSummary = new AbapUnitHandler().executePackage(packageName);

			String testResultMessage = unitTestResultSummary.getTestState().toString();
			List<AbapPackageTestState> packageTestStatesNew = packageTestStates.stream()
					.filter(item -> item.getPackageName().equals(packageName))
					.collect(Collectors.<AbapPackageTestState>toList());
			packageTestStatesNew.forEach(item -> item.setAUnitInfo(testResultMessage));

			String currentTime = new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime());
			packageTestStatesNew.forEach(item -> item.setLastRun(currentTime));
		}

		calculateOverallTestState(packageTestStates, TestStateType.UNIT);
		setAbapPackagesTestState(packageTestStates, overallTestState, TestStateType.UNIT);

		return overallTestState;
	}

}
