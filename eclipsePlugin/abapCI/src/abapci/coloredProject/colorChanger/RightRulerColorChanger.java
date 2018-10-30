package abapci.coloredProject.colorChanger;

import java.lang.reflect.Field;

import org.eclipse.jface.text.source.CompositeRuler;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.swt.graphics.Color;
import org.eclipse.ui.IEditorPart;

import abapci.Exception.ActiveEditorNotSetException;
import abapci.coloredProject.model.projectColor.IProjectColor;

public class RightRulerColorChanger extends ARulerColorChanger  {

	public RightRulerColorChanger(IEditorPart editorPart) {
		super(editorPart);
	}

	@Override
	public void change(IProjectColor projectColor) throws ActiveEditorNotSetException {

		if (projectColor == null || projectColor.isSuppressed())
			return;

		if (getTextViewer() != null) {

			CompositeRuler verticalRuler = (CompositeRuler) getVerticalRuler((SourceViewer) getTextViewer());

			if (verticalRuler != null) {
				verticalRuler.getControl().setBackground(projectColor.getColor());
			}
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
