package abapci.result;


import com.sap.adt.tools.abapsource.abapunit.IAbapUnitResult;
import com.sap.adt.tools.abapsource.abapunit.IAbapUnitResultItem;

import abapci.Domain.TestResultSummary;
import abapci.Domain.TestState;

public class TestResultSummaryFactory {

	private static final String UNDEFINED_PACKAGE_NAME = null;

	public static TestResultSummary create(String packageName, IAbapUnitResult abapUnitResult) 
	{
	    int numCritialAlerts = abapUnitResult.getAlerts().size();
	    
	    for(IAbapUnitResultItem abapUnitResultItem : abapUnitResult.getItems())
	    {
	    	numCritialAlerts = numCritialAlerts + getNumCriticalAlerts(abapUnitResultItem);     	
	    }
	    TestState testState = numCritialAlerts == 0 ? TestState.OK : TestState.NOK; 
		return new TestResultSummary(packageName, testState);
	}
	
	private static int getNumCriticalAlerts(IAbapUnitResultItem abapUnitResultItem) 
	{
		int numCritialAlerts = abapUnitResultItem.getAlerts().size();
	    for(IAbapUnitResultItem abapUnitResultSubItem : abapUnitResultItem.getChildItems())
	    {
	    	numCritialAlerts = numCritialAlerts + getNumCriticalAlerts(abapUnitResultSubItem);     	
	    }
	    return numCritialAlerts; 
	}

	public static TestResultSummary createUndefined(String packageName) {
		return new TestResultSummary(packageName, TestState.UNDEF);
	}

	public static TestResultSummary createUndefined() {
		return createUndefined(UNDEFINED_PACKAGE_NAME); 
	}

}
