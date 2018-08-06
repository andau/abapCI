package abapci.views.actions.ui;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import abapci.domain.AbapPackageTestState;
import abapci.domain.ContinuousIntegrationConfig;
import abapci.presenter.ContinuousIntegrationPresenter;

public class DeleteAction extends Action {

	private ContinuousIntegrationPresenter presenter;

	public DeleteAction(ContinuousIntegrationPresenter presenter, String label) {
		this.setText(label);
		this.setImageDescriptor(
				PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_ELCL_REMOVE));
		this.presenter = presenter;

	}

	public void run() {

		ISelection selection = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getSelection();

		AbapPackageTestState firstAbapPackageTestState = (AbapPackageTestState) ((StructuredSelection) selection)
				.getFirstElement();

		if (firstAbapPackageTestState != null) {
			ContinuousIntegrationConfig ciConfig = new ContinuousIntegrationConfig(
					firstAbapPackageTestState.getProjectName(), firstAbapPackageTestState.getPackageName(), true, true);
			presenter.removeContinousIntegrationConfig(ciConfig);
		} else {
			presenter.setStatusMessage("Deletion of the package entry failed");
		}

	}

}
