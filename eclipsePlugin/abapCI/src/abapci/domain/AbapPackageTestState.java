package abapci.domain;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class AbapPackageTestState {
	private String packageName;
	private String jenkinsInfo;
	private String aUnitInfo;
	private String atcInfo;

	private String lastRun;
	private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

	public AbapPackageTestState() {
	}

	public AbapPackageTestState(String packageName, String jenkinsInfo, String aUnitInfo, String atcState) {
		super();
		this.packageName = packageName;
		this.jenkinsInfo = jenkinsInfo;
		this.aUnitInfo = aUnitInfo;
		this.atcInfo = atcState;
	}

	public AbapPackageTestState(String packageName) {
		super();
		this.packageName = packageName;
		this.jenkinsInfo = TestState.UNDEF.toString();
		this.aUnitInfo = TestState.UNDEF.toString();
		this.atcInfo = TestState.UNDEF.toString();
	}

	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.removePropertyChangeListener(listener);
	}

	public String getPackageName() {
		return packageName;
	}

	public String getJenkinsInfo() {
		return jenkinsInfo;
	}

	public String getAUnitInfo() {
		return aUnitInfo;
	}

	public String getAtcInfo() {
		return atcInfo;
	}

	public String getLastRun() {
		return lastRun;
	}

	public void setJenkinsInfo(String jenkinsInfo) {
		this.jenkinsInfo = jenkinsInfo;
		propertyChangeSupport.firePropertyChange("jenkinsInfo", this.jenkinsInfo, jenkinsInfo);
	}

	public void setAUnitInfo(String aUnitInfo) {
		this.aUnitInfo = aUnitInfo;
		propertyChangeSupport.firePropertyChange("aUnitInfo", this.atcInfo, aUnitInfo);
	}

	public void setAtcInfo(String atcInfo) {
		this.atcInfo = atcInfo;
		propertyChangeSupport.firePropertyChange("atcInfo", this.atcInfo, atcInfo);
	}

	public void setLastRun(String lastRun) {
		this.lastRun = lastRun;
		propertyChangeSupport.firePropertyChange("lastRun", this.lastRun, lastRun);
	}

	@Override
	public String toString() {
		return packageName + " " + jenkinsInfo + " " + atcInfo + " " + atcInfo;
	}

}
