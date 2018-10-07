package abapci.coloredProject.handler;

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

import abapci.Exception.AbapCiColoredProjectFileParseException;
import abapci.coloredProject.model.ColoredProject;
import abapci.coloredProject.model.ColoredProjectModel;
import abapci.coloredProject.presenter.ColoredProjectsPresenter;
import abapci.coloredProject.view.AbapCiColoredProjectView;
import abapci.coloredProject.view.AddOrUpdateColoredProjectPage;
import abapci.domain.UiColor;

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

					UiColor assignedUiColor;
					try {
						assignedUiColor = coloredProjectPresenter.getUiColorOrDefault(project.getName());

						ColoredProject coloredProject = new ColoredProject(project.getName(), assignedUiColor);
						AddOrUpdateColoredProjectPage addColoredProjectPage = new AddOrUpdateColoredProjectPage(
								PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
								coloredProjectPresenter, coloredProject);

						if (addColoredProjectPage.open() == Window.OK) {
							coloredProjectPresenter.setStatusMessage(
									String.format("The coloring for the package '%s' was set", project.getName()));
						}
					} catch (AbapCiColoredProjectFileParseException e) {
						coloredProjectPresenter.setStatusMessage(
								String.format("The file with the project coloring info could not be read"));
					}

				}
			}
		}
		return null;
	}
}
