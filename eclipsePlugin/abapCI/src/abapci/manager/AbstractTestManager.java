package abapci.manager;

import java.util.List;

import abapci.domain.AbapPackageTestState;
import abapci.domain.GlobalTestState;
import abapci.domain.TestState;
import abapci.views.ViewModel;

abstract class AbstractTestManager {

	protected TestState overallTestState;

	protected void mergePackageTestStateIntoGlobalTestState(TestState packageTestState) {
		if (overallTestState == null && packageTestState == TestState.OK) {
			overallTestState = packageTestState;
		}

		switch (packageTestState) {
		case UNDEF:
		case NOK:
		case OFFL: 	
			overallTestState = packageTestState;
			break;
		case OK:
			overallTestState = (overallTestState == TestState.UNDEF) ? packageTestState : overallTestState;
			break;
		}

	}
	
	protected void setAbapPackagesTestState(List<AbapPackageTestState> packageTestStates, TestState testState, TestStateType teststateType) {
		ViewModel.INSTANCE.setPackageTestStates(packageTestStates);

		if (teststateType == teststateType.UNIT) {
			ViewModel.INSTANCE.setUnitState(testState);					
		}
		else 
		{
			ViewModel.INSTANCE.setAtcState(testState);								
		}
	}

	
	public enum TestStateType 
	{
		UNIT, 
		ATC
	}

}
