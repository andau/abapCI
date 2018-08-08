package abapci.manager;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.core.resources.IProject;

import abapci.domain.AbapPackageTestState;
import abapci.domain.TestResult;
import abapci.domain.TestState;
import abapci.domain.UnitTestResultSummary;
import abapci.handlers.AbapUnitHandler;
import abapci.presenter.ContinuousIntegrationPresenter;

public class AUnitTestManager extends AbstractTestManager {

	public AUnitTestManager(ContinuousIntegrationPresenter continuousIntegrationPresenter, String projectName,
			List<String> packageNames) {
		super(continuousIntegrationPresenter, projectName, packageNames);
	}

	public TestState executeAllPackages(IProject project, List<AbapPackageTestState> activeAbapPackageTestStates) {

		List<AbapPackageTestState> packageTestStates = activeAbapPackageTestStates.stream()
				.filter(item -> !item.getUnitTestState().equals(TestState.DEACT))
				.collect(Collectors.<AbapPackageTestState>toList());

		UnitTestResultSummary unitTestResultSummary;

		overallTestState = TestState.UNDEF;
		for (AbapPackageTestState abapPackageTestState : activeAbapPackageTestStates) {

			if (!abapPackageTestState.getUnitTestState().equals(TestState.DEACT)
					&& packageNames.stream().anyMatch(item -> item.equals(abapPackageTestState.getPackageName()))) {
				unitTestResultSummary = new AbapUnitHandler().executePackage(project,
						abapPackageTestState.getPackageName());
				abapPackageTestState.setUnitTestResult(unitTestResultSummary.getTestResult());

				TestResult testResult = unitTestResultSummary.getTestResult();

				mergePackageTestStateIntoGlobalTestState(testResult.getTestState());

				// List<AbapPackageTestState> packageTestStatesNew = packageTestStates.stream()
				// .filter(item -> item.getPackageName().equals(packageName))
				// .collect(Collectors.<AbapPackageTestState>toList());
				// packageTestStatesNew.forEach(item -> item.setUnitTestResult(testResult));
			}

		}

		// setAbapPackagesTestState(packageTestStates, overallTestState,
		// TestStateType.UNIT);

		return overallTestState;
	}

}
