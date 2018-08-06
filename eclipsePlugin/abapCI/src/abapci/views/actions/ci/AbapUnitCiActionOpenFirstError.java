package abapci.views.actions.ci;

import java.net.URI;
import java.util.Iterator;
import java.util.Map.Entry;

import com.sap.adt.tools.core.model.adtcore.IAdtObjectReference;
import com.sap.adt.tools.core.model.util.AdtObjectReferenceAdapterFactory;
import com.sap.adt.tools.core.ui.navigation.AdtNavigationServiceFactory;

import abapci.AbapCiPlugin;
import abapci.domain.UnitTestResultSummary;
import abapci.handlers.AbapUnitHandler;
import abapci.presenter.ContinuousIntegrationPresenter;
import abapci.result.TestResultSummaryFactory;

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

		// String uriString =
		// unitTestResultSummary.getTestResult().getFirstInvalidItem().getFirstStackEntry().getUri()
		// .toString();

		for (Iterator<Entry<String, String>> iter = getSelectedPackages().entrySet().iterator(); iter.hasNext();) {

			String packageName = iter.next().getValue();

			try {
				UnitTestResultSummary unitTestResultSummary = new AbapUnitHandler()
						.executePackage(continuousIntegrationPresenter.getCurrentProject(), packageName);
				if (unitTestResultSummary.getTestResult().getFirstInvalidItem() != null) {
					URI uri = unitTestResultSummary.getTestResult().getFirstInvalidItem().getFirstStackEntry().getUri();
					IAdtObjectReference objRef = AdtObjectReferenceAdapterFactory.create(uri.toString());
					AdtNavigationServiceFactory.createNavigationService()
							.navigate(continuousIntegrationPresenter.getCurrentProject(), objRef, true);

					continuousIntegrationPresenter.setStatusMessage("Test class for first failed test opened");
				} else {
					continuousIntegrationPresenter
							.setStatusMessage("No failed test exists, test class for first test could not be openend");
				}

			} catch (Exception ex) {
				TestResultSummaryFactory.createOffline(packageName);
			}

		}

	}
}
