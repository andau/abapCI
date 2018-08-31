package abapci.domain;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.text.SimpleDateFormat;

public class AbapPackageTestState {
	private String projectName;
	private String packageName;
	private String jenkinsInfo;
	private TestResult aUnitTestResult;
	private TestResult atcTestResult;

	private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

	public AbapPackageTestState() {
	}

	public AbapPackageTestState(String projectName, String packageName, String jenkinsInfo, TestResult aUnitTestResult,
			TestResult atcTestResult) {
		super();
		this.projectName = projectName;
		this.packageName = packageName;
		this.jenkinsInfo = jenkinsInfo;
		this.aUnitTestResult = aUnitTestResult;
		this.atcTestResult = atcTestResult;
	}

	public AbapPackageTestState(String projectName, String packageName) {
		super();
		this.projectName = projectName;
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

	public String getProjectName() {
		return projectName;
	}

	public String getPackageName() {
		return packageName;
	}

	public String getJenkinsInfo() {
		return jenkinsInfo;
	}

	public TestState getUnitTestState() {
		return aUnitTestResult.getTestState();
	}

	public int getAUnitNumOk() {
		return aUnitTestResult.getNumOk();
	}

	public int getAUnitNumErr() {
		return aUnitTestResult.getActiveErrors().size();
	}

	public int getAUnitNumSuppressed() {
		return aUnitTestResult.getSuppressedErrors().size();
	}

	public String getAUnitLastRun() {
		return aUnitTestResult.getLastRun() != null ? new SimpleDateFormat("HH:mm").format(aUnitTestResult.getLastRun())
				: "";
	}

	public void setJenkinsInfo(String jenkinsInfo) {
		this.jenkinsInfo = jenkinsInfo;
		propertyChangeSupport.firePropertyChange("jenkinsInfo", this.jenkinsInfo, jenkinsInfo);
	}

	public void setUnitTestResult(TestResult aUnitTestResult) {
		this.aUnitTestResult = aUnitTestResult;
		propertyChangeSupport.firePropertyChange("aUnitTestResult", this.aUnitTestResult, aUnitTestResult);
	}

	public TestState getAtcTestState() {
		return atcTestResult.getTestState();
	}

	public int getAtcNumErr() {
		return this.atcTestResult.getActiveErrors().size();
	}

	public String getAtcNumWarn() {
		return "n/a";
	}

	public String getAtcNumInfo() {
		return "n/a";
	}

	public int getAtcNumSuppressed() {
		return this.atcTestResult.getSuppressedErrors().size();
	}

	public String getAtcLastRun() {
		return atcTestResult.getLastRun() != null ? new SimpleDateFormat("HH:mm").format(atcTestResult.getLastRun())
				: "";
	}

	public void setAtcTestResult(TestResult atcTestResult) {
		this.atcTestResult = atcTestResult;
		propertyChangeSupport.firePropertyChange("atcTestResult", this.atcTestResult, atcTestResult);
	}

	@Override
	public String toString() {
		return packageName + " " + jenkinsInfo + " " + aUnitTestResult.getTestState() + " "
				+ atcTestResult.getTestState();
	}

	public InvalidItem getFirstFailedUnitTest() {
		return aUnitTestResult.getActiveErrors().size() > 0 ? aUnitTestResult.getActiveErrors().get(0) : null;
	}

	public InvalidItem getFirstFailedAtc() {
		return atcTestResult.getActiveErrors().size() > 0 ? atcTestResult.getActiveErrors().get(0) : null;
	}

	public TestResult getAtcTestResult() {
		return atcTestResult;
	}

}
