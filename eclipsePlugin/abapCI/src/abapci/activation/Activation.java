package abapci.activation;

public class Activation {

	public Activation(String objectName, String packageName, String projectName) {
		this.objectName = objectName;
		this.packageName = packageName;
		this.projectName = projectName;
	}

	private String objectName;

	public String getObjectName() {
		return objectName;
	}

	private String packageName;

	public String getPackageName() {
		return packageName;
	}

	private String projectName;

	public String getProjectName() {
		return projectName;
	}

	private ActivationStatus status;

	public ActivationStatus getActivationStatus() {
		return status;
	}

	public void setActivated() {
		status = ActivationStatus.ACTIVATED;
	}

	public boolean isActivated() {
		return status == ActivationStatus.ACTIVATED;
	}

	public void setIncludedInJob() {
		status = ActivationStatus.ADDED_TO_JOB;
	}

	public boolean isIncludedInJob() {
		return status == ActivationStatus.ADDED_TO_JOB;
	}

}
