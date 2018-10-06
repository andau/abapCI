package abapci.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.source.CompositeRuler;
import org.eclipse.jface.text.source.IOverviewRuler;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.jface.text.source.OverviewRuler;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.texteditor.AbstractTextEditor;

import abapci.domain.UiColor;

public class AnnotationRuleColorChanger {

	public void change(IEditorPart editorPart, UiColor uiColor, boolean changeColorOfLeftRuler,
			boolean changeColorOfRightRuler, boolean changeStatusBar) {
		try {

			Shell currentShell = editorPart.getEditorSite().getShell();

			RGB rgb = ColorToRGBMapper.mapUiColorToTheme(uiColor);

			if (rgb != null && changeStatusBar) {

				setBackgroundForChildren(currentShell, new Color(Display.getDefault(), rgb),
						"org.eclipse.jface.action.StatusLine", 0);

			} else {
				setBackgroundForChildren(currentShell,
						Display.getDefault().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND),
						"org.eclipse.jface.action.StatusLine", 0);
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

	private void setBackgroundForChildren(Composite composite, Color color, String classname, int level) {
		if (level < 3) {
			Control[] children = composite.getChildren();
			for (Control child : children) {

				if (child.getClass().getName().equals(classname)) {
					child.getParent().setBackground(color);
				} else {
					if (child instanceof Composite) {
						setBackgroundForChildren((Composite) child, color, classname, ++level);
					}
					level--;
				}
			}
		}
	}

	private void setBackgroundForLabel(Composite composite, Color color, String classname, int level, String caption) {
		Control[] children = composite.getChildren();
		for (Control child : children) {

			if (child instanceof Label) {
				Label label = (Label) child;
				if (label.getText().equals(caption)) {
					child.getParent().setBackground(color);
				}
			} else {
				if (child instanceof Composite) {
					setBackgroundForLabel((Composite) child, color, classname, ++level, caption);
				}
				level--;
			}
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
