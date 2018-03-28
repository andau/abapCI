package abapci.model;

import java.util.List;

import abapci.domain.ColoredProject;
import abapci.domain.UiColor;
import abapci.xml.ColoredProjectModelXml;

public class ColoredProjectModel {

	private ColoredProjectModelXml coloredProjectModelXml; 
	
	public ColoredProjectModel() {

		coloredProjectModelXml = new ColoredProjectModelXml();
	}

	public void saveColoredProjects(List<ColoredProject> coloredProjects) 
	{
		coloredProjectModelXml.clear(); 
		for(ColoredProject coloredProject : coloredProjects) 
		{
			coloredProjectModelXml.addColoredProjectToXML(coloredProject.getName(), coloredProject.getUiColor());					
		}
	}
	

	public UiColor getColorForProject(String projectName) {
	    return coloredProjectModelXml.getColorForProject(projectName); 
	}

	public List<ColoredProject> getColoredProjects() {
		return coloredProjectModelXml.getColoredProjects(); 
	}

	public void removeColoredProject(ColoredProject coloredProject) {
	    coloredProjectModelXml.removeColoredProject(coloredProject); 
	}

	public void addColoredProject(ColoredProject coloredProject) {
		coloredProjectModelXml.addColoredProjectToXML(coloredProject.getName(), coloredProject.getUiColor());							
	}
}
