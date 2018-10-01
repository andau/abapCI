package abapci.manager;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.core.resources.IProject;

import com.sap.adt.atc.model.atcworklist.IAtcWorklist;

import abapci.activation.Activation;
import abapci.domain.AbapPackageTestState;
import abapci.domain.TestResult;
import abapci.domain.TestResultSummary;
import abapci.domain.TestState;
import abapci.handlers.AbapAtcHandler;
import abapci.presenter.ContinuousIntegrationPresenter;
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

				List<Activation> inactiveObjectsForPackage = inactiveObjects.stream()
						.filter(item -> item.getPackageName().equals(abapPackageTestState.getPackageName()))
						.collect(Collectors.<Activation>toList());

				if (inactiveObjectsForPackage.size() > 0) {
					atcWorklist = new AbapAtcHandler().executeObjects(project, inactiveObjects);
				}
			}

			if (atcWorklist != null) {

				TestResult testResult = AtcResultAnalyzer.getTestResult(atcWorklist, inactiveObjects);
				TestResultSummary testResultSummary = new TestResultSummary(abapPackageTestState.getPackageName(),
						AtcResultAnalyzer.getTestResult(atcWorklist, inactiveObjects));
				continuousIntegrationPresenter.mergeAtcWorklist(testResultSummary);
				mergePackageTestStateIntoGlobalTestState(testResult.getTestState());

			}
		}

		return overallTestState;

	}
}
