package abapci.views.actions.ci;

import java.net.URI;

import com.sap.adt.tools.core.model.adtcore.IAdtObjectReference;
import com.sap.adt.tools.core.model.util.AdtObjectReferenceAdapterFactory;
import com.sap.adt.tools.core.ui.navigation.AdtNavigationServiceFactory;

import abapci.AbapCiPlugin;
import abapci.domain.AbapPackageTestState;
import abapci.presenter.ContinuousIntegrationPresenter;
import abapci.testResult.TestResultSummaryFactory;

public class AbapUnitCiActionOpenFirstError extends AbstractCiAction {

	public AbapUnitCiActionOpenFirstError(ContinuousIntegrationPresenter continuousIntegrationPresenter, String label,
			String tooltip) {
		this.setText(label);
		this.setToolTipText(tooltip);
		this.setImageDescriptor(AbapCiPlugin.getImageDescriptor("icons/aunit.png"));
		this.continuousIntegrationPresenter = continuousIntegrationPresenter;
	}

	@Override
	public void run() {

		for (AbapPackageTestState abapPackageTestState : getSelectedAbapPackageTestStates()) {

			try {

				URI uriToError = abapPackageTestState.getFirstFailedUnitTest() != null
						? abapPackageTestState.getFirstFailedUnitTest().getUriToError()
						: null;
				if (uriToError == null) {
					uriToError = abapPackageTestState.getFirstFailedAtc() != null
							? abapPackageTestState.getFirstFailedAtc().getUriToError()
							: null;
				}

				if (uriToError != null) {
					IAdtObjectReference objRef = AdtObjectReferenceAdapterFactory.create(uriToError.toString());
					AdtNavigationServiceFactory.createNavigationService()
							.navigate(continuousIntegrationPresenter.getCurrentProject(), objRef, true);

					continuousIntegrationPresenter.setStatusMessage("Test class for first failed test opened");
				} else {
					continuousIntegrationPresenter.setStatusMessage(
							"No failed test or ATC findings found, development object could not be openend");
				}

			} catch (Exception ex) {
				TestResultSummaryFactory.createOffline(null, abapPackageTestState.getPackageName());
			}

		}

	}
}
