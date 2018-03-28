package abapci.presenter;

import abapci.domain.ColoredProject;
import abapci.model.ColoredProjectModel;
import abapci.views.AbapCiColoredProjectView;

public class ColoredProjectsPresenter {

	private AbapCiColoredProjectView view;
	private ColoredProjectModel model;

	public ColoredProjectsPresenter(AbapCiColoredProjectView abapCiColoredProjectView,
			ColoredProjectModel coloredProjectModel) {
		this.view = abapCiColoredProjectView;
		this.model = coloredProjectModel;
		setViewerInput(); 
	}

	public void removeColoredProject(ColoredProject coloredProject) {
		model.removeColoredProject(coloredProject); 
		
	}

	public void setViewerInput() {
		view.setViewerInput(model.getColoredProjects());
		
	}

	public void addColoredProject(ColoredProject coloredProject) {
		model.addColoredProject(coloredProject); 
		
	}

}
