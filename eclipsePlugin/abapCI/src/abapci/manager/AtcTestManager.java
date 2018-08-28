package abapci.manager;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.core.resources.IProject;

import com.sap.adt.atc.model.atcworklist.IAtcWorklist;

import abapci.domain.AbapPackageTestState;
import abapci.domain.ActivationObject;
import abapci.domain.TestResult;
import abapci.domain.TestState;
import abapci.handlers.AbapAtcHandler;
import abapci.presenter.ContinuousIntegrationPresenter;
import abapci.utils.AtcResultAnalyzer;

public class AtcTestManager extends AbstractTestManager {

	public AtcTestManager(ContinuousIntegrationPresenter continuousIntegrationPresenter, String projectName,
			List<String> packageNames) {
		super(continuousIntegrationPresenter, projectName, packageNames);
	}

	public TestState executeAllPackages(IProject project, List<AbapPackageTestState> activeAbapPackageTestStates,
			List<ActivationObject> inactiveObjects) {

		List<AbapPackageTestState> packageTestStates = activeAbapPackageTestStates.stream()
				.filter(item -> !item.getUnitTestState().equals(TestState.DEACT))
				.collect(Collectors.<AbapPackageTestState>toList());

		IAtcWorklist atcWorklist = null;

		packageNames.addAll(packageTestStates.stream().filter(item -> !item.getAtcTestState().equals(TestState.UNDEF))
				.map(item -> item.getPackageName()).collect(Collectors.<String>toList()));

		overallTestState = TestState.UNDEF;

		for (AbapPackageTestState abapPackageTestState : activeAbapPackageTestStates) {

			List<ActivationObject> inactiveObjectsForPackage = inactiveObjects.stream()
					.filter(item -> item.packagename.equals(abapPackageTestState.getPackageName()))
					.collect(Collectors.<ActivationObject>toList());

			if (inactiveObjectsForPackage.size() > 0) {
				atcWorklist = new AbapAtcHandler().executeObjects(project, inactiveObjects);
				TestResult testResult = AtcResultAnalyzer.getTestResult(atcWorklist);

				abapPackageTestState.setAtcTestResult(testResult);
				mergePackageTestStateIntoGlobalTestState(testResult.getTestState());

			}

		}

		return overallTestState;

	}
}
