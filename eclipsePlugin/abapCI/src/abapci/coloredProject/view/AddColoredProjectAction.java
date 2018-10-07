package abapci.coloredProject.view;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import abapci.coloredProject.presenter.ColoredProjectsPresenter;

public class AddColoredProjectAction extends Action {
	ColoredProjectsPresenter presenter;

	public AddColoredProjectAction(ColoredProjectsPresenter presenter, String label) {
		this.setText(label);
		this.setImageDescriptor(
				PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJ_ADD));
		this.presenter = presenter;
	}

	@Override
	public void run() {

		AddOrUpdateColoredProjectPage coloredProjectDialog = new AddOrUpdateColoredProjectPage(
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), presenter, null);
		if (coloredProjectDialog.open() == Window.OK) {

			// TODO
		}
	}
}
