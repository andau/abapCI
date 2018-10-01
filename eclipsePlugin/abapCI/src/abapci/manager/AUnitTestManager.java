package abapci.manager;

import java.util.List;

import org.eclipse.core.resources.IProject;

import abapci.activation.Activation;
import abapci.domain.AbapPackageTestState;
import abapci.domain.TestResultSummary;
import abapci.domain.TestState;
import abapci.handlers.AbapUnitHandler;
import abapci.presenter.ContinuousIntegrationPresenter;

public class AUnitTestManager extends AbstractTestManager {

	public AUnitTestManager(ContinuousIntegrationPresenter continuousIntegrationPresenter, IProject project,
			List<String> packageNames) {
		super(continuousIntegrationPresenter, project, packageNames);
	}

	public TestState executeAllPackages(IProject project, List<AbapPackageTestState> activeAbapPackageTestStates,
			List<Activation> inactiveObjects) {

		TestResultSummary unitTestResultSummary;

		overallTestState = TestState.UNDEF;
		for (AbapPackageTestState abapPackageTestState : activeAbapPackageTestStates) {

			if (!abapPackageTestState.getUnitTestState().equals(TestState.DEACT)
					&& packageNames.stream().anyMatch(item -> item.equals(abapPackageTestState.getPackageName()))) {
				if (inactiveObjects == null) {
					unitTestResultSummary = new AbapUnitHandler().executePackage(project,
							abapPackageTestState.getPackageName());
				} else {
					unitTestResultSummary = new AbapUnitHandler().executeObjects(project,
							abapPackageTestState.getPackageName(), inactiveObjects);
					continuousIntegrationPresenter.mergeUnitTestResult(unitTestResultSummary);
				}

				abapPackageTestState.setUnitTestResult(unitTestResultSummary.getTestResult());
				mergePackageTestStateIntoGlobalTestState(unitTestResultSummary.getTestResult().getTestState());

			}

		}
		return overallTestState;
	}

}
