package abapci.abapgit.handlers;

import static org.junit.Assert.assertNotNull;

import org.eclipse.ui.IWorkbench;
import org.junit.Before;
import org.junit.Test;

import com.sap.adt.sapgui.ui.editors.IAdtSapGuiEditorUtility;

public class AbapGitHandlerHelperTest {

	AbapGitHandlerHelper cut;

	@Before
	public void testBefore() {
		cut = new AbapGitHandlerHelper();
	}

	@Test(expected = NullPointerException.class)
	public void testGetAbapGitFeature() {
		cut.getAbapGitFeature();
	}

	@Test
	public void testSapGuiEditorUtility() {
		final IAdtSapGuiEditorUtility sapGuiEditorUtility = cut.getSapGuiEditorUtility();
		assertNotNull(sapGuiEditorUtility);
	}

	@Test(expected = NoClassDefFoundError.class)
	public void testWorkbench() {
		final IWorkbench workbench = cut.getWorkbench();
		assertNotNull(workbench);
	}

}
