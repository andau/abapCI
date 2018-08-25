package abapci.feature;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.swt.graphics.RGB;

import abapci.AbapCiPlugin;

public class ColorFeature {

	IPreferenceStore store;
	private String preferenceConstant;

	public ColorFeature() {
		store = AbapCiPlugin.getDefault().getPreferenceStore();
	}

	public void setPreferenceConstant(String preferenceConstant) {
		this.preferenceConstant = preferenceConstant;
	}

	public RGB getColor() {
		return PreferenceConverter.getColor(store, preferenceConstant);
	}

	public void writePreference(RGB color) {
		PreferenceConverter.setValue(store, preferenceConstant, color);
	}
}
