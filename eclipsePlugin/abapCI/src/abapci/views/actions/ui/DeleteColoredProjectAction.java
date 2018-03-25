package abapci.views.actions.ui;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import abapci.domain.ColoredProject;
import abapci.presenter.ColoredProjectsPresenter;

public class DeleteColoredProjectAction extends Action {

	ColoredProjectsPresenter presenter; 
	
	public DeleteColoredProjectAction(ColoredProjectsPresenter presenter, String label) {
		this.presenter = presenter; 
		this.setText(label);
		this.setImageDescriptor(
				PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_ELCL_REMOVE));

	}

	@Override
	public void run() {

		ISelection selection = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.getSelection();
		ColoredProject firstColoredProject = (ColoredProject) ((StructuredSelection) selection).getFirstElement();
		
		presenter.removeColoredProject(firstColoredProject); 
		presenter.setViewerInput(); 
	}

}
