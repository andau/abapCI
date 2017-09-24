package abapci.connections;

import abapci.domain.TestResultSummary;

public interface ISapConnection {
	
	TestResultSummary executeTests(String packageName); 
}
