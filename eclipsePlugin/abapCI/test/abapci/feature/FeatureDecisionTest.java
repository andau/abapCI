package abapci.feature;

import org.eclipse.jface.preference.IPreferenceStore;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.reflect.Whitebox;

import abapci.AbapCiPlugin;
import abapci.preferences.PreferenceConstants;

public class FeatureDecisionTest {

	@Test
	public void runUnitTestsOnSaveTest() throws Exception {
		IPreferenceStore preferenceStore = Mockito.mock(IPreferenceStore.class);
		AbapCiPlugin abapCiPlugin = PowerMockito.mock(AbapCiPlugin.class);

		PowerMockito.when(abapCiPlugin.getPreferenceStore()).thenReturn(preferenceStore);

		PowerMockito.when(preferenceStore.getBoolean(PreferenceConstants.PREF_UNIT_RUN_ON_SAVE)).thenReturn(true);
		FeatureFacade featureFacade = new FeatureFacade();
	    FeatureCreator featureCreator = new FeatureCreator(); 
	    Whitebox.setInternalState(featureCreator, "prefs", preferenceStore);
	    Whitebox.setInternalState(featureFacade, "featureCreator", featureCreator);
	
		PowerMockito.when(preferenceStore.getBoolean(PreferenceConstants.PREF_UNIT_RUN_ON_SAVE)).thenReturn(false);
	    Whitebox.setInternalState(featureCreator, "prefs", preferenceStore);
		

		Assert.assertFalse(featureFacade.getUnitFeature().isActive());
	}
}
