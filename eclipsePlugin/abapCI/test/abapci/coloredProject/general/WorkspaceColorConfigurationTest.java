package abapci.coloredProject.general;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.eclipse.core.resources.IProject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import abapci.Exception.AbapCiColoredProjectFileParseException;
import abapci.coloredProject.config.IColoringConfig;
import abapci.coloredProject.config.IColoringConfigFactory;
import abapci.coloredProject.config.ProjectColoringConfigFactory;
import abapci.coloredProject.config.TestStateColoringConfigFactory;
import abapci.coloredProject.model.ColoredProject;
import abapci.coloredProject.model.projectColor.DefaultEclipseProjectColor;
import abapci.coloredProject.samples.ColoredProjectTestSample;
import abapci.feature.SourceCodeVisualisationFeature;
import abapci.feature.activeFeature.ColoredProjectFeature;
import abapci.utils.StringUtils;

public class WorkspaceColorConfigurationTest {

	IProject project = Mockito.mock(IProject.class);
	WorkspaceColorConfiguration cut;

	ColoredProject greenColoredProject;
	ColoredProject blueColoredProject;

	SourceCodeVisualisationFeature sourceCodeVisualisationFeature;
	ColoredProjectFeature coloredProjectFeature;
	IColoringConfigFactory testStateColoringConfigFactory;
	IColoringConfigFactory projectColoringConfigFactory;

	@Before
	public void before() throws AbapCiColoredProjectFileParseException {
		cut = new WorkspaceColorConfiguration(false);

		Mockito.when(project.getName()).thenReturn(ColoredProjectTestSample.getGreenColoredProject().getName());

		greenColoredProject = ColoredProjectTestSample.getGreenColoredProject();
		blueColoredProject = ColoredProjectTestSample.getBlueColoredProject();

		sourceCodeVisualisationFeature = new SourceCodeVisualisationFeature();

		coloredProjectFeature = new ColoredProjectFeature();
		testStateColoringConfigFactory = new TestStateColoringConfigFactory(sourceCodeVisualisationFeature);
		projectColoringConfigFactory = new ProjectColoringConfigFactory(coloredProjectFeature);
	}

	@Test
	public void testWorkspaceColorConfigurationInitialisation() throws AbapCiColoredProjectFileParseException {

		assertEquals(0, cut.getNumColoredProjects());
		assertNotNull(cut.getColoring(project));
		assertTrue(cut.getColoring(project).getStatusBarColor() instanceof DefaultEclipseProjectColor);
		assertEquals(false, cut.isConfigured(project));

	}

	@Test
	public void testColoringOfTestStateOnly() throws AbapCiColoredProjectFileParseException {

		sourceCodeVisualisationFeature.setChangeStatusBarBackgroundColorEnabled(true);
		IColoringConfig coloredProjectConfig = testStateColoringConfigFactory.create(greenColoredProject);
		Mockito.when(project.getName()).thenReturn(greenColoredProject.getName());
		assertEquals(StringUtils.EMPTY, cut.getTestStateOutput(project));

		cut.addOrUpdateTestStateColoring(coloredProjectConfig, "Testoutput");

		assertEquals("Testoutput", cut.getTestStateOutput(project));

		assertEquals(1, cut.getNumColoredProjects());
		assertEquals(true, cut.isConfigured(project));
		assertEquals(greenColoredProject.getColor().getRGB(),
				cut.getColoring(project).getStatusBarColor().getColor().getRGB());
	}

	@Test
	public void testColoringOfProjectAndTestState() throws AbapCiColoredProjectFileParseException {

		sourceCodeVisualisationFeature.setChangeStatusBarBackgroundColorEnabled(true);
		IColoringConfig testStateColoringConfig = testStateColoringConfigFactory.create(greenColoredProject);
		cut.addOrUpdateTestStateColoring(testStateColoringConfig, StringUtils.EMPTY);

		coloredProjectFeature.setTitleIconActive(true);
		IColoringConfig projectColoringConfig = projectColoringConfigFactory.create(blueColoredProject);
		cut.addOrUpdateProjectColoringConfig(projectColoringConfig);

		assertEquals(1, cut.getNumColoredProjects());
		assertEquals(true, cut.isConfigured(project));
		assertEquals(greenColoredProject.getColor().getRGB(),
				cut.getColoring(project).getStatusBarColor().getColor().getRGB());
		assertEquals(blueColoredProject.getColor().getRGB(),
				cut.getColoring(project).getTitleIconColor().getColor().getRGB());

	}

	@Test
	public void testTestStateOverridesProjectColoringConfig() throws AbapCiColoredProjectFileParseException {

		coloredProjectFeature.setChangeStatusBarActive(true);
		IColoringConfig projectColoringConfig = projectColoringConfigFactory.create(blueColoredProject);
		cut.addOrUpdateProjectColoringConfig(projectColoringConfig);

		sourceCodeVisualisationFeature.setChangeStatusBarBackgroundColorEnabled(true);
		IColoringConfig testStateColoringConfig = testStateColoringConfigFactory.create(greenColoredProject);
		cut.addOrUpdateTestStateColoring(testStateColoringConfig, StringUtils.EMPTY);

		assertEquals(greenColoredProject.getColor().getRGB(),
				cut.getColoring(project).getStatusBarColor().getColor().getRGB());

	}

	@Test
	public void testStatusAndStatusWidgetColoredEqual() throws AbapCiColoredProjectFileParseException {

		coloredProjectFeature.setChangeStatusBarActive(true);
		IColoringConfig projectColoringConfig = projectColoringConfigFactory.create(blueColoredProject);
		cut.addOrUpdateProjectColoringConfig(projectColoringConfig);

		assertEquals(blueColoredProject.getColor().getRGB(),
				cut.getColoring(project).getStatusBarColor().getColor().getRGB());
		assertEquals(blueColoredProject.getColor().getRGB(),
				cut.getColoring(project).getStatusWidgetBackgroundColor().getColor().getRGB());

	}

}
