package abapci.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.source.CompositeRuler;
import org.eclipse.jface.text.source.IOverviewRuler;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.jface.text.source.OverviewRuler;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.texteditor.AbstractTextEditor;

import abapci.domain.UiColor;

public class AnnotationRuleColorChanger {

	BackgroundColorChanger backgroundColorChanger;

	public AnnotationRuleColorChanger() {
		backgroundColorChanger = new BackgroundColorChanger();
	}

	public void change(IEditorPart editorPart, UiColor uiColor, boolean changeColorOfLeftRuler,
			boolean changeColorOfRightRuler, boolean changeStatusBar) {
		try {

			RGB rgb = ColorToRGBMapper.mapUiColorToTheme(uiColor);

			if (changeStatusBar) {
				backgroundColorChanger.change(editorPart.getEditorSite().getShell(), rgb);
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

			if (rgb == null)
				return;

			if (verticalRuler != null && changeColorOfLeftRuler) {
				verticalRuler.getControl().setBackground(new Color(Display.getDefault(), rgb));
			}

			if (overviewRuler != null && changeColorOfRightRuler) {
				overviewRuler.getHeaderControl().setBackground(new Color(Display.getDefault(), rgb));
				overviewRuler.getControl().setBackground(new Color(Display.getDefault(), rgb));
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
