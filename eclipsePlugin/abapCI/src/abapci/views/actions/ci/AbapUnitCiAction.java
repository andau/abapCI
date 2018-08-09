package abapci.views.actions.ci;

import org.eclipse.core.resources.IProject;

import abapci.AbapCiPlugin;
import abapci.AbapProjectUtil;
import abapci.domain.AbapPackageTestState;
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

		for (AbapPackageTestState abapPackageTestState : getSelectedAbapPackageTestStates()) {

			try {
				IProject project = AbapProjectUtil.getProjectByProjectName(abapPackageTestState.getProjectName());
				new AbapUnitHandler().executePackage(project, abapPackageTestState.getPackageName());

			} catch (Exception ex) {
				TestResultSummaryFactory.createOffline(abapPackageTestState.getPackageName());
			}

		}

	}
}
