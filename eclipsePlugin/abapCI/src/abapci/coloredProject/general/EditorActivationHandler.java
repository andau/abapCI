package abapci.coloredProject.general;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import abapci.AbapCiPlugin;
import abapci.AbapProjectUtil;
import abapci.Exception.AbapCiColoredProjectFileParseException;
import abapci.coloredProject.model.ColoredProject;
import abapci.coloredProject.presenter.ColoredProjectsPresenter;
import abapci.coloredProject.view.AddOrUpdateColoredProjectPage;
import abapci.feature.FeatureFacade;

public class EditorActivationHandler {

	private DisplayColorChanger displayColorChanger;
	private FeatureFacade featureFacade;

	public EditorActivationHandler() {
		this.displayColorChanger = new DisplayColorChanger();
		featureFacade = new FeatureFacade();
	}

	public void updateDisplayColoring() {

		IProject currentProject = AbapProjectUtil.getCurrentProject();

		IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		IEditorPart activeEditor = activePage.getActiveEditor();

		if (activeEditor != null && currentProject != null) {

			try {

				boolean changeColorOfLeftRuler = featureFacade.getColoredProjectFeature().isLeftRulerActive();
				boolean changeColorOfRightRuler = featureFacade.getColoredProjectFeature().isRightRulerActive();
				boolean changeStatusBar = featureFacade.getColoredProjectFeature().isChangeStatusBarActive();

				if (changeColorOfLeftRuler || changeColorOfRightRuler || changeStatusBar) {
					WorkspaceColorProxySingleton colorProxy = WorkspaceColorProxySingleton.getInstance();
					if (!colorProxy.isConfigured(currentProject)) {
						showDialogForProject(currentProject);
					}

					DisplayColor displayColoring = WorkspaceColorProxySingleton.getInstance()
							.getColoring(currentProject);
					displayColorChanger.change(activeEditor, displayColoring, changeColorOfLeftRuler,
							changeColorOfRightRuler, changeStatusBar);

				}

			} catch (AbapCiColoredProjectFileParseException e) {
				// if there was an error retrieving the color we skip this feature,
				// the user should already got an info message in the ABAP Colored Projects view
				e.printStackTrace();
			}
		}
	}

	private void showDialogForProject(IProject currentProject) {
		Runnable runnable = () -> handleNewColoredProject(currentProject);
		Display.getDefault().asyncExec(runnable);
	}

	private void handleNewColoredProject(IProject project) {

		ColoredProjectsPresenter coloredProjectPresenter = AbapCiPlugin.getDefault().coloredProjectsPresenter;
		ColoredProject coloredProject = new ColoredProject(project.getName(), null);
		if (featureFacade.getColoredProjectFeature().isDialogEnabled()) {
			AddOrUpdateColoredProjectPage addOrUpdateColoredProjectPage = new AddOrUpdateColoredProjectPage(
					PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), coloredProjectPresenter,
					coloredProject, true);

			if (addOrUpdateColoredProjectPage.open() == Window.OK) {
				coloredProjectPresenter.setStatusMessage(
						String.format("Configuration for colored project %s added ", coloredProject.getName()));
			} else {
				coloredProjectPresenter.setStatusMessage(
						String.format("Configuration for colored project %s aborted ", coloredProject.getName()));
			}
		}

	}

}
