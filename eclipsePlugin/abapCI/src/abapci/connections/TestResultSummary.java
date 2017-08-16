package abapci.connections;

public class TestResultSummary {

	private final String packageName;
	private final int numErrors;

	public TestResultSummary(String packageName, int numErrors) {
		this.packageName = packageName;
		this.numErrors = numErrors;
	}

	public String getPackageName() {
		return packageName;
	}

	public int getNumErrors() {
		return numErrors;
	}

}
