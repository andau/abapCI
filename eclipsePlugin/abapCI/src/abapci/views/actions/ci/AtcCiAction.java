package abapci.views.actions.ci;

import org.eclipse.core.resources.IProject;

import com.sap.adt.atc.model.atcworklist.IAtcWorklist;

import abapci.AbapCiPlugin;
import abapci.AbapProjectUtil;
import abapci.domain.AbapPackageTestState;
import abapci.domain.UnitTestResultSummary;
import abapci.handlers.AbapAtcHandler;
import abapci.presenter.ContinuousIntegrationPresenter;
import abapci.result.TestResultSummaryFactory;
import abapci.utils.AtcResultAnalyzer;

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
				IProject project = AbapProjectUtil.getProjectByProjectName(abapPackageTestState.getProjectName());
				IAtcWorklist atcWorklist = new AbapAtcHandler().executePackage(project,
						abapPackageTestState.getPackageName());
				UnitTestResultSummary atcTestResultSummary = new UnitTestResultSummary(
						abapPackageTestState.getPackageName(), AtcResultAnalyzer.getTestResult(atcWorklist));
				continuousIntegrationPresenter.mergeAtcWorklist(atcTestResultSummary);

			} catch (Exception ex) {
				TestResultSummaryFactory.createOffline(abapPackageTestState.getPackageName());
			}

		}
	}
}
