package abapci.feature;

public abstract class ActiveFeature {
    private boolean active;

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}  
}
