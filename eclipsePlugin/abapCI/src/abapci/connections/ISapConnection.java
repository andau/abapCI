package abapci.connections;

import abapci.Domain.TestResultSummary;

public interface ISapConnection {
	
	TestResultSummary executeTests(String packageName); 
}
