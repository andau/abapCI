package abapci.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.source.IOverviewRuler;
import org.eclipse.jface.text.source.OverviewRuler;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.texteditor.AbstractTextEditor;

import abapci.domain.UiColor;

public class AnnotationRuleColorChanger {

	public void change(IEditorPart editorPart, UiColor uiColor) {
		try {

			SourceViewer sv = (SourceViewer) callGetSourceViewer((AbstractTextEditor) editorPart);
			RGB rgb = ColorToRGBMapper.mapUiColorToTheme(uiColor);
			OverviewRuler ruler = (OverviewRuler) getOverviewRuler(sv);
			
			if (rgb == null || ruler == null)
				return;

			ruler.getHeaderControl().setBackground(new Color(Display.getDefault(), rgb));
			ruler.getControl().setBackground(new Color(Display.getDefault(), rgb));
		} catch (Exception e) {
			// TODO handle exception
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

}
