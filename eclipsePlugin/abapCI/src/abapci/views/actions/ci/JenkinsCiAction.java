package abapci.views.actions.ci;

import java.util.Iterator;
import java.util.Map.Entry;

import abapci.AbapCiPlugin;
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

		for (Iterator<Entry<String, String>> iter = getSelectedPackages().entrySet().iterator(); iter.hasNext();) {

			String packageName = iter.next().getValue();

			try {
				new JenkinsHandler().execute(packageName);
			} catch (Exception ex) {
				// TODO
			}

			continuousIntegrationPresenter.updateViewsAsync();

		}
	}
}
