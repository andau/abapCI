package abapci.ci.atc.ui;

import org.eclipse.jface.viewers.ISelection;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import com.sap.adt.atc.ui.internal.launch.AtcLaunchShortcut;

public class AtcLaunchShortcutTest {

	ISelection selection = Mockito.mock(ISelection.class);

	@Before
	public void before() {
		/**
		 * The AtcLaunchShortcut class is not part of the export-package in the plugin
		 * com.sap.adt.atc.ui. To set it accessible the following line in the
		 * MANIFEST.MF has to be changed:
		 *
		 * Change the line:
		 *
		 * Export-Package: com.sap.adt.atc.ui;x-friends:="com.sap.adt.tm.model"
		 *
		 * with
		 *
		 * Export-Package: com.sap.adt.atc.ui;x-friends:="com.sap.adt.tm.model",
		 * com.sap.adt.atc.ui.internal.launch; x-friends:="com.sap.adt.atc,
		 * com.sap.adt.communication.resources, com.sap.adt.logging,
		 * com.sap.adt.project, com.sap.adt.projectexplorer.ui,
		 * com.sap.adt.ris.search.ui, com.sap.adt.tools.abapsource,
		 * com.sap.adt.tools.core"
		 *
		 * before activating the feature and the test
		 */
	}

	@Test
	@Ignore
	public void test() {
		final AtcLaunchShortcut cut = new AtcLaunchShortcut();
		cut.launch(selection, "mode");
	}

}
