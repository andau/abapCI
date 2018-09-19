package abapci.domain;

@Deprecated
public class ActivationObject {
	public String packagename;
	public String classname;
	private String uri;
	private String type;

	public ActivationObject(String packagename, String classname, String uri, String type) {
		this.packagename = packagename;
		this.classname = classname;
		this.uri = uri;
		this.type = type;
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

	public String getType() {
		return type;
	}

}
