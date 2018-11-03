package abapci.coloredProject.colorChanger;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.eclipse.jface.text.source.IOverviewRuler;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.WorkbenchPart;

import abapci.Exception.ActiveEditorNotSetException;
import abapci.coloredProject.exeption.ProjectColorNotSetException;
import abapci.coloredProject.model.projectColor.IProjectColor;

public class TitleIconColorChanger extends ColorChanger {

	private IEditorPart editorPart;
	private int titleIconWidth;
	private int titleIconHeight;

	public TitleIconColorChanger(IEditorPart editorPart, IProjectColor projectColor, int width, int height) {
		this.editorPart = editorPart;
		this.projectColor = projectColor; 
		this.titleIconWidth = width; 
		this.titleIconHeight = height; 
	}

	@Override
	public void change() throws ActiveEditorNotSetException, ProjectColorNotSetException {

		if (editorPart == null) 
		{
			throw new ActiveEditorNotSetException(); 
		}

		if (projectColor == null) 
		{
			throw new ProjectColorNotSetException(); 
		}

		if (editorPart != null && projectColor != null && !projectColor.isSuppressed()) {

			Image newTitleImage = (Image) getTitleIcon(editorPart);
			GC gc = new GC(newTitleImage);
			gc.setForeground(projectColor.getColor());
			int imageWidth = newTitleImage.getBounds().width;
			
			int rectangleWidth =  imageWidth * titleIconWidth / 100;
			int rectangleHeight = imageWidth * titleIconHeight / 100 ; 
			gc.setLineWidth(rectangleHeight);
			int yDrawingLineCenterPosition = newTitleImage.getBounds().height - gc.getLineWidth() / 2;
			gc.drawLine(imageWidth-rectangleWidth, yDrawingLineCenterPosition, imageWidth, yDrawingLineCenterPosition);
			gc.dispose();
			
			Image freezeTitleImage =  new Image(Display.getCurrent(), newTitleImage, SWT.IMAGE_COPY);
			
			WorkbenchPart workbenchPart = (WorkbenchPart)editorPart; 

			try {

				Field f = WorkbenchPart.class.getDeclaredField("titleImage");
				f.setAccessible(true);
				f.set(workbenchPart, freezeTitleImage);
			
			} catch (IllegalArgumentException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (NoSuchFieldException e) {
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

	private String getTitle(IEditorPart editorPart) {

		try {
			Field f = WorkbenchPart.class.getDeclaredField("title");
			f.setAccessible(true);
			return (String) f.get(editorPart);
		} catch (Exception err) {
			return null;
		}
	}

}
