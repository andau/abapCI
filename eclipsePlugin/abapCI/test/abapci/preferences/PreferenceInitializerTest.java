package abapci.preferences;

import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.times;

import org.eclipse.jface.preference.IPreferenceStore;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;

import abapci.AbapCiPluginHelper;

public class PreferenceInitializerTest {

	private final AbapCiPluginHelper abapCiPluginHelper = Mockito.mock(AbapCiPluginHelper.class);
	IPreferenceStore preferenceStore = Mockito.mock(IPreferenceStore.class);

	@Test
	public void testPreferenceInitializer() {
		PreferenceInitializer cut = new PreferenceInitializer();
		Whitebox.setInternalState(cut, "abapCiPluginHelper", abapCiPluginHelper);
		Mockito.when(abapCiPluginHelper.getPreferenceStore()).thenReturn(preferenceStore);
		cut.initializeDefaultPreferences();

		Mockito.verify(preferenceStore, atLeast(10)).setDefault(Mockito.any(String.class), Mockito.any(boolean.class));
		Mockito.verify(preferenceStore, times(3)).setDefault(Mockito.any(String.class), Mockito.any(int.class));
	}

}
