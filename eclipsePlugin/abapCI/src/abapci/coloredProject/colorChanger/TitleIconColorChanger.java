package abapci.coloredProject.colorChanger;

import java.lang.reflect.Field;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.part.WorkbenchPart;

import abapci.Exception.ActiveEditorNotSetException;
import abapci.coloredProject.model.projectColor.IProjectColor;

public class TitleIconColorChanger extends ColorChanger {

	private IEditorPart editorPart;

	public TitleIconColorChanger(IEditorPart editorPart) {
		this.editorPart = editorPart;
	}

	@Override
	public void change(IProjectColor projectColor) throws ActiveEditorNotSetException {

		if (editorPart != null && projectColor != null && !projectColor.isSuppressed()) {
			Image currentTitleImage = (Image) getTitleIcon(editorPart);
			currentTitleImage.setBackground(new Color(Display.getCurrent(), 255, 0, 0));

			GC gc = new GC(currentTitleImage);
			gc.setForeground(projectColor.getColor());
			int imageWidth = currentTitleImage.getBounds().width;
			gc.setLineWidth(imageWidth / 2);
			int yDrawingLineCenterPosition = currentTitleImage.getBounds().height - gc.getLineWidth() / 2;
			gc.drawLine(imageWidth / 2, yDrawingLineCenterPosition, imageWidth, yDrawingLineCenterPosition);
			gc.dispose();
			editorPart.getEditorSite().getShell().layout();
			editorPart.getSite().getShell().redraw();
		} else {
			throw new ActiveEditorNotSetException();
		}
	}

	private Image getTitleIcon(IEditorPart editorPart) {

		try {
			Field f = WorkbenchPart.class.getDeclaredField("titleImage");
			f.setAccessible(true);
			return (Image) f.get(editorPart);
		} catch (Exception err) {
			return null;
		}
	}

}
