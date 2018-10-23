package abapci;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.texteditor.SourceViewerDecorationSupport;
import org.eclipse.ui.texteditor.StatusTextEditor;

import abapci.coloredProject.general.DisplayColorChanger;
import abapci.coloredProject.general.EditorActivationHandler;
import abapci.presenter.GeneralThemePresenter;

public class PartListener2 implements IPartListener2 {

	private GeneralThemePresenter generalThemePresenter;
	private DisplayColorChanger annotationRuleColorChanger;

	public PartListener2(GeneralThemePresenter generalThemePresenter) {
		this.generalThemePresenter = generalThemePresenter;
		this.annotationRuleColorChanger = new DisplayColorChanger();

	}

	@Override
	public void partActivated(IWorkbenchPartReference arg0) {
		try {
			EditorActivationHandler editorActivationHandler = new EditorActivationHandler();
			editorActivationHandler.updateDisplayColoring();
		} catch (Exception ex) {
			// if there happens any error while formatting the editor, we skip it as this
			// function is not mandatory
			// but at leas write the trace
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
		// TODO Auto-generated method stub

	}

	@Override
	public void partVisible(IWorkbenchPartReference arg0) {
		// TODO Auto-generated method stub
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
