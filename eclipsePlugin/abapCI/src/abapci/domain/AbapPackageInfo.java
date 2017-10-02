package abapci.domain;

public class AbapPackageInfo {
	private final String packageName;
	private JenkinsRunInfo jenkinsRunInfo;
	private AbapUnitRunInfo abapUnitRunInfo;
	private AbapAtcRunInfo abapAtcRunInfo;

	public AbapPackageInfo(String packageName) {
		this.packageName = packageName;
		this.jenkinsRunInfo = new JenkinsRunInfo();
		this.abapUnitRunInfo = new AbapUnitRunInfo();
		this.abapAtcRunInfo = new AbapAtcRunInfo();
	}

	public String getPackageName() {
		return packageName;
	}

	public JenkinsRunInfo getJenkinsRunInfo() {
		return jenkinsRunInfo;
	}

	public void setJenkinsRunInfo(JenkinsRunInfo jenkinsRunInfo) {
		this.jenkinsRunInfo = jenkinsRunInfo;
	}

	public AbapUnitRunInfo getAbapUnitRunInfo() {
		return abapUnitRunInfo;
	}

	public void setAbapUnitRunInfo(AbapUnitRunInfo abapUnitRunInfo) {
		this.abapUnitRunInfo = abapUnitRunInfo;
	}

	public AbapAtcRunInfo getAbapAtcRunInfo() {
		return abapAtcRunInfo;
	}

	public void setAbapAtcRunInfo(AbapAtcRunInfo abapAtcRunInfo) {
		this.abapAtcRunInfo = abapAtcRunInfo;
	}

	public String getPackageRunInfos() {
		return jenkinsRunInfo.getExecutionResult() + " " + abapUnitRunInfo.getExecutionResult();
	}


}
