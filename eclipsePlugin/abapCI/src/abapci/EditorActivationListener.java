package abapci;

import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.PlatformUI;

import abapci.Exception.AbapCiColoredProjectFileParseException;
import abapci.Exception.ActiveEditorNotSetException;
import abapci.coloredProject.colorChanger.ColorChanger;
import abapci.coloredProject.colorChanger.ColorChangerFactory;
import abapci.coloredProject.colorChanger.ColorChangerType;
import abapci.coloredProject.exeption.ColorChangerNotImplementedException;
import abapci.coloredProject.exeption.ProjectColorNotSetException;
import abapci.coloredProject.general.DisplayColor;
import abapci.coloredProject.general.EditorActivationHandler;
import abapci.coloredProject.general.WorkspaceColorConfiguration;
import abapci.feature.FeatureFacade;
import abapci.feature.activeFeature.ColoredProjectFeature;

public class EditorActivationListener implements IPartListener2 {
	ColoredProjectFeature coloredProjectFeature;

	public EditorActivationListener() {
		FeatureFacade featureFacade = new FeatureFacade();
		coloredProjectFeature = featureFacade.getColoredProjectFeature();
	}

	@Override
	public void partActivated(IWorkbenchPartReference arg0) {
		try {
			final EditorActivationHandler editorActivationHandler = new EditorActivationHandler();
			editorActivationHandler.updateDisplayColoring();

			updateProjectColorsExperimental();
		} catch (final Exception ex) {
			// if there happens any error while formatting the editor, we skip it as this
			// function is not mandatory
			// but at least write the trace
			ex.printStackTrace();
		}
	}

	@Override
	public void partBroughtToTop(IWorkbenchPartReference arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void partClosed(IWorkbenchPartReference arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void partDeactivated(IWorkbenchPartReference arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void partHidden(IWorkbenchPartReference arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void partInputChanged(IWorkbenchPartReference arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void partOpened(IWorkbenchPartReference arg0) {
		try {
			final EditorActivationHandler editorActivationHandler = new EditorActivationHandler();
			editorActivationHandler.updateDisplayColoring();
		} catch (final Exception ex) {
			// if there happens any error while formatting the editor, we skip it as this
			// function is not mandatory
			// but at least write the trace
			ex.printStackTrace();
		}
	}

	@Override
	public void partVisible(IWorkbenchPartReference arg0) {
		// TODO Auto-generated method stub
	}

	private void updateProjectColorsExperimental() throws ColorChangerNotImplementedException,
			AbapCiColoredProjectFileParseException, ActiveEditorNotSetException, ProjectColorNotSetException {

		if (coloredProjectFeature.isTitleIconActive()) {
			IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			activePage.getActiveEditor();

			IEditorReference[] editorReferences = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
					.getEditorReferences();

			ColorChangerFactory colorChangerFactory = new ColorChangerFactory();

			AbapCiPluginHelper abapCiPluginHelper = new AbapCiPluginHelper();
			WorkspaceColorConfiguration workspaceColorConfiguration = abapCiPluginHelper
					.getWorkspaceColorConfiguration();

			for (IEditorReference editorReference : editorReferences) {
				DisplayColor displayColor = workspaceColorConfiguration
						.getColoring(GeneralProjectUtil.getProject(editorReference.getEditor(true)));
				ColorChanger colorChanger = colorChangerFactory.create(ColorChangerType.TITLE_ICON,
						editorReference.getEditor(true), coloredProjectFeature, displayColor.getTitleIconColor());
				colorChanger.change();
			}
		}

	}

}
