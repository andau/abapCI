package abapci.result;


import com.sap.adt.tools.abapsource.abapunit.IAbapUnitResult;
import com.sap.adt.tools.abapsource.abapunit.IAbapUnitResultItem;

import abapci.Domain.TestResultSummary;

public class TestResultSummaryFactory {

	public static TestResultSummary create(String packageName, IAbapUnitResult abapUnitResult) 
	{
	    int numCritialAlerts = abapUnitResult.getAlerts().size();
	    
	    for(IAbapUnitResultItem abapUnitResultItem : abapUnitResult.getItems())
	    {
	    	numCritialAlerts = numCritialAlerts + getNumCriticalAlerts(abapUnitResultItem);     	
	    }
		return new TestResultSummary(packageName, numCritialAlerts);
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

}
