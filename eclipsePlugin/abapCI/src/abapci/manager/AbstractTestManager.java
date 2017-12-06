package abapci.manager;

import java.util.List;

import abapci.domain.AbapPackageTestState;
import abapci.domain.TestState;
import abapci.views.ViewModel;

abstract class AbstractTestManager {

	protected List<String> packageNames; 
	
	public AbstractTestManager(List<String> packageNames)
	{
		this.packageNames = packageNames;
		overallTestState = TestState.UNDEF;
	}
	
	public void setPackages(List<String> packageNames) 
	{
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
			overallTestState = packageTestState;
			break;
		case OK:
			overallTestState = (overallTestState == TestState.UNDEF) ? packageTestState : overallTestState;
			break;
		}

	}
	
	protected void calculateOverallTestState(List<AbapPackageTestState> packageTestStates, TestStateType teststateType) 
	{
		
		if (teststateType == TestStateType.UNIT) 
		{
			if (packageTestStates.stream().anyMatch(item -> item.getAUnitInfo().equals("UNDEF"))) 
			{
			 overallTestState = TestState.UNDEF;  		 
		    }
			else if (packageTestStates.stream().anyMatch(item -> item.getAUnitInfo().equals("NOK"))) 
			{
				 overallTestState = TestState.NOK;  				
			}
			else
			{
				overallTestState = TestState.OK; 
			}			
		}
		else 
		{
			if (packageTestStates.stream().anyMatch(item -> item.getAtcInfo().equals("UNDEF"))) 
			{
			 overallTestState = TestState.UNDEF;  		 
		    }
			else if (packageTestStates.stream().anyMatch(item -> item.getAtcInfo().equals("NOK"))) 
			{
				 overallTestState = TestState.NOK;  				
			}
			else
			{
				overallTestState = TestState.OK; 
			}
		}
	}
	
	protected void setAbapPackagesTestState(List<AbapPackageTestState> packageTestStates, TestState testState, TestStateType teststateType) {
		ViewModel.INSTANCE.setPackageTestStates(packageTestStates);

		if (teststateType == TestStateType.UNIT) {
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
