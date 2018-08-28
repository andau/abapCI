package abapci.views.actions.ci;

import java.net.URI;

import org.eclipse.core.resources.IProject;

import com.sap.adt.tools.core.model.adtcore.IAdtObjectReference;
import com.sap.adt.tools.core.model.util.AdtObjectReferenceAdapterFactory;
import com.sap.adt.tools.core.ui.navigation.AdtNavigationServiceFactory;

import abapci.AbapCiPlugin;
import abapci.AbapProjectUtil;
import abapci.domain.AbapPackageTestState;
import abapci.domain.TestResultSummary;
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

		for (AbapPackageTestState abapPackageTestState : getSelectedAbapPackageTestStates()) {

			try {
				IProject project = AbapProjectUtil.getProjectByProjectName(abapPackageTestState.getProjectName());

				TestResultSummary unitTestResultSummary = new AbapUnitHandler().executePackage(project,
						abapPackageTestState.getPackageName());
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
				TestResultSummaryFactory.createOffline(abapPackageTestState.getPackageName());
			}

		}

	}
}
