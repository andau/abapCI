package abapci.views.actions.ci;

import java.util.Iterator;
import java.util.Map.Entry;

import com.sap.adt.atc.model.atcworklist.IAtcWorklist;

import abapci.AbapCiPlugin;
import abapci.handlers.AbapAtcHandler;
import abapci.presenter.ContinuousIntegrationPresenter;

public class AtcCiAction extends AbstractCiAction {

	public AtcCiAction(ContinuousIntegrationPresenter continuousIntegrationPresenter, String label, String tooltip) {
		this.setText(label);
		this.setToolTipText(tooltip);
		this.setImageDescriptor(AbapCiPlugin.getImageDescriptor("icons/atc.png"));
		this.continuousIntegrationPresenter = continuousIntegrationPresenter;
	}

	@Override
	public void run() {

		for (Iterator<Entry<String, String>> iter = getSelectedPackages().entrySet().iterator(); iter.hasNext();) {

			String packageName = iter.next().getValue();

			try {
				IAtcWorklist atcWorklist = new AbapAtcHandler().executePackage(packageName);

			} catch (Exception ex) {
				// TODO
			}

		}
	}
}
