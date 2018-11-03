package abapci.testResult;

import java.beans.PropertyChangeSupport;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IProject;

import abapci.activation.Activation;
import abapci.domain.InvalidItem;

public class TestResultSummary {

	private final IProject project;
	private final String packageName;
	private TestResult testResult;

	private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

	public TestResultSummary(IProject project, String packageName, boolean testrunOk, int numTests,
			List<InvalidItem> invalidItems, Set<Activation> activations) {
		this.project = project;
		this.packageName = packageName;
		this.testResult = new TestResult(testrunOk, numTests, invalidItems, activations);
	}

	public TestResultSummary(IProject project, String packageName, TestResult testResult) {
		this.project = project;
		this.packageName = packageName;
		this.testResult = testResult;
	}

	public IProject getProject() {
		return project;
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
