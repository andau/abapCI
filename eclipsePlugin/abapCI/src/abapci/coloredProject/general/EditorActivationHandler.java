package abapci.coloredProject.general;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import abapci.AbapCiPluginHelper;
import abapci.GeneralProjectUtil;
import abapci.coloredProject.model.ColoredProject;
import abapci.coloredProject.model.projectColor.DefaultEclipseProjectColor;
import abapci.coloredProject.presenter.ColoredProjectsPresenter;
import abapci.coloredProject.view.AddOrUpdateColoredProjectPage;
import abapci.feature.FeatureFacade;
import abapci.feature.SourceCodeVisualisationFeature;
import abapci.feature.activeFeature.ColoredProjectFeature;

public class EditorActivationHandler {

	private final DisplayColorChanger displayColorChanger;
	private ColoredProjectFeature coloredProjectFeature;
	private SourceCodeVisualisationFeature sourceCodeVisualisationFeature;
	private final AbapCiPluginHelper abapCiPluginHelper;

	public EditorActivationHandler() {

		abapCiPluginHelper = new AbapCiPluginHelper();

		this.displayColorChanger = new DisplayColorChanger();
		initFeatures();

	}

	private void initFeatures() {
		FeatureFacade featureFacade = new FeatureFacade();
		coloredProjectFeature = featureFacade.getColoredProjectFeature();
		sourceCodeVisualisationFeature = featureFacade.getSourceCodeVisualisationFeature();
	}

	public void updateDisplayColoring() {

		IProject currentProject = GeneralProjectUtil.getCurrentProject();

		IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		IEditorPart activeEditor = activePage.getActiveEditor();

		if (activeEditor != null && currentProject != null) {

			if (coloredProjectFeature.isActive()) {

				WorkspaceColorConfiguration colorConfiguration = abapCiPluginHelper.getWorkspaceColorConfiguration();
				if (!colorConfiguration.isConfigured(currentProject)) {
					showDialogForProject(currentProject);
				}

				DisplayColor displayColoring = colorConfiguration.getColoring(currentProject);

				displayColorChanger.change(activeEditor, displayColoring, coloredProjectFeature,
						sourceCodeVisualisationFeature);

			}
		}
	}

	private void showDialogForProject(IProject currentProject) {
		Runnable runnable = () -> handleNewColoredProject(currentProject);
		// dialog is called asynchronously to not stop UI processing
		// this means especially for a new added project that it is not colored directly
		// after adding
		Display.getDefault().asyncExec(runnable);
	}

	private void handleNewColoredProject(IProject project) {

		ColoredProjectsPresenter coloredProjectPresenter = abapCiPluginHelper.getColoredProjectsPresenter();
		ColoredProject coloredProject = new ColoredProject(project.getName(), new DefaultEclipseProjectColor());
		if (coloredProjectFeature.isDialogEnabled()) {
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
