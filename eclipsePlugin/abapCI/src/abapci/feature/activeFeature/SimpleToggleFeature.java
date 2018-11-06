package abapci.feature.activeFeature;

import org.eclipse.jface.preference.IPreferenceStore;

import abapci.AbapCiPluginHelper;

public class SimpleToggleFeature extends ActiveFeature {

	private String preferenceConstant;
	private AbapCiPluginHelper abapCiPluginHelper; 

	public SimpleToggleFeature() 
	{
		this.abapCiPluginHelper = new AbapCiPluginHelper(); 
	}
	public void setPreferenceConstant(String preferenceConstant) {
		this.preferenceConstant = preferenceConstant;
	}

	public void writePreference() {
		IPreferenceStore prefs = abapCiPluginHelper.getPreferenceStore();
		prefs.setValue(preferenceConstant, isActive());
	}
}
