package abapci.preferences;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

public class ColoredProjectsPreferencesTest {

	private static final int MINIMUM_EXPECTED_UI_ITEMS = 10;
	Composite fieldEditorParent = new Shell();

	@Test
	public void testColoredProjectsCreateFieldEditors() {
		ColoredProjectsPreferences cut = new ColoredProjectsPreferences();
		Whitebox.setInternalState(cut, "fieldEditorParent", fieldEditorParent);
		cut.createFieldEditors();
		assertTrue(fieldEditorParent.getChildren().length > MINIMUM_EXPECTED_UI_ITEMS);
		assertFalse(UiItemsTestHelper.findDuplicates(fieldEditorParent.getChildren()));
	}

}
