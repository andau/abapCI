package abapci.feature.activeFeature;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.swt.graphics.RGB;

import abapci.AbapCiPluginHelper;

public class ColorFeature {

	IPreferenceStore store;
	private String preferenceConstant;
	private AbapCiPluginHelper abapCiPluginHelper; 

	public ColorFeature() {
		abapCiPluginHelper = new AbapCiPluginHelper(); 
		store = abapCiPluginHelper.getPreferenceStore();
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
