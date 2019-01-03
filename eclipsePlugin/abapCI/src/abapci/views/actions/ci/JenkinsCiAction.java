package abapci.views.actions.ci;

import abapci.AbapCiPlugin;
import abapci.ci.presenter.ContinuousIntegrationPresenter;
import abapci.domain.AbapPackageTestState;
import abapci.handlers.JenkinsHandler;

public class JenkinsCiAction extends AbstractCiAction {

	public JenkinsCiAction(ContinuousIntegrationPresenter continuousIntegrationPresenter, String label,
			String tooltip) {
		this.setText(label);
		this.setToolTipText(tooltip);
		this.setImageDescriptor(AbapCiPlugin.getImageDescriptor("icons/jenkins.png"));
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
