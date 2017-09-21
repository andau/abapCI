package abapci.Domain;

public class TestResultSummary {

	private final String packageName;
	private final TestState testState;

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
	

}
