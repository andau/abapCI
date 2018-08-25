package abapci.manager;

import java.util.List;

import abapci.domain.AbapPackageTestState;
import abapci.domain.TestState;
import abapci.presenter.ContinuousIntegrationPresenter;
import abapci.views.ViewModel;

abstract class AbstractTestManager {

	String projectName;
	protected List<String> packageNames;
	protected ContinuousIntegrationPresenter continuousIntegrationPresenter;

	public AbstractTestManager(ContinuousIntegrationPresenter continuousIntegrationPresenter, String projectName,
			List<String> packageNames) {
		this.continuousIntegrationPresenter = continuousIntegrationPresenter;
		this.projectName = projectName;
		this.packageNames = packageNames;
		overallTestState = TestState.UNDEF;
	}

	public void setPackages(List<String> packageNames) {
		this.packageNames = packageNames;
	}

	protected TestState overallTestState;

	protected void mergePackageTestStateIntoGlobalTestState(TestState packageTestState) {
		if (overallTestState == null && packageTestState == TestState.OK) {
			overallTestState = packageTestState;
		}

		switch (packageTestState) {
		case UNDEF:
		case NOK:
		case OFFL:
		case DEACT:
			overallTestState = packageTestState;
			break;
		case OK:
			overallTestState = (overallTestState == TestState.UNDEF) ? packageTestState : overallTestState;
			break;
		}

	}

	protected void calculateOverallTestState(List<AbapPackageTestState> packageTestStates,
			TestStateType teststateType) {

		if (teststateType == TestStateType.UNIT) {
			if (packageTestStates.stream().anyMatch(item -> item.getUnitTestState().equals(TestState.UNDEF))) {
				overallTestState = TestState.UNDEF;
			} else if (packageTestStates.stream().anyMatch(item -> item.getUnitTestState().equals(TestState.NOK))) {
				overallTestState = TestState.NOK;
			} else {
				overallTestState = TestState.OK;
			}
		} else {
			if (packageTestStates.stream().anyMatch(item -> item.getAtcTestState().equals(TestState.UNDEF))) {
				overallTestState = TestState.UNDEF;
			} else if (packageTestStates.stream().anyMatch(item -> item.getAtcTestState().equals(TestState.NOK))) {
				overallTestState = TestState.NOK;
			} else {
				overallTestState = TestState.OK;
			}
		}
	}

	protected void setAbapPackagesTestState(List<AbapPackageTestState> packageTestStates, TestState testState,
			TestStateType teststateType) {

		// continuousIntegrationPresenter.updatePackageTestStates(packageTestStates);

		if (teststateType == TestStateType.UNIT) {
			ViewModel.INSTANCE.setUnitState(testState);
		} else {
			ViewModel.INSTANCE.setAtcState(testState);
		}
	}

	public enum TestStateType {
		UNIT, ATC
	}

}
