package abapci.coloredProject.model;

import static org.junit.Assert.*;

import org.eclipse.swt.graphics.RGB;
import org.junit.Test;

import abapci.coloredProject.model.projectColor.DefaultEclipseProjectColor;
import abapci.coloredProject.model.projectColor.IProjectColor;
import abapci.coloredProject.model.projectColor.IProjectColorFactory;
import abapci.coloredProject.model.projectColor.ProjectColorFactory;
import abapci.coloredProject.samples.ColoredProjectTestSample;

public class ColoredProjectTest {

	private static final String TEST_PROJECT = "TEST_PROJECT";
	private static final RGB COLOR_RED = new RGB(255, 0, 0);

	@Test
	public void standardProjectColorTest() {
		IProjectColor projectColor = ColoredProjectTestSample.getRedProjectColor(); 
		ColoredProject coloredProject = new ColoredProject(TEST_PROJECT, projectColor, false);
		assertEquals(TEST_PROJECT, coloredProject.getName());
		assertEquals(COLOR_RED, coloredProject.getColor().getRGB());
		assertEquals(false, coloredProject.isSuppressedColoring()); 
	}

	@Test
	public void standardReducedProjectColorTest() {
		IProjectColorFactory projectColorFactory = new ProjectColorFactory();
		IProjectColor projectColor = projectColorFactory.create(COLOR_RED);
		ColoredProject coloredProject = new ColoredProject(TEST_PROJECT, projectColor);
		assertEquals(TEST_PROJECT, coloredProject.getName());
		assertEquals(COLOR_RED, coloredProject.getColor().getRGB());
		assertEquals(false, coloredProject.isSuppressedColoring()); 
	}

	@Test
	public void defaultEclipseProjectColorTest() {
		ColoredProject coloredProject = new ColoredProject(TEST_PROJECT, new DefaultEclipseProjectColor(), false);
		assertEquals(TEST_PROJECT, coloredProject.getName());
		assertEquals(null, coloredProject.getColor());
	}

}
