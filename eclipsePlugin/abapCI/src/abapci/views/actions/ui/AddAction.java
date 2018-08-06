package abapci.views.actions.ui;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import abapci.presenter.ContinuousIntegrationPresenter;
import abapci.views.wizard.AddContinuousIntegrationConfigPage;

public class AddAction extends Action {

	private ContinuousIntegrationPresenter presenter;

	public AddAction(ContinuousIntegrationPresenter presenter, String label) {
		this.setText(label);
		this.setImageDescriptor(
				PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJ_ADD));
		this.presenter = presenter;

	}

	public void run() {

		AddContinuousIntegrationConfigPage ciConfigPage = new AddContinuousIntegrationConfigPage(
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), presenter, null);
		if (ciConfigPage.open() == Window.OK) {

			// TODO
		}

	}
}
