package abapci.views.actions.ci;

import java.net.URI;

import com.sap.adt.tools.core.model.adtcore.IAdtObjectReference;
import com.sap.adt.tools.core.model.util.AdtObjectReferenceAdapterFactory;
import com.sap.adt.tools.core.ui.navigation.AdtNavigationServiceFactory;

import abapci.AbapCiPlugin;
import abapci.ci.presenter.ContinuousIntegrationPresenter;
import abapci.domain.AbapPackageTestState;
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

		for (final AbapPackageTestState abapPackageTestState : getSelectedAbapPackageTestStates()) {

			try {

				URI uriToError = abapPackageTestState.getFirstUnitTestErrors().iterator().hasNext()
						? abapPackageTestState.getFirstUnitTestErrors().iterator().next().getUriToError()
						: null;
				if (uriToError == null) {
					uriToError = abapPackageTestState.getFirstFailedAtcErrors().iterator().hasNext()
							? abapPackageTestState.getFirstFailedAtcErrors().iterator().next().getUriToError()
							: null;
				}

				if (uriToError != null) {
					final IAdtObjectReference objRef = AdtObjectReferenceAdapterFactory.create(uriToError.toString());
					AdtNavigationServiceFactory.createNavigationService()
							.navigate(continuousIntegrationPresenter.getCurrentProject(), objRef, true);

					continuousIntegrationPresenter.setStatusMessage("Test class for first failed test opened");
				} else {
					continuousIntegrationPresenter.setStatusMessage(
							"No failed test or ATC findings found, development object could not be openend");
				}

			} catch (final Exception ex) {
				TestResultSummaryFactory.createOffline(null, abapPackageTestState.getPackageName());
			}

		}

	}
}
