package abapci.feature;

import org.eclipse.jface.preference.IPreferenceStore;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import abapci.AbapCiPlugin;
import abapci.preferences.PreferenceConstants;

@RunWith(PowerMockRunner.class)
@PrepareForTest(AbapCiPlugin.class)
public class FeatureDecisionTest {

	@Test
	public void runUnitTestsOnSaveTest() throws Exception {
		IPreferenceStore preferenceStore = Mockito.mock(IPreferenceStore.class);
		AbapCiPlugin abapCiPlugin = PowerMockito.mock(AbapCiPlugin.class);

		PowerMockito.when(abapCiPlugin.getPreferenceStore()).thenReturn(preferenceStore);

		PowerMockito.when(preferenceStore.getBoolean(PreferenceConstants.PREF_ABAP_UNIT_RUN_ON_SAVE)).thenReturn(true);
		FeatureDecision featureDecision = new FeatureDecision(preferenceStore);
	
		PowerMockito.when(preferenceStore.getBoolean(PreferenceConstants.PREF_ABAP_UNIT_RUN_ON_SAVE)).thenReturn(false);
		featureDecision = new FeatureDecision(preferenceStore);
		

		Assert.assertFalse(featureDecision.runUnitTestsOnSave());
	}
}
