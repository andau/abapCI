package abapci.coloredProject.colorChanger;

import java.lang.reflect.Field;
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
	public void change() {
		final Runnable task = () -> {
			try {
				changeWithShortDelay();
			} catch (ActiveEditorNotSetException | ProjectColorNotSetException | InterruptedException e) {
				// if an error happens while coloring the editor icons, we go on as this feature
				// is not critical
				e.printStackTrace();
			}
		};
		Display.getDefault().asyncExec(task);
	}

	public void changeWithShortDelay()
			throws ActiveEditorNotSetException, ProjectColorNotSetException, InterruptedException {

		waitSmallDelayUntilEditorIsLoaded();
		if (editorPart == null) {
			throw new ActiveEditorNotSetException();
		}

		if (projectColor == null) {
			throw new ProjectColorNotSetException();
		}

		if (editorPart != null && projectColor != null && !projectColor.isSuppressed()) {

			try {

				final Image currentTitleImage = getTitleIcon(editorPart);

				final Image newTitleImage = new Image(Display.getCurrent(), currentTitleImage, SWT.IMAGE_COPY);
				newTitleImage.setBackground(projectColor.getColor());

				final Field f = Image.class.getDeclaredField("transparentPixel");
				f.setAccessible(true);
				f.set(newTitleImage, 100);

				final GC gc = new GC(newTitleImage);
				gc.setForeground(projectColor.getColor());
				final int imageWidth = newTitleImage.getBounds().width;

				final int rectangleWidth = imageWidth * rectangle.getWidth() / 100;
				final int rectangleHeight = imageWidth * rectangle.getHeight() / 100;
				gc.setLineWidth(rectangleHeight);
				final int yDrawingLineCenterPosition = newTitleImage.getBounds().height - gc.getLineWidth() / 2;
				gc.drawLine(imageWidth - rectangleWidth, yDrawingLineCenterPosition, imageWidth,
						yDrawingLineCenterPosition);
				gc.dispose();

				final WorkbenchPart workbenchPart = (WorkbenchPart) editorPart;
				final Method m = WorkbenchPart.class.getDeclaredMethod("setTitleImage", Image.class);
				m.setAccessible(true);
				m.invoke(workbenchPart, newTitleImage);

			} catch (final Exception e) {
				// when an error occurs while decorating the title icon we move on as this
				// function is not critical
				e.printStackTrace();
			}
		}
	}

	private void waitSmallDelayUntilEditorIsLoaded() throws InterruptedException {
		Thread.sleep(100);
	}

	private Image getTitleIcon(IEditorPart editorPart) {

		try {
			final Field f = WorkbenchPart.class.getDeclaredField("titleImage");
			f.setAccessible(true);
			return (Image) f.get(editorPart);
		} catch (final Exception err) {
			return null;
		}
	}

}
