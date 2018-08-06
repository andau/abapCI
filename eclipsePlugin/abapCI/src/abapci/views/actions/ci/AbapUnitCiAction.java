package abapci.views.actions.ci;

import java.util.Iterator;
import java.util.Map.Entry;

import abapci.AbapCiPlugin;
import abapci.handlers.AbapUnitHandler;
import abapci.presenter.ContinuousIntegrationPresenter;
import abapci.result.TestResultSummaryFactory;

public class AbapUnitCiAction extends AbstractCiAction {

	public AbapUnitCiAction(ContinuousIntegrationPresenter continuousIntegrationPresenter, String label,
			String tooltip) {
		this.setText(label);
		this.setToolTipText(tooltip);
		this.setImageDescriptor(AbapCiPlugin.getImageDescriptor("icons/aunit.png"));
		this.continuousIntegrationPresenter = continuousIntegrationPresenter;
	}

	@Override
	public void run() {

		for (Iterator<Entry<String, String>> iter = getSelectedPackages().entrySet().iterator(); iter.hasNext();) {

			String packageName = iter.next().getValue();

			try {
				new AbapUnitHandler().executePackage(continuousIntegrationPresenter.getCurrentProject(), packageName);

			} catch (Exception ex) {
				TestResultSummaryFactory.createOffline(packageName);
			}

		}

	}
}
