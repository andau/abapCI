package abapci.views.actions.ci;

import com.sap.adt.atc.model.atcworklist.IAtcWorklist;

import abapci.AbapCiPlugin;
import abapci.domain.AbapPackageTestState;
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

		for (AbapPackageTestState abapPackageTestState : getSelectedAbapPackageTestStates()) {

			try {
				IAtcWorklist atcWorklist = new AbapAtcHandler().executePackage(abapPackageTestState.getPackageName());

			} catch (Exception ex) {
				// TODO
			}

		}
	}
}
