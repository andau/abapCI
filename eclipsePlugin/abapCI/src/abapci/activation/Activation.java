package abapci.activation;

import java.net.URI;

import org.eclipse.core.resources.IProject;

public class Activation {

	private IProject project;
	private String packageName;
	private String objectName;
	private ActivationStatus status;
	private URI uri;
	private String type;

	public Activation(String objectName, String packageName, IProject project, URI uri, String type) {
		this.project = project;
		this.objectName = objectName;
		this.packageName = packageName;
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

	public IProject getProject() {
		return project;
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
