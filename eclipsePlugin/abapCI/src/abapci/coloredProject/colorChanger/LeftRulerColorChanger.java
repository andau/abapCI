package abapci.coloredProject.colorChanger;

import java.lang.reflect.Field;

import org.eclipse.jface.text.source.IOverviewRuler;
import org.eclipse.jface.text.source.OverviewRuler;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.swt.graphics.Color;
import org.eclipse.ui.IEditorPart;

import abapci.Exception.ActiveEditorNotSetException;
import abapci.coloredProject.model.projectColor.IProjectColor;

public class LeftRulerColorChanger extends ARulerColorChanger {

	public LeftRulerColorChanger(IEditorPart editorPart) {
		super(editorPart);
	}

	@Override
	public void change(IProjectColor projectColor) throws ActiveEditorNotSetException {

		if (getTextViewer() != null && projectColor != null && !projectColor.isSuppressed()) {
			OverviewRuler overviewRuler = (OverviewRuler) getOverviewRuler((SourceViewer) getTextViewer());

			if (overviewRuler != null) {
				overviewRuler.getHeaderControl().setBackground(projectColor.getColor());
				overviewRuler.getControl().setBackground(projectColor.getColor());
			}
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
