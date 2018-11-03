package abapci;

import org.eclipse.jface.preference.IPreferenceStore;

import abapci.coloredProject.general.IStatusBarWidget;
import abapci.coloredProject.presenter.ColoredProjectsPresenter;

public class AbapCiPluginHelper {

	public ColoredProjectsPresenter getColoredProjectsPresenter() {
		return AbapCiPlugin.getDefault().getColoredProjectsPresenter(); 
	}

	public IStatusBarWidget getStatusBarWidget() {
		return AbapCiPlugin.getDefault().getStatusBarWidget(); 
	}

	public IPreferenceStore getPreferenceStore() {
		return AbapCiPlugin.getDefault().getPreferenceStore(); 
	}

}
