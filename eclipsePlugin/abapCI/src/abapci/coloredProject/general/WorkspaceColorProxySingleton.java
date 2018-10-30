package abapci.coloredProject.general;

import java.util.HashMap;
import java.util.List;

import org.eclipse.core.resources.IProject;

import abapci.Exception.AbapCiColoredProjectFileParseException;
import abapci.coloredProject.model.ColoredProject;
import abapci.coloredProject.model.ColoredProjectModel;

public class WorkspaceColorProxySingleton {

	private static WorkspaceColorProxySingleton instance;
	private HashMap<String, DisplayColor> coloredProjects;

	private WorkspaceColorProxySingleton(boolean initialize) throws AbapCiColoredProjectFileParseException {
		coloredProjects = new HashMap<String, DisplayColor>();

		if (initialize) {
			initialize();
		}
	}

	private void initialize() throws AbapCiColoredProjectFileParseException {

		ColoredProjectModel model = new ColoredProjectModel();
		List<ColoredProject> coloredProjects = model.getColoredProjects();
		for (ColoredProject coloredProject : coloredProjects) {
			addOrUpdate(coloredProject);
		}

	}

	public static synchronized WorkspaceColorProxySingleton getInstance()
			throws AbapCiColoredProjectFileParseException {
		if (instance == null) {
			instance = new WorkspaceColorProxySingleton(true);
		}
		return instance;
	}

	public static WorkspaceColorProxySingleton getEmptyInstance() throws AbapCiColoredProjectFileParseException {
		if (instance == null) {
			instance = new WorkspaceColorProxySingleton(false);
		}
		return instance;
	}

	public void addOrUpdate(ColoredProject coloredProject) {
		if (coloredProjects.containsKey(coloredProject.getName())) {
			coloredProjects.remove(coloredProject.getName());
		}
		coloredProjects.put(coloredProject.getName(),
				new DisplayColor(coloredProject.getColor(), coloredProject.isSuppressedColoring()));

	}

	public int getNumColoredProjects() {
		return coloredProjects.size();
	}

	public DisplayColor getColoring(IProject project) {

		DisplayColor projectColor = null;

		if (project != null) {
			projectColor = coloredProjects.containsKey(project.getName()) ? coloredProjects.get(project.getName())
					: null;
		}

		return projectColor;
	}

	public static void destroyInstance() {
		instance = null;
	}

	public boolean isConfigured(IProject project) {
		return coloredProjects.containsKey(project.getName());
	}

}