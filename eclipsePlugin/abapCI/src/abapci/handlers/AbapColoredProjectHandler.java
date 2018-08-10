package abapci.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import abapci.model.ColoredProjectModel;
import abapci.presenter.ColoredProjectsPresenter;
import abapci.views.AbapCiColoredProjectView;
import abapci.views.wizard.AddColoredProjectPage;

public class AbapColoredProjectHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		ISelection selection = HandlerUtil.getCurrentSelection(event);
		if (!selection.isEmpty()) {
			if (selection instanceof TreeSelection) {
				if (!((TreeSelection) selection).isEmpty()
						&& ((TreeSelection) selection).getFirstElement() instanceof IProject) {
					IProject project = (IProject) ((TreeSelection) selection).getFirstElement();

					IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();

					AbapCiColoredProjectView abapCiColoredProjectView = (AbapCiColoredProjectView) page
							.findView(AbapCiColoredProjectView.ID);
					ColoredProjectsPresenter coloredProjectPresenter = new ColoredProjectsPresenter(
							abapCiColoredProjectView, new ColoredProjectModel());

					AddColoredProjectPage addColoredProjectPage = new AddColoredProjectPage(
							PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), coloredProjectPresenter,
							project);

					if (addColoredProjectPage.open() == Window.OK) {
						coloredProjectPresenter.setStatusMessage(
								String.format("The coloring for the package '%s' was set", project.getName()));
					}

				}
			}
		}
		return null;
	}
}
