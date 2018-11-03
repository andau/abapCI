package abapci.preferences;

import static org.junit.Assert.*;

import java.lang.reflect.Field;

import org.eclipse.jface.preference.IPreferenceStore;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;

public class PreferenceInitializerTest {

	
	@Test
	public void testInitializeDefaultPreferences() {
	 
/** TODO check if all preferences have a default value 
		IPreferenceStore store = Mockito.mock(IPreferenceStore.class); 

		PreferenceInitializer cut = new PreferenceInitializer();
		cut.initializeDefaultPreferences();
		
		
		Field[] declaredPreferenceConstants = PreferenceConstants.class.getDeclaredFields();
		
		for(Field declaredPreferenceConstant : declaredPreferenceConstants)
		{
			Mockito.verify(store).setDefault(declaredPreferenceConstant.getName(), Mockito.anyString());
		}
**/ 
	}

}
