package abapci.domain;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import abapci.lang.UiTexts;

public class AbapPackageTestState {
    private String packageName;
    private String jenkinsState;
    private String abapState;
	private String atcState;
	
	private String lastRun;
    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(
            this);


    public AbapPackageTestState() {
    }

    public AbapPackageTestState(String packageName, String jenkinsState, String abapState, String atcState) {
        super();
        this.packageName = packageName;
        this.jenkinsState = jenkinsState;
        this.abapState = abapState;
        this.atcState = atcState; 
    }

    public AbapPackageTestState(String packageName) {
        super();
        this.packageName = packageName;
        this.jenkinsState = UiTexts.NOT_YET_EXECUTED;
        this.abapState = UiTexts.NOT_YET_EXECUTED;
        this.atcState = UiTexts.NOT_YET_EXECUTED;
    }

    
    public void addPropertyChangeListener(String propertyName,
            PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    public String getPackageName() {
        return packageName;
    }


    public String getJenkinsState() {
        return jenkinsState;
    }

    public String getAbapState() {
        return abapState;
    }

    public String getAtcState() {
        return atcState;
    }

	public String getLastRun() {
		return lastRun;
	}

    public void setPackageName(String packageName) {
    	this.packageName = packageName; 
        propertyChangeSupport.firePropertyChange("packageName", this.packageName, packageName);
    }

 
    public void setJenkinsState(String jenkinsState) {
    	this.jenkinsState = jenkinsState; 
    	propertyChangeSupport.firePropertyChange("jenkinsState", this.jenkinsState, jenkinsState);
    }

    public void setAbapState(String abapState) {
    	this.abapState = abapState; 
        propertyChangeSupport.firePropertyChange("abapState", this.abapState, abapState);
    }

	public void setAtcState(String atcState) {
		this.atcState = atcState; 
        propertyChangeSupport.firePropertyChange("atcState", this.atcState,   atcState);
	}
	
    public void setLastRun(String lastRun) {
        propertyChangeSupport.firePropertyChange("lastRun", this.lastRun,
                this.lastRun = lastRun);
    }

    
    @Override
    public String toString() {
        return packageName + " " + jenkinsState + " " + abapState;
    }

}
