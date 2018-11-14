package abapci.ci.atc.ui;

import org.eclipse.jface.viewers.ISelection;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.sap.adt.atc.ui.internal.launch.AtcLaunchShortcut;

public class AtcLaunchShortcutTest {

	ISelection selection = Mockito.mock(ISelection.class);

	@Test
	public void test() {
		AtcLaunchShortcut cut = new AtcLaunchShortcut();
		cut.launch(selection, "mode");
	}

}
