package abapci.coloredProject.colorChanger;

import java.lang.reflect.Field;

import org.eclipse.jface.text.source.IOverviewRuler;
import org.eclipse.jface.text.source.OverviewRuler;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.ui.IEditorPart;

import abapci.Exception.ActiveEditorNotSetException;
import abapci.coloredProject.model.projectColor.IProjectColor;

public class RightRulerColorChanger extends ARulerColorChanger {

	public RightRulerColorChanger(IEditorPart editorPart, IProjectColor projectColor) {
		super(editorPart);
		this.projectColor = projectColor;
	}

	@Override
	public void change() throws ActiveEditorNotSetException {

		if (getTextViewer() != null && projectColor != null && !projectColor.isSuppressed()) {
			final OverviewRuler overviewRuler = (OverviewRuler) getOverviewRuler((SourceViewer) getTextViewer());

			if (overviewRuler != null) {
				// overviewRuler.getHeaderControl().setBackground(projectColor.getColor());
				overviewRuler.getControl().setBackground(projectColor.getColor());
			}
		}
	}

	private IOverviewRuler getOverviewRuler(SourceViewer viewer) {
		try {
			final Field f = SourceViewer.class.getDeclaredField("fOverviewRuler"); //$NON-NLS-1$
			f.setAccessible(true);
			return (IOverviewRuler) f.get(viewer);
		} catch (final Exception err) {
			return null;
		}
	}

}
