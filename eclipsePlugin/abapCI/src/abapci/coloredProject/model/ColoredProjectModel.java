package abapci.coloredProject.model;

import java.util.List;

import abapci.Exception.AbapCiColoredProjectFileParseException;
import abapci.coloredProject.model.projectColor.IProjectColor;

public class ColoredProjectModel {

	private ColoredProjectModelXml coloredProjectModelXml;

	public ColoredProjectModel() {

		this.coloredProjectModelXml = new ColoredProjectModelXml();
	}
	public ColoredProjectModel(ColoredProjectModelXml coloredProjectModelXml) {

		this.coloredProjectModelXml = coloredProjectModelXml;
	}

	public void saveColoredProjects(List<ColoredProject> coloredProjects) {
		coloredProjectModelXml.clear();
		for (ColoredProject coloredProject : coloredProjects) {
			coloredProjectModelXml.addColoredProjectToXML(coloredProject.getName(), coloredProject.getColor(),
					coloredProject.isSuppressedColoring());
		}
	}

	public IProjectColor getColorForProject(String projectName)
			throws AbapCiColoredProjectFileParseException {
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
				coloredProject.isSuppressedColoring());
	}
}
