package abapci.coloredProject.colorChanger;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.part.WorkbenchPart;

import abapci.Exception.ActiveEditorNotSetException;
import abapci.coloredProject.exeption.ProjectColorNotSetException;
import abapci.coloredProject.model.projectColor.IProjectColor;

public class TitleIconColorChanger extends ColorChanger {

	private final IEditorPart editorPart;
	private final TitleIconOverlayRectangle rectangle;

	public TitleIconColorChanger(IEditorPart editorPart, IProjectColor projectColor,
			TitleIconOverlayRectangle rectangle) {
		this.editorPart = editorPart;
		this.projectColor = projectColor;
		this.rectangle = rectangle;
	}

	@Override
	public void change() throws ActiveEditorNotSetException, ProjectColorNotSetException {

		if (editorPart == null) {
			throw new ActiveEditorNotSetException();
		}

		if (projectColor == null) {
			throw new ProjectColorNotSetException();
		}

		if (editorPart != null && projectColor != null && !projectColor.isSuppressed()) {

			try {

				Image currentTitleImage = getTitleIcon(editorPart);

				Image newTitleImage = new Image(Display.getCurrent(), currentTitleImage, SWT.IMAGE_COPY);
				newTitleImage.setBackground(projectColor.getColor());

				Field f = Image.class.getDeclaredField("transparentPixel");
				f.setAccessible(true);
				f.set(newTitleImage, 100);

				GC gc = new GC(newTitleImage);
				gc.setForeground(projectColor.getColor());
				int imageWidth = newTitleImage.getBounds().width;

				int rectangleWidth = imageWidth * rectangle.getWidth() / 100;
				int rectangleHeight = imageWidth * rectangle.getHeight() / 100;
				gc.setLineWidth(rectangleHeight);
				int yDrawingLineCenterPosition = newTitleImage.getBounds().height - gc.getLineWidth() / 2;
				gc.drawLine(imageWidth - rectangleWidth, yDrawingLineCenterPosition, imageWidth,
						yDrawingLineCenterPosition);
				gc.dispose();

				WorkbenchPart workbenchPart = (WorkbenchPart) editorPart;
				Method m = WorkbenchPart.class.getDeclaredMethod("setTitleImage", Image.class);
				m.setAccessible(true);
				m.invoke(workbenchPart, newTitleImage);

				// Field titleImage = WorkbenchPart.class.getDeclaredField("titleImage");
				// titleImage.setAccessible(true);
				// titleImage.set(workbenchPart, newTitleImage);

			} catch (IllegalArgumentException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (NoSuchFieldException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
