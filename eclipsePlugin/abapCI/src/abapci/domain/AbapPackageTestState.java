package abapci.domain;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.text.SimpleDateFormat;

public class AbapPackageTestState {
	private String packageName;
	private String jenkinsInfo;
	private TestResult aUnitTestResult;
	private TestResult atcTestResult;

	private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

	public AbapPackageTestState() {
	}

	public AbapPackageTestState(String packageName, String jenkinsInfo, TestResult aUnitTestResult, TestResult atcTestResult) {
		super();
		this.packageName = packageName;
		this.jenkinsInfo = jenkinsInfo;
		this.aUnitTestResult = aUnitTestResult;
		this.atcTestResult = atcTestResult;
	}

	public AbapPackageTestState(String packageName) {
		super();
		this.packageName = packageName;
		this.jenkinsInfo = TestState.UNDEF.toString();
		this.aUnitTestResult = new TestResult(); 
		this.atcTestResult = new TestResult();
	}

	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.removePropertyChangeListener(listener);
	}

	public String getPackageName() {
		return packageName;
	}

	public String getJenkinsInfo() {
		return jenkinsInfo;
	}

	public String getAUnitInfo() {
		return aUnitTestResult.getTestResultInfo();
	}
	
	public String getAUnitNumSuppressed() {
		return Integer.toString(aUnitTestResult.getSuppressedErrors().size());
	}

	public String getAUnitLastRun() {
		return aUnitTestResult.getTestState() != TestState.UNDEF ? 
			new SimpleDateFormat("HH:mm").format(aUnitTestResult.getLastRun()) : ""; 	
	}



	public void setJenkinsInfo(String jenkinsInfo) {
		this.jenkinsInfo = jenkinsInfo;
		propertyChangeSupport.firePropertyChange("jenkinsInfo", this.jenkinsInfo, jenkinsInfo);
	}

	public void setAUnitInfo(TestResult aUnitTestResult) {
		this.aUnitTestResult = aUnitTestResult;
		propertyChangeSupport.firePropertyChange("aUnitTestResult", this.aUnitTestResult, aUnitTestResult);
	}

	public String getAtcInfo() {
		return atcTestResult.getTestResultInfo();
	}
	public String getAtcNumSuppressed() {
		return Integer.toString(this.atcTestResult.getSuppressedErrors().size()); 
	}

	public String getAtcLastRun() {
		return atcTestResult.getTestState() != TestState.UNDEF ? 
				new SimpleDateFormat("HH:mm").format(atcTestResult.getLastRun()) : ""; 
	}

	public void setAtcInfo(TestResult atcTestResult) {
		this.atcTestResult = atcTestResult;
		propertyChangeSupport.firePropertyChange("atcTestResult", this.atcTestResult, atcTestResult);
	}

	@Override
	public String toString() {
		return packageName + " " + jenkinsInfo + " " + aUnitTestResult.getTestState() + " " + atcTestResult.getTestState();
	}




}
