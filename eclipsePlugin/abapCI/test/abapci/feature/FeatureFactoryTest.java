package abapci.feature;

import static org.junit.Assert.*;

import org.eclipse.jface.preference.IPreferenceStore;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;

import abapci.preferences.PreferenceConstants;

public class FeatureFactoryTest {

	
	@Test
	public void testCreateAtcFeature() {
		IPreferenceStore prefs = Mockito.mock(IPreferenceStore.class); 
		FeatureFactory cut = new FeatureFactory(); 
		Whitebox.setInternalState(cut, "prefs", prefs);
		
		AtcFeature atcFeature = cut.createAtcFeature(); 
		assertFalse(atcFeature.isActive());
		Mockito.when(prefs.getBoolean(PreferenceConstants.PREF_ATC_RUN_ACTIVATED_OBJECTS_ONLY)).thenReturn(true); 	
		atcFeature = cut.createAtcFeature(); 
		assertTrue(atcFeature.isActive());
	}

}
