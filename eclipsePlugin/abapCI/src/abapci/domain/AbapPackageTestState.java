package abapci.domain;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;

import abapci.testResult.TestResult;

public class AbapPackageTestState {
	private String projectName;
	private String packageName;
	private String jenkinsInfo;
	private TestResult unitTestResult;
	private TestResult atcTestResult;

	private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

	public AbapPackageTestState() {
	}

	public AbapPackageTestState(String projectName, String packageName, String jenkinsInfo, TestResult aUnitTestResult,
			TestResult atcTestResult) {
		super();
		this.projectName = projectName;
		this.packageName = packageName;
		this.jenkinsInfo = jenkinsInfo;
		unitTestResult = aUnitTestResult;
		this.atcTestResult = atcTestResult;
	}

	public AbapPackageTestState(String projectName, String packageName) {
		super();
		this.projectName = projectName;
		this.packageName = packageName;
		jenkinsInfo = TestState.UNDEF.toString();
		unitTestResult = new TestResult();
		atcTestResult = new TestResult();
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
		return unitTestResult.getTestState();
	}

	public int getNumTests() {
		return unitTestResult.getNumItems();
	}

	public int getAUnitNumErr() {
		return unitTestResult.getActiveErrors().size();
	}

	public int getAUnitNumSuppressed() {
		return unitTestResult.getSuppressedErrors().size();
	}

	public String getAUnitLastRun() {
		return unitTestResult.getLastRun() != null ? new SimpleDateFormat("HH:mm").format(unitTestResult.getLastRun())
				: "";
	}

	public void setJenkinsInfo(String jenkinsInfo) {
		this.jenkinsInfo = jenkinsInfo;
		propertyChangeSupport.firePropertyChange("jenkinsInfo", this.jenkinsInfo, jenkinsInfo);
	}

	public void setUnitTestResult(TestResult aUnitTestResult) {
		unitTestResult = aUnitTestResult;
		propertyChangeSupport.firePropertyChange("aUnitTestResult", unitTestResult, aUnitTestResult);
	}

	public TestState getAtcTestState() {
		return atcTestResult.getTestState();
	}

	public int getAtcNumFiles() {
		return atcTestResult.getNumItems();
	}

	public int getAtcNumErr() {
		return atcTestResult.getActiveErrors(ErrorPriority.ERROR).size();
	}

	public int getAtcNumWarn() {
		return atcTestResult.getActiveErrors(ErrorPriority.WARNING).size();
	}

	public int getAtcNumInfo() {
		return atcTestResult.getActiveErrors(ErrorPriority.INFO).size();
	}

	public int getAtcNumSuppressed() {
		return atcTestResult.getSuppressedErrors().size();
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
		return packageName + " " + jenkinsInfo + " " + unitTestResult.getTestState() + " "
				+ atcTestResult.getTestState();
	}

	@Deprecated
	public InvalidItem getFirstFailedUnitTest() {
		return unitTestResult.getActiveErrors(ErrorPriority.ERROR).size() > 0
				? unitTestResult.getActiveErrors(ErrorPriority.ERROR).iterator().next()
				: null;
	}

	public Set<InvalidItem> getFirstUnitTestErrors() {
		final Set<InvalidItem> firstFailedUnitTestErrors = new HashSet<InvalidItem>();

		for (final InvalidItem invalidItem : unitTestResult.getActiveErrors(ErrorPriority.ERROR)) {
			if (setDoesNotContainClassNameAlready(firstFailedUnitTestErrors, invalidItem)) {
				firstFailedUnitTestErrors.add(invalidItem);
			}
		}
		return firstFailedUnitTestErrors;
	}

	@Deprecated
	public InvalidItem getFirstFailedAtc() {
		return atcTestResult.getActiveErrors(ErrorPriority.ERROR).size() > 0
				? atcTestResult.getActiveErrors(ErrorPriority.ERROR).iterator().next()
				: null;
	}

	public Set<InvalidItem> getFirstFailedAtcErrors() {
		final Set<InvalidItem> firstFailedAtcErrors = new HashSet<InvalidItem>();

		for (final InvalidItem invalidItem : atcTestResult.getActiveErrors(ErrorPriority.ERROR)) {
			if (setDoesNotContainClassNameAlready(firstFailedAtcErrors, invalidItem)) {
				firstFailedAtcErrors.add(invalidItem);
			}
		}
		return firstFailedAtcErrors;
	}

	private boolean setDoesNotContainClassNameAlready(Set<InvalidItem> firstFailedAtcErrors, InvalidItem invalidItem) {
		return !firstFailedAtcErrors.stream().anyMatch(item -> item.getClassName().equals(invalidItem.getClassName()));
	}

	public TestResult getAtcTestResult() {
		return atcTestResult;
	}

	public TestResult getUnitTestResult() {
		return unitTestResult;
	}

}
