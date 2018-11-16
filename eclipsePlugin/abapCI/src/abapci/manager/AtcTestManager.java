package abapci.manager;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.core.resources.IProject;

import com.sap.adt.atc.model.atcworklist.IAtcWorklist;

import abapci.activation.Activation;
import abapci.ci.presenter.ContinuousIntegrationPresenter;
import abapci.domain.AbapPackageTestState;
import abapci.domain.TestState;
import abapci.handlers.AbapAtcHandler;
import abapci.testResult.TestResult;
import abapci.testResult.TestResultSummary;
import abapci.utils.AtcResultAnalyzer;

public class AtcTestManager extends AbstractTestManager implements IAtcTestManager {

	public AtcTestManager(ContinuousIntegrationPresenter continuousIntegrationPresenter, IProject project,
			List<String> packageNames) {
		super(continuousIntegrationPresenter, project, packageNames);
	}

	@Override
	public TestState executeAllPackages(IProject project, List<AbapPackageTestState> activeAbapPackageTestStates,
			List<Activation> inactiveObjects) {

		overallTestState = TestState.UNDEF;

		for (final AbapPackageTestState abapPackageTestState : activeAbapPackageTestStates.stream()
				.filter(item -> !item.getAtcTestState().equals(TestState.DEACT)).collect(Collectors.toList())) {

			IAtcWorklist atcWorklist = null;

			if (abapPackageTestState.getAtcTestState() == TestState.OFFL && inactiveObjects == null) {
				atcWorklist = new AbapAtcHandler(continuousIntegrationPresenter.getAtcFeature()).executePackage(project,
						abapPackageTestState.getPackageName());
			}

			else if (inactiveObjects.stream()
					.anyMatch(item -> item.getPackageName().equals(abapPackageTestState.getPackageName()))) {

				final Set<Activation> inactiveObjectsForPackage = inactiveObjects.stream()
						.filter(item -> item.getPackageName().equals(abapPackageTestState.getPackageName()))
						.collect(Collectors.toSet());

				if (inactiveObjectsForPackage.size() > 0) {
					atcWorklist = new AbapAtcHandler(continuousIntegrationPresenter.getAtcFeature())
							.executeObjects(project, inactiveObjectsForPackage);
				}

				if (atcWorklist != null) {

					final TestResult testResult = AtcResultAnalyzer.getTestResult(atcWorklist,
							inactiveObjectsForPackage);
					final TestResultSummary testResultSummary = new TestResultSummary(project,
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
