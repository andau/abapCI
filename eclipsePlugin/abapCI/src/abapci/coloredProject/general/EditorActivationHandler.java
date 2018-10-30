package abapci.coloredProject.general;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import abapci.AbapCiPluginHelper;
import abapci.AbapProjectUtil;
import abapci.Exception.AbapCiColoredProjectFileParseException;
import abapci.coloredProject.model.ColoredProject;
import abapci.coloredProject.model.projectColor.DefaultEclipseProjectColor;
import abapci.coloredProject.presenter.ColoredProjectsPresenter;
import abapci.coloredProject.view.AddOrUpdateColoredProjectPage;
import abapci.feature.ColoredProjectFeature;
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

				ColoredProjectFeature coloredProjectFeature = featureFacade.getColoredProjectFeature();

				if (coloredProjectFeature.isActive()) {
					WorkspaceColorProxySingleton colorProxy = WorkspaceColorProxySingleton.getInstance();
					if (!colorProxy.isConfigured(currentProject)) {
						showDialogForProject(currentProject);
					}

					DisplayColor displayColoring = WorkspaceColorProxySingleton.getInstance()
							.getColoring(currentProject);
				    
					displayColorChanger.change(activeEditor, displayColoring, coloredProjectFeature );

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
		// dialog is called asynchronously to not stop UI processing 
		// this means especially for a new added project that it is not colored directly after adding 
		Display.getDefault().asyncExec(runnable);
	}

	private void handleNewColoredProject(IProject project) {

		AbapCiPluginHelper abapCiPluginHelper = new AbapCiPluginHelper(); 
		
		ColoredProjectsPresenter coloredProjectPresenter = abapCiPluginHelper.getColoredProjectsPresenter();
		ColoredProject coloredProject = new ColoredProject(project.getName(), new DefaultEclipseProjectColor());
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
