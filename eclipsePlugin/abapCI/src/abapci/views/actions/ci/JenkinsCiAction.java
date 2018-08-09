package abapci.views.actions.ci;

import abapci.AbapCiPlugin;
import abapci.domain.AbapPackageTestState;
import abapci.handlers.JenkinsHandler;
import abapci.presenter.ContinuousIntegrationPresenter;

public class JenkinsCiAction extends AbstractCiAction {

	public JenkinsCiAction(ContinuousIntegrationPresenter continuousIntegrationPresenter, String label,
			String tooltip) {
		this.setText(label);
		this.setToolTipText(tooltip);
		this.setImageDescriptor(AbapCiPlugin.getImageDescriptor("icons/jenkins.ico"));
		this.continuousIntegrationPresenter = continuousIntegrationPresenter;
	}

	@Override
	public void run() {

		for (AbapPackageTestState abapPackageTestState : getSelectedAbapPackageTestStates()) {

			try {
				new JenkinsHandler().execute(abapPackageTestState.getPackageName());
			} catch (Exception ex) {
				// TODO
			}

			continuousIntegrationPresenter.updateViewsAsync();

		}
	}
}
