package abapci.connections;

import abapci.Domain.TestResultSummary;
import abapci.Domain.TestState;

@Deprecated
public class SapDemoConnection implements ISapConnection {


	public TestResultSummary executeTests(String packageName) {
		TestResultSummary testResultSummary; 
		
		switch(packageName) 
		{
		   case "ZSAMPLE_ERROR":  
		   {
			   testResultSummary = new TestResultSummary(packageName, TestState.NOK); 
			   break; 
		   }
		   default: 
			   testResultSummary = new TestResultSummary(packageName, TestState.OK); 
		}
		
		return testResultSummary; 
	}
}