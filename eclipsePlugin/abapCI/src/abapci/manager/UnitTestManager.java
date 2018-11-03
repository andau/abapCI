package abapci.manager;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.JavaCore;

import abapci.activation.Activation;
import abapci.activation.ProgLanguageFactorySelector;
import abapci.domain.AbapPackageTestState;
import abapci.domain.TestState;
import abapci.handlers.IUnitHandler;
import abapci.presenter.ContinuousIntegrationPresenter;
import abapci.testResult.TestResultSummary;

public class UnitTestManager extends AbstractTestManager {

	public UnitTestManager(ContinuousIntegrationPresenter continuousIntegrationPresenter, IProject project,
			List<String> packageNames) {
		super(continuousIntegrationPresenter, project, packageNames);
	}

	public TestState executeAllPackages(IProject project, List<AbapPackageTestState> activeAbapPackageTestStates,
			List<Activation> activatedObjects) {

		TestResultSummary unitTestResultSummary = null;
		ProgLanguageFactorySelector progLanguageFactorySelector = new ProgLanguageFactorySelector(); 
		  
		IUnitHandler unitHandler = progLanguageFactorySelector.determine(project).createUnitHandler();

		overallTestState = TestState.UNDEF;
		for (AbapPackageTestState abapPackageTestState : activeAbapPackageTestStates) {

			if (!abapPackageTestState.getUnitTestState().equals(TestState.DEACT)
					&& packageNames.stream().anyMatch(item -> item.equals(abapPackageTestState.getPackageName()))) {

				if (activatedObjects == null) {
						unitTestResultSummary = unitHandler.executePackage(project,
								abapPackageTestState.getPackageName(), new HashSet<Activation>());

						abapPackageTestState.setUnitTestResult(unitTestResultSummary.getTestResult());
						mergePackageTestStateIntoGlobalTestState(unitTestResultSummary.getTestResult().getTestState());

					} else {
						Set<Activation> inactiveObjectsForPackage = activatedObjects.stream()
								.filter(item -> item.getPackageName().equals(abapPackageTestState.getPackageName()))
								.collect(Collectors.toSet());

						if (inactiveObjectsForPackage.size() > 0) {
							unitTestResultSummary = unitHandler.executeObjects(project,
									abapPackageTestState.getPackageName(), inactiveObjectsForPackage);
							continuousIntegrationPresenter.mergeUnitTestResult(unitTestResultSummary);
						}
					}
			}

		}
		return overallTestState;
	}

}
