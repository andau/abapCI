package abapci.coloredProject.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.eclipse.core.runtime.Path;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.junit.Ignore;
import org.junit.Test;

import abapci.Exception.AbapCiColoredProjectFileParseException;
import abapci.coloredProject.model.projectColor.IProjectColor;
import abapci.coloredProject.model.projectColor.IProjectColorFactory;
import abapci.coloredProject.model.projectColor.ProjectColorFactory;

public class ColoredProjectModelXmlTest {

	private static final String TEST_PROJECT_1 = "TestProject1";
	private static final String TEST_PROJECT_2 = "TestProject2";
	private static final RGB COLOR_RED = new RGB(0, 255, 0);

	@Test
	@Ignore
	public void StandardColorProjectModelTest() throws AbapCiColoredProjectFileParseException {
		ColoredProjectModelXml coloredProjectModelXml = new ColoredProjectModelXml(new Path("C:/temp/"),
				"coloredProjectTestfile.xml");

		if (!coloredProjectModelXml.fileExists()) {
			coloredProjectModelXml.createFile();
		} else {
			coloredProjectModelXml.clear();
		}

		assertTrue(coloredProjectModelXml.fileExists());

		IProjectColorFactory projectColorFactory = new ProjectColorFactory();
		IProjectColor projectColor = projectColorFactory.create(COLOR_RED);

		List<ColoredProject> coloredProjects = coloredProjectModelXml.getColoredProjects();
		assertEquals(0, coloredProjects.size());
		coloredProjectModelXml.addColoredProjectToXML(TEST_PROJECT_1, new Color(Display.getCurrent(), COLOR_RED),
				false);
		coloredProjects = coloredProjectModelXml.getColoredProjects();
		assertEquals(1, coloredProjects.size());

		coloredProjectModelXml.addColoredProjectToXML(TEST_PROJECT_2,
				new Color(Display.getCurrent(), new RGB(255, 0, 0)), false);
		coloredProjects = coloredProjectModelXml.getColoredProjects();
		assertEquals(2, coloredProjects.size());

		coloredProjectModelXml.removeColoredProject(new ColoredProject(TEST_PROJECT_2, projectColor));
		coloredProjects = coloredProjectModelXml.getColoredProjects();
		assertEquals(1, coloredProjects.size());
	}
}
