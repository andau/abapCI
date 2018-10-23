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

public class ColoredProjectModelXmlTest {

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

		List<ColoredProject> coloredProjects = coloredProjectModelXml.getColoredProjects();
		assertEquals(0, coloredProjects.size());
		coloredProjectModelXml.addColoredProjectToXML("TestProject1",
				new Color(Display.getCurrent(), new RGB(0, 255, 0)), false);
		coloredProjects = coloredProjectModelXml.getColoredProjects();
		assertEquals(1, coloredProjects.size());

		coloredProjectModelXml.addColoredProjectToXML("TestProject2",
				new Color(Display.getCurrent(), new RGB(255, 0, 0)), false);
		coloredProjects = coloredProjectModelXml.getColoredProjects();
		assertEquals(2, coloredProjects.size());

		coloredProjectModelXml.removeColoredProject(
				new ColoredProject("TestProject2", new Color(Display.getCurrent(), new RGB(255, 0, 0))));
		coloredProjects = coloredProjectModelXml.getColoredProjects();
		assertEquals(1, coloredProjects.size());
	}
}
