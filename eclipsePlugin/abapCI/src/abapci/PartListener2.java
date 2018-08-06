package abapci;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.SourceViewerDecorationSupport;
import org.eclipse.ui.texteditor.StatusTextEditor;

import abapci.Exception.AbapCiColoredProjectFileParseException;
import abapci.domain.UiColor;
import abapci.feature.FeatureFacade;
import abapci.presenter.GeneralThemePresenter;
import abapci.utils.AnnotationRuleColorChanger;

public class PartListener2 implements IPartListener2 {

	private GeneralThemePresenter generalThemePresenter;
	private AnnotationRuleColorChanger annotationRuleColorChanger;

	public PartListener2(GeneralThemePresenter generalThemePresenter) {
		this.generalThemePresenter = generalThemePresenter;
		this.annotationRuleColorChanger = new AnnotationRuleColorChanger();

	}

	@Override
	public void partActivated(IWorkbenchPartReference arg0) {
		formatPart();
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
		// TODO Auto-generated method stub

	}

	@Override
	public void partVisible(IWorkbenchPartReference arg0) {
		// TODO Auto-generated method stub
	}

	private void formatPart() {

		try {
			FeatureFacade featureFacade = new FeatureFacade();

			IProject currentProject = ProjectUtil.getCurrentProject();

			IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			IEditorPart activeEditor = activePage.getActiveEditor();

			if (activeEditor != null) {

				try {
					String currentProjectname = (currentProject == null) ? "UNDEF" : currentProject.getName();
					UiColor uiColor = generalThemePresenter.getUiColor(currentProjectname);

					boolean changeColorOfTabHeader = featureFacade.getColoredProjectsTabHeaderFeature().isActive();
					boolean changeColorOfLeftRuler = featureFacade.getColoredProjectsLeftRulerFeature().isActive();
					boolean changeColorOfRightRuler = featureFacade.getColoredProjectsRightRulerFeature().isActive();

					if (changeColorOfTabHeader) {
						generalThemePresenter.updateEditorLabel(uiColor);
					}

					annotationRuleColorChanger.change(activeEditor, uiColor, changeColorOfLeftRuler,
							changeColorOfRightRuler);

				} catch (AbapCiColoredProjectFileParseException e) {
					// if there was an error retrieving the color we skip this feature,
					// the user should already got an info message in the ABAP Colored Projects view
					e.printStackTrace();
				}
			}
		} catch (Exception ex) {
			// if there happens any error while formatting the editor, we skip it for now
		}

	}

	protected SourceViewerDecorationSupport getSourceViewerDecorationSupport(IEditorPart editor, ISourceViewer viewer) {

		try {
			Method getSourceViewerDecorationSupport = StatusTextEditor.class
					.getDeclaredMethod("getSourceViewerDecorationSupport", ISourceViewer.class);

			getSourceViewerDecorationSupport.setAccessible(true);
			return (SourceViewerDecorationSupport) getSourceViewerDecorationSupport.invoke(editor, viewer);

		} catch (NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;

	}

}
