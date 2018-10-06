package abapci.manager;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.core.resources.IProject;

import abapci.activation.Activation;
import abapci.domain.AbapPackageTestState;
import abapci.domain.TestState;
import abapci.handlers.AbapUnitHandler;
import abapci.presenter.ContinuousIntegrationPresenter;
import abapci.result.TestResultSummary;

public class AUnitTestManager extends AbstractTestManager {

	public AUnitTestManager(ContinuousIntegrationPresenter continuousIntegrationPresenter, IProject project,
			List<String> packageNames) {
		super(continuousIntegrationPresenter, project, packageNames);
	}

	public TestState executeAllPackages(IProject project, List<AbapPackageTestState> activeAbapPackageTestStates,
			List<Activation> inactiveObjects) {

		TestResultSummary unitTestResultSummary = null;

		overallTestState = TestState.UNDEF;
		for (AbapPackageTestState abapPackageTestState : activeAbapPackageTestStates) {

			if (!abapPackageTestState.getUnitTestState().equals(TestState.DEACT)
					&& packageNames.stream().anyMatch(item -> item.equals(abapPackageTestState.getPackageName()))) {
				if (inactiveObjects == null) {
					unitTestResultSummary = new AbapUnitHandler().executePackage(project,
							abapPackageTestState.getPackageName(), new HashSet<Activation>());
				} else {
					Set<Activation> inactiveObjectsForPackage = inactiveObjects.stream()
							.filter(item -> item.getPackageName().equals(abapPackageTestState.getPackageName()))
							.collect(Collectors.toSet());

					if (inactiveObjectsForPackage.size() > 0) {
						unitTestResultSummary = new AbapUnitHandler().executeObjects(project,
								abapPackageTestState.getPackageName(), inactiveObjectsForPackage);
						continuousIntegrationPresenter.mergeUnitTestResult(unitTestResultSummary);
					}
				}

				if (unitTestResultSummary != null) {
					abapPackageTestState.setUnitTestResult(unitTestResultSummary.getTestResult());
					mergePackageTestStateIntoGlobalTestState(unitTestResultSummary.getTestResult().getTestState());
				}

			}

		}
		return overallTestState;
	}

}
