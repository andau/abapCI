package abapci.coloredProject.presenter;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.swt.graphics.RGB;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;

import abapci.AbapCiPluginHelper;
import abapci.Exception.AbapCiColoredProjectFileParseException;
import abapci.coloredProject.model.ColoredProject;
import abapci.coloredProject.model.ColoredProjectModel;
import abapci.coloredProject.model.projectColor.DefaultEclipseProjectColor;
import abapci.coloredProject.model.projectColor.IProjectColor;
import abapci.coloredProject.model.projectColor.IProjectColorFactory;
import abapci.coloredProject.model.projectColor.ProjectColorFactory;
import abapci.coloredProject.model.projectColor.StandardProjectColor;
import abapci.coloredProject.samples.ColoredProjectTestSample;
import abapci.coloredProject.view.AbapCiColoredProjectView;

public class ColoredProjectsPresenterTest {

	private static final String TEST_ERRORMESSAGE = "Testmessage";

	private static final String TESTPROJECT_1_NAME = "TESTPROJECT_1";

	private static final RGB COLOR_RED = new RGB(255, 0, 0);

	ColoredProjectsPresenter cutColoredProjectPresenter;
	ColoredProject sampleColoredProject;
	IProjectColor sampleProjectColor;

	ColoredProjectModel coloredProjectModel;
	AbapCiColoredProjectView coloredProjectView;
	AbapCiPluginHelper abapCiPluginHelper = Mockito.mock(AbapCiPluginHelper.class);

	@Before
	public void before() throws AbapCiColoredProjectFileParseException {
		coloredProjectModel = Mockito.mock(ColoredProjectModel.class);
		coloredProjectView = Mockito.mock(AbapCiColoredProjectView.class);

		Mockito.when(coloredProjectModel.getColoredProjects()).thenReturn(new ArrayList<ColoredProject>());

		IProjectColorFactory projectColorFactory = new ProjectColorFactory();
		sampleProjectColor = ColoredProjectTestSample.getGreenProjectColor();
		projectColorFactory.create(COLOR_RED);
		sampleColoredProject = new ColoredProject(TESTPROJECT_1_NAME, sampleProjectColor);

		cutColoredProjectPresenter = new ColoredProjectsPresenter(coloredProjectView, coloredProjectModel);
		Whitebox.setInternalState(cutColoredProjectPresenter, "abapCiPluginHelper", abapCiPluginHelper);
	}

	@Test
	public void testAddColoredProjectStandardTest() throws AbapCiColoredProjectFileParseException {
		cutColoredProjectPresenter.addColoredProject(sampleColoredProject);
		Mockito.verify(coloredProjectModel).addColoredProject(sampleColoredProject);
	}

	@Test
	public void testAddExistingColoredProjectTest() throws AbapCiColoredProjectFileParseException {
		List<ColoredProject> coloredProjects = Collections.singletonList(sampleColoredProject);
		Mockito.when(coloredProjectModel.getColoredProjects()).thenReturn(coloredProjects);

		cutColoredProjectPresenter.addColoredProject(sampleColoredProject);
		Mockito.verify(coloredProjectModel).removeColoredProject(sampleColoredProject);
		Mockito.verify(coloredProjectModel).addColoredProject(sampleColoredProject);
		Mockito.verify(abapCiPluginHelper).resetWorkspaceColorConfiguration();
	}

	@Test
	public void testAddExistingColoredProjectWithExceptionTest() throws AbapCiColoredProjectFileParseException {
		Mockito.when(coloredProjectModel.getColoredProjects()).thenThrow(new AbapCiColoredProjectFileParseException());

		cutColoredProjectPresenter.addColoredProject(sampleColoredProject);
	}

	@Test
	public void testRemoveColoredProject() throws AbapCiColoredProjectFileParseException {
		List<ColoredProject> coloredProjects = Collections.singletonList(sampleColoredProject);
		Mockito.when(coloredProjectModel.getColoredProjects()).thenReturn(coloredProjects);

		cutColoredProjectPresenter.removeColoredProject(sampleColoredProject);
		Mockito.verify(coloredProjectModel).removeColoredProject(sampleColoredProject);
		Mockito.verify(abapCiPluginHelper).resetWorkspaceColorConfiguration();
	}

	@Test
	public void testGetProjectColor() throws AbapCiColoredProjectFileParseException {
		Mockito.when(coloredProjectModel.getColorForProject(sampleColoredProject.getName()))
				.thenReturn(ColoredProjectTestSample.getGreenProjectColor());

		IProjectColor projectColor = cutColoredProjectPresenter.getProjectColor(sampleColoredProject.getName());
		assertTrue(projectColor instanceof StandardProjectColor);
		Mockito.verify(coloredProjectModel).getColorForProject(sampleColoredProject.getName());

	}

	@Test
	public void testGetProjectColorNull() throws AbapCiColoredProjectFileParseException {
		Mockito.when(coloredProjectModel.getColorForProject(sampleColoredProject.getName())).thenReturn(null);

		IProjectColor defaultProjectColor = cutColoredProjectPresenter.getProjectColor(sampleColoredProject.getName());
		assertTrue(defaultProjectColor instanceof DefaultEclipseProjectColor);
		Mockito.verify(coloredProjectModel).getColorForProject(sampleColoredProject.getName());
	}

	@Test
	public void testSetStatusMessage() throws AbapCiColoredProjectFileParseException {

		// Color errorMessageColor = new Color(Display.getDefault(), new RGB(255,0,0));
		// Color warningMessageColor = new Color(Display.getDefault(), new
		// RGB(255,0,0));

		cutColoredProjectPresenter.setStatusErrorMessage(TEST_ERRORMESSAGE);
		// TODO verify asnychrous calls
		// Mockito.verify(coloredProjectView).setStatusLabelText(TEST_ERRORMESSAGE);
		// Mockito.verify(coloredProjectView).setStatusLabelForeground(errorMessageColor);

	}

}
