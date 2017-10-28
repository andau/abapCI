package abapci.domain;

import java.beans.PropertyChangeSupport;

public class UnitTestResultSummary {

	private final String packageName;
	private TestState testState;
	private int numSuppressedErrors; 
	
    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(
            this);

	public UnitTestResultSummary(String packageName, TestState testState, int numSuppressedErrors) {
		this.packageName = packageName;
		this.testState = testState;
		this.numSuppressedErrors = numSuppressedErrors; 
	}

	public String getPackageName() {
		return packageName;
	}

	public TestState getTestState() {
		return testState;
	}
	
	public int getNumSuppressedErrors() 
	{
		return numSuppressedErrors; 
	}
	
	public void setTestState(TestState testState) {
		this.testState = testState; 
		propertyChangeSupport.firePropertyChange("testState", this.testState, testState);
	}
	

}
