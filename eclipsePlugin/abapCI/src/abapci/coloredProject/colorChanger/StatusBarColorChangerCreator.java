package abapci.coloredProject.colorChanger;

import org.eclipse.ui.IEditorPart;

import abapci.coloredProject.model.projectColor.IProjectColor;
import abapci.feature.activeFeature.ColoredProjectFeature;

public class StatusBarColorChangerCreator implements ColorChangerCreator  {

	public ColorChanger create(IEditorPart editorPart, ColoredProjectFeature coloredProjectFeature, IProjectColor projectColor) {
		StatusBarColorChanger statusBarColorChanger = new StatusBarColorChanger(editorPart.getEditorSite().getShell(), projectColor); 
		statusBarColorChanger.setActive(coloredProjectFeature.isChangeStatusBarActive()); 
		return statusBarColorChanger;  
	}
	
}
