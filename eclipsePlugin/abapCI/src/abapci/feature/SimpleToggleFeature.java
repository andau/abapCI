package abapci.feature;

import org.eclipse.jface.preference.IPreferenceStore;

import abapci.AbapCiPlugin;

public class SimpleToggleFeature extends ActiveFeature {

	private String preferenceConstant;

	public void setPreferenceConstant(String preferenceConstant) {
		this.preferenceConstant = preferenceConstant;
	}

	public void writePreference() {
		IPreferenceStore prefs = AbapCiPlugin.getDefault().getPreferenceStore();
		prefs.setValue(preferenceConstant, isActive());
	}
}
