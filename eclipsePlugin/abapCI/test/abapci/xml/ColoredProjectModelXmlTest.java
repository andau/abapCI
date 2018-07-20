package abapci.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.eclipse.core.runtime.Path;
import org.junit.Ignore;
import org.junit.Test;

import abapci.domain.ColoredProject;
import abapci.domain.UiColor;

public class ColoredProjectModelXmlTest {

	@Test
	@Ignore
	public void StandardColorProjectModelTest() {
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
		coloredProjectModelXml.addColoredProjectToXML("TestProject1", UiColor.GREEN);
		coloredProjects = coloredProjectModelXml.getColoredProjects();
		assertEquals(1, coloredProjects.size());

		coloredProjectModelXml.addColoredProjectToXML("TestProject2", UiColor.RED);
		coloredProjects = coloredProjectModelXml.getColoredProjects();
		assertEquals(2, coloredProjects.size());

		coloredProjectModelXml.removeColoredProject(new ColoredProject("TestProject2", UiColor.RED));
		coloredProjects = coloredProjectModelXml.getColoredProjects();
		assertEquals(1, coloredProjects.size());
	}
}
