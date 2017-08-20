package abapci.connections;

import abapci.Domain.TestResultSummary;

public class SapDemoConnection implements ISapConnection {


	public TestResultSummary executeTests(String packageName) {
		TestResultSummary testResultSummary; 
		
		switch(packageName) 
		{
		   case "ZSAMPLE_ERROR":  
		   {
			   testResultSummary = new TestResultSummary(packageName, 1); 
			   break; 
		   }
		   default: 
			   testResultSummary = new TestResultSummary(packageName, 0); 
		}
		
		return testResultSummary; 
	}
}