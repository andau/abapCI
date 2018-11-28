package abapci.views.actions.ci;

import abapci.AbapCiPlugin;
import abapci.ci.presenter.ContinuousIntegrationPresenter;
import abapci.handlers.AbapGitHandler;

public class AbapGitCiAction extends AbstractCiAction {

	private static final String ICONS_GIT = "icons/git.ico";

	public AbapGitCiAction(ContinuousIntegrationPresenter continuousIntegrationPresenter, String label,
			String tooltip) {
		this.setText(label);
		this.setToolTipText(tooltip);
		this.setImageDescriptor(AbapCiPlugin.getImageDescriptor(ICONS_GIT));
		this.continuousIntegrationPresenter = continuousIntegrationPresenter;
	}

	@Override
	public void run() {
		new AbapGitHandler().execute("", "");
	}

}
