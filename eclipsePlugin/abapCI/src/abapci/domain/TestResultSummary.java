package abapci.domain;

import java.beans.PropertyChangeSupport;

public class TestResultSummary {

	private final String packageName;
	private TestState testState;
    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(
            this);

	public TestResultSummary(String packageName, TestState testState) {
		this.packageName = packageName;
		this.testState = testState;
	}

	public String getPackageName() {
		return packageName;
	}

	public TestState getTestState() {
		return testState;
	}
	
	public void setTestState(TestState testState) {
		propertyChangeSupport.firePropertyChange("jenkinsState", this.testState,
	                this.testState = testState);
	}
	

}
