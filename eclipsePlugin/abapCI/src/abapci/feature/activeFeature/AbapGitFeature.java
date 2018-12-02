package abapci.feature.activeFeature;

public class AbapGitFeature extends ActiveFeature {

	private boolean onlyOneAbapGitTransactionActive;
	private boolean changeTransactionLabelActive;

	public boolean isOnlyOneAbapGitTransactionActive() {
		return onlyOneAbapGitTransactionActive;
	}

	public void setOnlyOneAbapGitTransactionActive(boolean active) {
		onlyOneAbapGitTransactionActive = active;
	}

	public boolean isChangeTransactionLabelActive() {
		return changeTransactionLabelActive;
	}

	public void setChangeTransactionLabelActive(boolean active) {
		changeTransactionLabelActive = active;
	}
}
