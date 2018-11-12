package abapci;

import org.eclipse.jface.preference.IPreferenceStore;

import abapci.Exception.AbapCiColoredProjectFileParseException;
import abapci.ci.presenter.ContinuousIntegrationPresenter;
import abapci.coloredProject.general.IStatusBarWidget;
import abapci.coloredProject.general.WorkspaceColorConfiguration;
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

	public void resetWorkspaceColorConfiguration() throws AbapCiColoredProjectFileParseException {
		AbapCiPlugin.resetWorkspaceColorConfiguration();

	}

	public WorkspaceColorConfiguration getWorkspaceColorConfiguration() {
		// TODO Auto-generated method stub
		return AbapCiPlugin.getWorkspaceColorConfiguration();
	}

	public ContinuousIntegrationPresenter getContinousIntegrationPresenter() {
		// TODO Auto-generated method stub
		return AbapCiPlugin.getContinuousIntegrationPresenter();
	}

}
