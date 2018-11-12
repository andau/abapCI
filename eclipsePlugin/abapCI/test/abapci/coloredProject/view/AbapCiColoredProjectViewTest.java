package abapci.coloredProject.view;

import static org.junit.Assert.assertFalse;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPartSite;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;

import abapci.AbapCiPluginHelper;
import abapci.coloredProject.presenter.ColoredProjectsPresenter;
import abapci.preferences.UiItemsTestHelper;

public class AbapCiColoredProjectViewTest {

	AbapCiColoredProjectView cut;

	Shell shell = new Shell();

	private final AbapCiPluginHelper abapCiPluginHelper = Mockito.mock(AbapCiPluginHelper.class);
	private final ColoredProjectsPresenter coloredProjectsPresenter = Mockito.mock(ColoredProjectsPresenter.class);
	private final IWorkbenchPartSite partSite = Mockito.mock(IWorkbenchPartSite.class);

	@Before
	public void before() {
		cut = new AbapCiColoredProjectView();
		Whitebox.setInternalState(cut, "abapCiPluginHelper", abapCiPluginHelper);
		Whitebox.setInternalState(cut, "partSite", partSite);

		Mockito.when(abapCiPluginHelper.getColoredProjectsPresenter()).thenReturn(coloredProjectsPresenter);

	}

	@Test(expected = NoClassDefFoundError.class)
	public void createPartControl() {
		cut.createPartControl(shell);
		assertFalse(UiItemsTestHelper.findDuplicates(shell.getChildren()));
	}

}
