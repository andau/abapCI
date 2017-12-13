package abapci.domain;

import java.beans.PropertyChangeSupport;
import java.util.List;

public class UnitTestResultSummary {

	private final String packageName;
	private TestResult testResult; 
	
    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(
            this);

	public UnitTestResultSummary(String packageName, boolean testrunOk, List<InvalidItem> invalidItems) {
		this.packageName = packageName;
		this.testResult = new TestResult(testrunOk, invalidItems); 
	}

	public String getPackageName() {
		return packageName;
	}

	public TestResult getTestResult() {
		return testResult;
	}
		
	public void setTestState(TestResult testResult) {
		this.testResult = testResult; 
		propertyChangeSupport.firePropertyChange("testResult", this.testResult, testResult);
	}
	

}
