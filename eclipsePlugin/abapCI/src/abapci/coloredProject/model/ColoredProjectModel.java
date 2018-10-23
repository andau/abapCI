package abapci.coloredProject.model;

import java.util.List;

import org.eclipse.swt.graphics.Color;

import abapci.Exception.AbapCiColoredProjectFileParseException;
import abapci.Exception.ProjectColorNotDefinedException;

public class ColoredProjectModel {

	private ColoredProjectModelXml coloredProjectModelXml;

	public ColoredProjectModel() {

		coloredProjectModelXml = new ColoredProjectModelXml();
	}

	public void saveColoredProjects(List<ColoredProject> coloredProjects) {
		coloredProjectModelXml.clear();
		for (ColoredProject coloredProject : coloredProjects) {
			coloredProjectModelXml.addColoredProjectToXML(coloredProject.getName(), coloredProject.getColor(),
					coloredProject.suppressColoring());
		}
	}

	public Color getColorForProject(String projectName)
			throws AbapCiColoredProjectFileParseException, ProjectColorNotDefinedException {
		return coloredProjectModelXml.getColorForProject(projectName);
	}

	public List<ColoredProject> getColoredProjects() throws AbapCiColoredProjectFileParseException {
		return coloredProjectModelXml.getColoredProjects();
	}

	public void removeColoredProject(ColoredProject coloredProject) throws AbapCiColoredProjectFileParseException {
		coloredProjectModelXml.removeColoredProject(coloredProject);
	}

	public void addColoredProject(ColoredProject coloredProject) {
		coloredProjectModelXml.addColoredProjectToXML(coloredProject.getName(), coloredProject.getColor(),
				coloredProject.suppressColoring());
	}
}
