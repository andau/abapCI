package abapci.coloredProject.view;

import static org.junit.Assert.assertFalse;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;

import abapci.AbapCiPluginHelper;
import abapci.coloredProject.presenter.ColoredProjectsPresenter;
import abapci.preferences.UiItemsTestHelper;
import abapci.utils.ResourcePluginHelper;

public class AddOrUpdateColoredProjectPageTest {

	AddOrUpdateColoredProjectPage cut;

	Shell shell = new Shell();

	private final AbapCiPluginHelper abapCiPluginHelper = Mockito.mock(AbapCiPluginHelper.class);
	private final ColoredProjectsPresenter coloredProjectsPresenter = Mockito.mock(ColoredProjectsPresenter.class);
	ResourcePluginHelper resourcePluginHelper = Mockito.mock(ResourcePluginHelper.class);

	@Before
	public void before() {
		cut = new AddOrUpdateColoredProjectPage(shell, coloredProjectsPresenter, null, false);
		Whitebox.setInternalState(cut, "resourcePluginHelper", resourcePluginHelper);
		Mockito.when(abapCiPluginHelper.getColoredProjectsPresenter()).thenReturn(coloredProjectsPresenter);
		Mockito.when(resourcePluginHelper.getProjects()).thenReturn(new IProject[] {});

	}

	@Test(expected = NoClassDefFoundError.class)
	public void testCreateDialogArea() {
		cut.createDialogArea(shell);
		assertFalse(UiItemsTestHelper.findDuplicates(shell.getChildren()));
	}

}
