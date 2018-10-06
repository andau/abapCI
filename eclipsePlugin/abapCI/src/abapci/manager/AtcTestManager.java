package abapci.manager;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.core.resources.IProject;

import com.sap.adt.atc.model.atcworklist.IAtcWorklist;

import abapci.activation.Activation;
import abapci.domain.AbapPackageTestState;
import abapci.domain.TestState;
import abapci.handlers.AbapAtcHandler;
import abapci.presenter.ContinuousIntegrationPresenter;
import abapci.result.TestResult;
import abapci.result.TestResultSummary;
import abapci.utils.AtcResultAnalyzer;

public class AtcTestManager extends AbstractTestManager {

	public AtcTestManager(ContinuousIntegrationPresenter continuousIntegrationPresenter, IProject project,
			List<String> packageNames) {
		super(continuousIntegrationPresenter, project, packageNames);
	}

	public TestState executeAllPackages(IProject project, List<AbapPackageTestState> activeAbapPackageTestStates,
			List<Activation> inactiveObjects) {

		overallTestState = TestState.UNDEF;

		for (AbapPackageTestState abapPackageTestState : activeAbapPackageTestStates.stream()
				.filter(item -> !item.getAtcTestState().equals(TestState.DEACT)).collect(Collectors.toList())) {

			IAtcWorklist atcWorklist = null;

			if (abapPackageTestState.getAtcTestState() == TestState.OFFL && inactiveObjects == null) {
				atcWorklist = new AbapAtcHandler().executePackage(project, abapPackageTestState.getPackageName());
			}

			else if (inactiveObjects.stream()
					.anyMatch(item -> item.getPackageName().equals(abapPackageTestState.getPackageName()))) {

				Set<Activation> inactiveObjectsForPackage = inactiveObjects.stream()
						.filter(item -> item.getPackageName().equals(abapPackageTestState.getPackageName()))
						.collect(Collectors.toSet());

				if (inactiveObjectsForPackage.size() > 0) {
					atcWorklist = new AbapAtcHandler().executeObjects(project, inactiveObjectsForPackage);
				}

				if (atcWorklist != null) {

					TestResult testResult = AtcResultAnalyzer.getTestResult(atcWorklist, inactiveObjectsForPackage);
					TestResultSummary testResultSummary = new TestResultSummary(project,
							abapPackageTestState.getPackageName(),
							AtcResultAnalyzer.getTestResult(atcWorklist, inactiveObjectsForPackage));
					continuousIntegrationPresenter.mergeAtcWorklist(testResultSummary);
					mergePackageTestStateIntoGlobalTestState(testResult.getTestState());

				}

			}

		}

		return overallTestState;

	}
}
