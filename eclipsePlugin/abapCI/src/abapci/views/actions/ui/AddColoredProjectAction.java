package abapci.views.actions.ui;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import abapci.presenter.ColoredProjectsPresenter;
import abapci.views.wizard.AddColoredProjectPage;

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

		AddColoredProjectPage coloredProjectDialog = new AddColoredProjectPage(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), presenter); 
		if (coloredProjectDialog.open() == Window.OK) {
			
			//TODO 
		}
	}
}
