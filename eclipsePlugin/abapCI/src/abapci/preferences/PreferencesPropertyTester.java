package abapci.preferences;

import org.eclipse.core.expressions.PropertyTester;

import abapci.AbapCiPluginHelper;

public class PreferencesPropertyTester extends PropertyTester {

	@Override
	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
		final String preferenceName = expectedValue.toString();
		return isEnabled(preferenceName);
	}

	public static boolean isEnabled(String preferenceName) {
		final AbapCiPluginHelper abapCiPluginHelper = new AbapCiPluginHelper();
		return abapCiPluginHelper.getPreferenceStore().getBoolean(preferenceName);
	}

}