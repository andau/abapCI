package abapci.domain;

import java.beans.PropertyChangeSupport;

public class UnitTestResultSummary {

	private final String packageName;
	private TestState testState;
    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(
            this);

	public UnitTestResultSummary(String packageName, TestState testState) {
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
		this.testState = testState; 
		propertyChangeSupport.firePropertyChange("testState", this.testState, testState);
	}
	

}
