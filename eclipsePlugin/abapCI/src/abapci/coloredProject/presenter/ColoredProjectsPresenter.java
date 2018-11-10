package abapci.coloredProject.presenter;

import java.util.Comparator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

import abapci.AbapCiPluginHelper;
import abapci.Exception.AbapCiColoredProjectFileParseException;
import abapci.coloredProject.model.ColoredProject;
import abapci.coloredProject.model.ColoredProjectModel;
import abapci.coloredProject.model.projectColor.DefaultEclipseProjectColor;
import abapci.coloredProject.model.projectColor.IProjectColor;
import abapci.coloredProject.view.AbapCiColoredProjectView;

public class ColoredProjectsPresenter {

	private static final String PARSING_COLORED_PROJECT_XML_FAILED_MESSAGE = "Parsing the coloredProjectModel.xml file failed, for help see  https://github.com/andau/abapCI/issues/4";
	private AbapCiColoredProjectView view;
	private final ColoredProjectModel model;
	private final AbapCiPluginHelper abapCiPluginHelper;

	public ColoredProjectsPresenter(AbapCiColoredProjectView abapCiColoredProjectView,
			ColoredProjectModel coloredProjectModel) {
		this.view = abapCiColoredProjectView;
		this.model = coloredProjectModel;
		this.abapCiPluginHelper = new AbapCiPluginHelper();
		setViewerInput();
	}

	public void addColoredProject(ColoredProject coloredProject) {
		try {
			if (coloredProject != null && model.getColoredProjects().stream()
					.anyMatch(item -> item.getName().equals(coloredProject.getName()))) {
				model.removeColoredProject(coloredProject);
			}

			model.addColoredProject(coloredProject);
			abapCiPluginHelper.resetWorkspaceColorConfiguration();

			setViewerInput();
		} catch (AbapCiColoredProjectFileParseException e) {
			setStatusMessage(String.format("Parsing error of XML file when handling the coloring for project %s",
					coloredProject.getName()), new Color(Display.getCurrent(), new RGB(255, 0, 0)));
		} catch (Exception ex) {
			setStatusMessage(
					String.format("General error when trying to set the coloring for project %s, errormessage %s",
							coloredProject.getName(), ex.getMessage()),
					new Color(Display.getCurrent(), new RGB(255, 0, 0)));
		}

	}

	public void removeColoredProject(ColoredProject coloredProject) {
		try {
			model.removeColoredProject(coloredProject);
			abapCiPluginHelper.resetWorkspaceColorConfiguration();
			setViewerInput();
		} catch (AbapCiColoredProjectFileParseException e) {
			setStatusMessageParsingColoredProjectXmlFailed();
			e.printStackTrace();
		}
	}

	public void setView(AbapCiColoredProjectView view) {
		this.view = view;
	}

	public void setViewerInput() {
		try {
			if (view != null) {

				Comparator<ColoredProject> compareByName = (o1, o2) -> o1.getName().compareTo(o2.getName());

				List<ColoredProject> coloredProjects = model.getColoredProjects();
				coloredProjects.sort(compareByName);
				view.setViewerInput(coloredProjects);
			}
		} catch (AbapCiColoredProjectFileParseException e) {
			setStatusMessageParsingColoredProjectXmlFailed();
			e.printStackTrace();
		}

	}

	private void setStatusMessageParsingColoredProjectXmlFailed() {
		try {
			if (view != null) {
				view.setStatusLabelText(PARSING_COLORED_PROJECT_XML_FAILED_MESSAGE);
				org.eclipse.swt.graphics.Color red = Display.getCurrent().getSystemColor(SWT.COLOR_RED);
				view.setStatusLabelForeground(red);
			}
		} catch (Exception ex) {
			// if the status message can not be set we will ignore this
			ex.printStackTrace();

		}
	}

	public void setStatusMessage(String message) {
		setStatusMessage(message, new Color(Display.getCurrent(), new RGB(0, 0, 0)));
	}

	public void setStatusErrorMessage(String message) {
		setStatusMessage(message, new Color(Display.getCurrent(), new RGB(255, 0, 0)));
	}

	public void setStatusMessage(String message, Color color) {
		Runnable task = () -> setStatusMessageInternal(message, color);
		Display.getDefault().asyncExec(task);
	}

	private void setStatusMessageInternal(String message, Color color) {
		try {
			if (view != null) {
				view.setStatusLabelText(message);
				view.setStatusLabelForeground(color);
			}
		} catch (Exception ex) {
			// if the status message can not be set we will ignore this
			ex.printStackTrace();

		}
	}

	public IProjectColor getProjectColor(String projectName) throws AbapCiColoredProjectFileParseException {
		IProjectColor projectColor = model.getColorForProject(projectName);
		return projectColor == null ? new DefaultEclipseProjectColor() : projectColor;
	}
}
