package abapci.coloredProject.model;

import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import abapci.Exception.AbapCiColoredProjectFileParseException;
import abapci.coloredProject.samples.ColoredProjectTestSample;

public class ColoredProjectModelTest {

	ColoredProjectModelXml coloredProjectModelXml;

	ColoredProjectModel cut;

	@Before
	public void before() {
		coloredProjectModelXml = Mockito.mock(ColoredProjectModelXml.class);

		cut = new ColoredProjectModel(coloredProjectModelXml);
	}

	@Test
	public void testAddColoredProject() {

		ColoredProject redColoredProject = ColoredProjectTestSample.getGreenColoredProject();
		cut.addColoredProject(redColoredProject);
		Mockito.verify(coloredProjectModelXml).addColoredProjectToXML(redColoredProject.getName(),
				redColoredProject.getColor(), redColoredProject.isSuppressedColoring());

		ColoredProject suppressedColoredProject = ColoredProjectTestSample.getSuppressedColoredProject();
		cut.addColoredProject(suppressedColoredProject);
		Mockito.verify(coloredProjectModelXml).addColoredProjectToXML(suppressedColoredProject.getName(),
				suppressedColoredProject.getColor(), true);

	}

	@Test
	public void testRemoveColoredProject() throws AbapCiColoredProjectFileParseException {
		ColoredProject redColoredProject = ColoredProjectTestSample.getGreenColoredProject();
		cut.removeColoredProject(redColoredProject);
		Mockito.verify(coloredProjectModelXml).removeColoredProject(redColoredProject);
	}

	@Test
	public void testSaveColoredProjects() throws AbapCiColoredProjectFileParseException {
		ColoredProject sampleProject = ColoredProjectTestSample.getGreenColoredProject(); 
		List<ColoredProject> coloredProjects = Collections.singletonList(sampleProject); 
		cut.saveColoredProjects(coloredProjects);
		Mockito.verify(coloredProjectModelXml).clear();
		Mockito.verify(coloredProjectModelXml).addColoredProjectToXML(sampleProject.getName(), sampleProject.getColor(), sampleProject.isSuppressedColoring());
	}

	@Test
	public void testGetColoredProjects() throws AbapCiColoredProjectFileParseException {
		cut.getColoredProjects();
		Mockito.verify(coloredProjectModelXml).getColoredProjects();
	}

	@Test
	public void testGetColorForProject() throws AbapCiColoredProjectFileParseException {
		cut.getColorForProject(ColoredProjectTestSample.getGreenColoredProject().getName());
		Mockito.verify(coloredProjectModelXml)
				.getColorForProject(ColoredProjectTestSample.getGreenColoredProject().getName());
	}

}
