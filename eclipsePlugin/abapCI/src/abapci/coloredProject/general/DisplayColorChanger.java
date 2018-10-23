package abapci.coloredProject.general;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.source.CompositeRuler;
import org.eclipse.jface.text.source.IOverviewRuler;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.jface.text.source.OverviewRuler;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.texteditor.AbstractTextEditor;

import abapci.utils.BackgroundColorChanger;

public class DisplayColorChanger {

	BackgroundColorChanger backgroundColorChanger;

	public DisplayColorChanger() {
		backgroundColorChanger = new BackgroundColorChanger();
	}

	public void change(IEditorPart editorPart, DisplayColor displayColoring, boolean changeColorOfLeftRuler,
			boolean changeColorOfRightRuler, boolean changeStatusBar) {
		try {

			if (changeStatusBar) {
				backgroundColorChanger.change(editorPart.getEditorSite().getShell(),
						displayColoring.getStatusBarColor());
			}

			ITextViewer textViewer = null;
			Object activeEditor;

			if (editorPart instanceof AbstractTextEditor) {
				activeEditor = editorPart;
			} else {
				activeEditor = ((MultiPageEditorPart) editorPart).getSelectedPage();
			}
			if (activeEditor instanceof AbstractTextEditor) {
				textViewer = callGetSourceViewer((AbstractTextEditor) activeEditor);
			}
			CompositeRuler verticalRuler = (CompositeRuler) getVerticalRuler((SourceViewer) textViewer);
			OverviewRuler overviewRuler = (OverviewRuler) getOverviewRuler((SourceViewer) textViewer);

			if (displayColoring.getAnnotationBarColor() == null)
				return;

			if (verticalRuler != null && changeColorOfLeftRuler) {
				verticalRuler.getControl().setBackground(displayColoring.getAnnotationBarColor());
			}

			if (overviewRuler != null && changeColorOfRightRuler) {
				overviewRuler.getHeaderControl().setBackground(displayColoring.getAnnotationBarColor());
				overviewRuler.getControl().setBackground(displayColoring.getAnnotationBarColor());
			}
		} catch (

		Exception e) {
			// coloring will fail for specific editors - so continue for the
			// moment

		}
	}

	private ITextViewer callGetSourceViewer(AbstractTextEditor editor) throws Exception {
		try {
			Method method = AbstractTextEditor.class.getDeclaredMethod("getSourceViewer"); //$NON-NLS-1$
			method.setAccessible(true);

			return (ITextViewer) method.invoke(editor);
		} catch (NullPointerException npe) {
			return null;
		}
	}

	private IOverviewRuler getOverviewRuler(SourceViewer viewer) {
		try {
			Field f = SourceViewer.class.getDeclaredField("fOverviewRuler"); //$NON-NLS-1$
			f.setAccessible(true);
			return (IOverviewRuler) f.get(viewer);
		} catch (Exception err) {
			return null;
		}
	}

	private IVerticalRuler getVerticalRuler(SourceViewer viewer) {
		try {
			Field f = SourceViewer.class.getDeclaredField("fVerticalRuler"); //$NON-NLS-1$
			f.setAccessible(true);
			return (IVerticalRuler) f.get(viewer);
		} catch (Exception err) {
			return null;
		}
	}

}
