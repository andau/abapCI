package abapci.presenter;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;

import abapci.Exception.AbapCiColoredProjectFileParseException;
import abapci.domain.ColoredProject;
import abapci.model.ColoredProjectModel;
import abapci.views.AbapCiColoredProjectView;

public class ColoredProjectsPresenter {

	private static final String PARSING_COLORED_PROJECT_XML_FAILED_MESSAGE = "Parsing the coloredProjectModel.xml file failed, for help see  https://github.com/andau/abapCI/issues/4";
	private AbapCiColoredProjectView view;
	private ColoredProjectModel model;

	public ColoredProjectsPresenter(AbapCiColoredProjectView abapCiColoredProjectView,
			ColoredProjectModel coloredProjectModel) {
		this.view = abapCiColoredProjectView;
		this.model = coloredProjectModel;
		setViewerInput();
	}

	public void removeColoredProject(ColoredProject coloredProject) {
		try {
			model.removeColoredProject(coloredProject);
			setViewerInput();
		} catch (AbapCiColoredProjectFileParseException e) {
			setStatusMessageParsingColoredProjectXmlFailed();
			e.printStackTrace();
		}
	}

	public void setViewerInput() {
		try {
			view.setViewerInput(model.getColoredProjects());
		} catch (AbapCiColoredProjectFileParseException e) {
			setStatusMessageParsingColoredProjectXmlFailed();
			e.printStackTrace();
		}

	}

	private void setStatusMessageParsingColoredProjectXmlFailed() {
		try {
			view.statusLabel.setText(PARSING_COLORED_PROJECT_XML_FAILED_MESSAGE);
			org.eclipse.swt.graphics.Color red = Display.getCurrent().getSystemColor(SWT.COLOR_RED);
			view.statusLabel.setForeground(red);
		} catch (Exception ex) {
			// if the status message can not be set we will ignore this
			ex.printStackTrace();

		}
	}

	public void addColoredProject(ColoredProject coloredProject) {
		model.addColoredProject(coloredProject);
		setViewerInput();

	}

}
