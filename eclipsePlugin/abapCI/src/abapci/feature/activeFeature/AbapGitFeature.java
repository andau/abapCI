package abapci.feature.activeFeature;

public class AbapGitFeature extends ActiveFeature {

	boolean onlyOneAbapGitTransactionActive;

	public boolean isOnlyOneAbapGitTransactionActive() {
		return onlyOneAbapGitTransactionActive;
	}

	public void setOnlyOneAbapGitTransactionActive(boolean active) {
		onlyOneAbapGitTransactionActive = active;
	}
}
