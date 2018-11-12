package abapci.views.actions.ci;

import java.util.HashSet;

import org.eclipse.core.resources.IProject;

import abapci.AbapCiPlugin;
import abapci.GeneralProjectUtil;
import abapci.activation.Activation;
import abapci.ci.presenter.ContinuousIntegrationPresenter;
import abapci.domain.AbapPackageTestState;
import abapci.handlers.AbapUnitHandler;
import abapci.testResult.TestResultSummary;
import abapci.testResult.TestResultSummaryFactory;

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

		for (AbapPackageTestState abapPackageTestState : getSelectedAbapPackageTestStates()) {

			try {
				IProject project = GeneralProjectUtil.getAbapProjectByProjectName(abapPackageTestState.getProjectName());
				TestResultSummary unitTestResultSummary = new AbapUnitHandler().executePackage(project,
						abapPackageTestState.getPackageName(), new HashSet<Activation>());
				continuousIntegrationPresenter.mergeUnitTestResultSummary(unitTestResultSummary);

			} catch (Exception ex) {
				TestResultSummaryFactory.createOffline(null, abapPackageTestState.getPackageName());
			}

		}

	}
}
