package abapci.feature.activeFeature;

public abstract class ActiveFeature {
    private boolean active;

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}  
}
