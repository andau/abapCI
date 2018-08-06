package abapci.domain;

public class ContinuousIntegrationConfig {

	private String projectName;
	private String packageName;
	private boolean utActivated;
	private boolean atcActivated;

	public ContinuousIntegrationConfig(String projectName, String packageName, boolean utActivated,
			boolean atcActivated) {
		this.projectName = projectName;
		this.packageName = packageName;
		this.utActivated = utActivated;
		this.atcActivated = atcActivated;
	}

	public String getProjectName() {
		return projectName;
	}

	public String getPackageName() {
		return packageName;
	}

	public boolean getUtActivated() {
		return utActivated;
	}

	public boolean getAtcActivated() {
		return atcActivated;
	}

}
