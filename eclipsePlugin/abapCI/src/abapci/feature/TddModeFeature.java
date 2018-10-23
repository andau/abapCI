package abapci.feature;

public class TddModeFeature extends ActiveFeature {

	private int minRequiredSeconds;

	public void setMinimumRequiredSeconds(int minRequiredSeconds) {
		this.minRequiredSeconds = minRequiredSeconds;
	}

	public int getMinimumRequiredSeconds() {
		return minRequiredSeconds;
	}

}
