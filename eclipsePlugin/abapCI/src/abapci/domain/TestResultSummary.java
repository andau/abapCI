package abapci.domain;

import java.beans.PropertyChangeSupport;
import java.util.List;

public class TestResultSummary {

	private final String packageName;
	private TestResult testResult;

	private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

	public TestResultSummary(String packageName, boolean testrunOk, int numTests, List<InvalidItem> invalidItems) {
		this.packageName = packageName;
		this.testResult = new TestResult(testrunOk, numTests, invalidItems);
	}

	public TestResultSummary(String packageName, TestResult testResult) {
		this.packageName = packageName;
		this.testResult = testResult;
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
