package abapci.feature;

import org.eclipse.jface.preference.IPreferenceStore;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

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

		PowerMockito.when(preferenceStore.getBoolean(PreferenceConstants.PREF_UNIT_RUN_ON_SAVE)).thenReturn(true);
		FeatureFacade featureFacade = new FeatureFacade();
	    FeatureFactory featureFactory = new FeatureFactory(); 
	    Whitebox.setInternalState(featureFactory, "prefs", preferenceStore);
	    Whitebox.setInternalState(featureFacade, "featureFactory", featureFactory);
	
		PowerMockito.when(preferenceStore.getBoolean(PreferenceConstants.PREF_UNIT_RUN_ON_SAVE)).thenReturn(false);
	    Whitebox.setInternalState(featureFactory, "prefs", preferenceStore);
		

		Assert.assertFalse(featureFacade.getUnitFeature().isActive());
	}
}
