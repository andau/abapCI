package abapci;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.WorkbenchPart;

import abapci.Exception.AbapCiColoredProjectFileParseException;
import abapci.Exception.ActiveEditorNotSetException;
import abapci.abapgit.GitEditorIdentifier;
import abapci.coloredProject.colorChanger.ColorChanger;
import abapci.coloredProject.colorChanger.ColorChangerFactory;
import abapci.coloredProject.colorChanger.ColorChangerType;
import abapci.coloredProject.exeption.ColorChangerNotImplementedException;
import abapci.coloredProject.exeption.ProjectColorNotSetException;
import abapci.coloredProject.general.DisplayColor;
import abapci.coloredProject.general.EditorActivationHandler;
import abapci.coloredProject.general.WorkspaceColorConfiguration;
import abapci.feature.FeatureFacade;
import abapci.feature.activeFeature.AbapGitFeature;
import abapci.feature.activeFeature.ColoredProjectFeature;

public class EditorActivationListener implements IPartListener2 {

	private static final String ICONS_GIT = "icons/abapgit.png";

	AbapCiPluginHelper abapCiPluginHelper = new AbapCiPluginHelper();

	ColoredProjectFeature coloredProjectFeature;
	AbapGitFeature abapGitFeature;

	public EditorActivationListener() {
		final FeatureFacade featureFacade = new FeatureFacade();
		coloredProjectFeature = featureFacade.getColoredProjectFeature();
		abapGitFeature = featureFacade.getAbapGitFeature();
	}

	@Override
	public void partActivated(IWorkbenchPartReference arg0) {
		try {
			final EditorActivationHandler editorActivationHandler = new EditorActivationHandler();
			editorActivationHandler.updateDisplayColoring();

			updateProjectColorsExperimental();
			updateGitLabelsExperimental();

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
			final IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			activePage.getActiveEditor();

			final IEditorReference[] editorReferences = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
					.getActivePage().getEditorReferences();

			final ColorChangerFactory colorChangerFactory = new ColorChangerFactory();

			final AbapCiPluginHelper abapCiPluginHelper = new AbapCiPluginHelper();
			final WorkspaceColorConfiguration workspaceColorConfiguration = abapCiPluginHelper
					.getWorkspaceColorConfiguration();

			for (final IEditorReference editorReference : editorReferences) {
				final DisplayColor displayColor = workspaceColorConfiguration
						.getColoring(GeneralProjectUtil.getProject(editorReference.getEditor(true)));
				final ColorChanger colorChanger = colorChangerFactory.create(ColorChangerType.TITLE_ICON,
						editorReference.getEditor(true), coloredProjectFeature, displayColor.getTitleIconColor());
				colorChanger.change();
			}
		}

	}

	private void updateGitLabelsExperimental() {

		if (abapGitFeature.isChangeTransactionLabelActive()) {

			for (final Map.Entry<GitEditorIdentifier, IEditorPart> entry : abapCiPluginHelper.getGitEditors()
					.entrySet()) {
				try {

					final Image newTitleImage = abapCiPluginHelper.getImageDescriptor(ICONS_GIT).createImage();

					final WorkbenchPart workbenchPart = (WorkbenchPart) entry.getValue();
					final Method imageMethod = WorkbenchPart.class.getDeclaredMethod("setTitleImage", Image.class);
					imageMethod.setAccessible(true);
					imageMethod.invoke(workbenchPart, newTitleImage);

					final Method partNameMethod = WorkbenchPart.class.getDeclaredMethod("setPartName", String.class);
					partNameMethod.setAccessible(true);
					partNameMethod.invoke(workbenchPart, entry.getKey().getPackageName());

				} catch (final Exception e) {
					// when an error occurs while changing the title icon with the git icon we move
					// on as this
					// function is not critical
					e.printStackTrace();
				}
			}
		}

	}

	@Deprecated
	private Image getTitleIcon(IEditorPart editorPart) {

		try {
			final Field f = WorkbenchPart.class.getDeclaredField("titleImage");
			f.setAccessible(true);
			return (Image) f.get(editorPart);
		} catch (final Exception err) {
			return null;
		}
	}

}
