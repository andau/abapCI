package abapci.activation;

import java.net.URI;

public class Activation {

	private String projectName;
	private String packageName;
	private String objectName;
	private ActivationStatus status;
	private URI uri;
	private String type;

	public Activation(String objectName, String packageName, String projectName, URI uri, String type) {
		this.objectName = objectName;
		this.packageName = packageName;
		this.projectName = projectName;
		this.uri = uri;
		this.status = ActivationStatus.MODIFIED;
		this.type = type;
	}

	public String getObjectName() {
		return objectName;
	}

	public String getPackageName() {
		return packageName;
	}

	public String getProjectName() {
		return projectName;
	}

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

	public String getPackagename() {
		return packageName;
	}

	public URI getUri() {
		return uri;
	}

	public String getType() {
		return type;
	}

}
