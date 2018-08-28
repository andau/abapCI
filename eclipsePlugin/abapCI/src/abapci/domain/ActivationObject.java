package abapci.domain;

public class ActivationObject {
	public String packagename;
	public String classname;
	private String uri;

	public ActivationObject(String packagename, String classname, String uri) {
		this.packagename = packagename;
		this.classname = classname;
		this.uri = uri;
	}

	public String getPackagename() {
		return packagename;
	}

	public String getClassname() {
		return classname;
	}

	public String getUri() {
		return uri;
	}

}
