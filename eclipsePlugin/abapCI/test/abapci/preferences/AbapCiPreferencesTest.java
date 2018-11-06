package abapci.preferences;

import org.eclipse.swt.widgets.Composite;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;

public class AbapCiPreferencesTest {

	Composite fieldEditorParent = Mockito.mock(Composite.class); 
	@Test
	@Ignore 
	public void testCreateFieldEditors() {
		AbapCiPreferences cut = new AbapCiPreferences(); 
		//TODO mocking the composite parent 
		Whitebox.setInternalState(cut, "fieldEditorParent" , fieldEditorParent);
		cut.createFieldEditors(); 
	}

}
