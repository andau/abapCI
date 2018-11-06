package abapci.feature.activeFeature;

import org.eclipse.jface.preference.IPreferenceStore;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;

import abapci.AbapCiPluginHelper;

public class SimpleToggleFeatureTest extends AbstractFeatureTest {

	private static final String TEST_PREFERENCE = "TEST_PREFERENCE";
	private AbapCiPluginHelper abapCiPluginHelper = Mockito.mock(AbapCiPluginHelper.class); 
	private IPreferenceStore preferenceStore = Mockito.mock(IPreferenceStore.class);

	SimpleToggleFeature cut; 
	
	@Before 
	public void before() 
	{
		cut = new SimpleToggleFeature();
		testActiveField(cut);
		
		Mockito.when(abapCiPluginHelper.getPreferenceStore()).thenReturn(preferenceStore); 
		Whitebox.setInternalState(cut, "abapCiPluginHelper", abapCiPluginHelper);		
	}
	
	@Test 
	public void testSimpleToggleFeature() 
	{
		cut.setPreferenceConstant(TEST_PREFERENCE);
		cut.writePreference();
		Mockito.verify(preferenceStore).setValue(TEST_PREFERENCE, true); 
		
	}	
}
