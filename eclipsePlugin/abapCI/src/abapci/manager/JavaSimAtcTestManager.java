package abapci.manager;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.core.resources.IProject;
import abapci.activation.Activation;
import abapci.domain.AbapPackageTestState;
import abapci.domain.TestState;
import abapci.handlers.JavaAtcSimuHandler;
import abapci.presenter.ContinuousIntegrationPresenter;
import abapci.testResult.TestResultSummary;

public class JavaSimAtcTestManager extends AbstractTestManager implements IAtcTestManager {
	
	JavaAtcSimuHandler javaAtcHandler; 
	
	public JavaSimAtcTestManager(ContinuousIntegrationPresenter continuousIntegrationPresenter, IProject project,
			List<String> packageNames) {
		super(continuousIntegrationPresenter, project, packageNames);
		javaAtcHandler = new JavaAtcSimuHandler(); 
	}

	public TestState executeAllPackages(IProject project, List<AbapPackageTestState> activeAbapPackageTestStates,
			List<Activation> inactiveObjects)  {

		overallTestState = TestState.UNDEF;

		for (AbapPackageTestState abapPackageTestState : activeAbapPackageTestStates.stream()
				.filter(item -> !item.getAtcTestState().equals(TestState.DEACT)).collect(Collectors.toList())) {
			{
				TestResultSummary testResultSummary =  javaAtcHandler.executePackage(project, abapPackageTestState.getPackageName(), new HashSet<Activation>(inactiveObjects)); 
				continuousIntegrationPresenter.mergeAtcWorklist(testResultSummary);
				mergePackageTestStateIntoGlobalTestState(testResultSummary.getTestResult().getTestState());
			}

		}

		return overallTestState;

	}
}
