package abapci;

import abapci.coloredProject.presenter.ColoredProjectsPresenter;

public class AbapCiPluginHelper {

	public ColoredProjectsPresenter getColoredProjectsPresenter() {
		return AbapCiPlugin.getDefault().getColoredProjectsPresenter(); 
	}

}
