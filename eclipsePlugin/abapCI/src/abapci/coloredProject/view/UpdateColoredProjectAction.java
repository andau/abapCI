package abapci.coloredProject.view;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import abapci.coloredProject.model.ColoredProject;
import abapci.coloredProject.presenter.ColoredProjectsPresenter;

public class UpdateColoredProjectAction extends Action {
	ColoredProjectsPresenter presenter;

	public UpdateColoredProjectAction(ColoredProjectsPresenter presenter, String label) {
		this.setText(label);
		this.setImageDescriptor(
				PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJ_ADD));
		this.presenter = presenter;
	}

	@Override
	public void run() {

		ISelection selection = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getSelection();

		Object firstElement = ((StructuredSelection) selection).getFirstElement();
		if (firstElement instanceof ColoredProject) {
			AddOrUpdateColoredProjectPage coloredProjectDialog = new AddOrUpdateColoredProjectPage(
					PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), presenter,
					(ColoredProject) firstElement);
			if (coloredProjectDialog.open() == Window.OK) {
				presenter.setStatusMessage("Update to colored project configuration successful");
			}
		} else {
			presenter.setStatusErrorMessage("Update to colored project configuration could not be performed");
		}
	}
}
