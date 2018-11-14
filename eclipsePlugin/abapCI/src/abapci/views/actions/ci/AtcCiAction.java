package abapci.views.actions.ci;

import org.eclipse.core.resources.IProject;

import com.sap.adt.atc.model.atcworklist.IAtcWorklist;

import abapci.AbapCiPlugin;
import abapci.GeneralProjectUtil;
import abapci.ci.presenter.ContinuousIntegrationPresenter;
import abapci.domain.AbapPackageTestState;
import abapci.handlers.AbapAtcHandler;
import abapci.testResult.TestResultSummary;
import abapci.testResult.TestResultSummaryFactory;
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
				IProject project = GeneralProjectUtil
						.getAbapProjectByProjectName(abapPackageTestState.getProjectName());
				IAtcWorklist atcWorklist = new AbapAtcHandler(continuousIntegrationPresenter.getAtcFeature())
						.executePackage(project, abapPackageTestState.getPackageName());
				TestResultSummary atcTestResultSummary = new TestResultSummary(project,
						abapPackageTestState.getPackageName(), AtcResultAnalyzer.getTestResult(atcWorklist, null));
				continuousIntegrationPresenter.mergeAtcWorklist(atcTestResultSummary);

			} catch (Exception ex) {
				TestResultSummaryFactory.createOffline(null, abapPackageTestState.getPackageName());
			}

		}
	}
}
