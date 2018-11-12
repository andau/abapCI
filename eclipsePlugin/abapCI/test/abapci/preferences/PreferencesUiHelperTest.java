package abapci.preferences;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.junit.Test;

public class PreferencesUiHelperTest {

	private static final String TEST_HEADER_TEXT = "Test Header Text";
	Composite fieldEditorParent = new Shell();

	@Test
	public void testAddHeaderLabelWithSpaceBefore() {
		PreferencesUiHelper preferencesUiHelper = new PreferencesUiHelper();
		preferencesUiHelper.addHeaderLabelWithSpaceBefore(fieldEditorParent, TEST_HEADER_TEXT);
	}

}
