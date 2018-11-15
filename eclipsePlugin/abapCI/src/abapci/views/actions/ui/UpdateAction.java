package abapci.views.actions.ui;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import abapci.ci.presenter.ContinuousIntegrationPresenter;
import abapci.ci.views.AddOrUpdateContinuousIntegrationConfigPage;
import abapci.domain.AbapPackageTestState;
import abapci.domain.ContinuousIntegrationConfig;

public class UpdateAction extends Action {

	private ContinuousIntegrationPresenter presenter;

	public UpdateAction(ContinuousIntegrationPresenter presenter, String label) {
		this.setText(label);
		this.setImageDescriptor(
				PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJ_FILE));
		this.presenter = presenter;

	}

	public void run() {

		ISelection selection = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getSelection();

		AbapPackageTestState firstAbapPackageTestState = (AbapPackageTestState) ((StructuredSelection) selection)
				.getFirstElement();

		if (firstAbapPackageTestState != null) {
			ContinuousIntegrationConfig ciConfig = new ContinuousIntegrationConfig(
					firstAbapPackageTestState.getProjectName(), firstAbapPackageTestState.getPackageName(), true, true);

			AddOrUpdateContinuousIntegrationConfigPage ciConfigPage = new AddOrUpdateContinuousIntegrationConfigPage(
					PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), presenter, ciConfig, false);
			if (ciConfigPage.open() == Window.OK) {
				presenter.setStatusMessage(String.format("Package configuration for package %s changed",
						firstAbapPackageTestState.getPackageName()));
			}
		} else {
			presenter.setStatusMessage("Update failed - please select one entry by clicking on the first column!");
		}

	}
}
