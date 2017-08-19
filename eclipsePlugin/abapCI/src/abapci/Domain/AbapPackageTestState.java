package abapci.Domain;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class AbapPackageTestState {
    private String packageName;
    private String jenkinsState;
    private String abapState;
    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(
            this);

    public AbapPackageTestState() {
    }

    public AbapPackageTestState(String packageName, String jenkinsState, String abapState) {
        super();
        this.packageName = packageName;
        this.jenkinsState = jenkinsState;
        this.abapState = abapState;
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

    public void setPackageName(String packageName) {
        propertyChangeSupport.firePropertyChange("packageName", this.packageName,
                this.packageName = packageName);
    }

 
    public void setJenkinsState(String jenkinsState) {
        propertyChangeSupport.firePropertyChange("jenkinsState", this.jenkinsState,
                this.jenkinsState = jenkinsState);
    }

    public void setAbapState(String abapState) {
        propertyChangeSupport.firePropertyChange("abapState", this.abapState,
                this.abapState = abapState);
    }


    @Override
    public String toString() {
        return packageName + " " + jenkinsState + " " + abapState;
    }

}
