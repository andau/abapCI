package abapci.coloredProject.colorChanger;

import org.eclipse.ui.IEditorPart;

import abapci.feature.ColoredProjectFeature;

public class StatusBarColorChangerCreator implements ColorChangerCreator  {

	public ColorChanger create(IEditorPart editorPart, ColoredProjectFeature coloredProjectFeature) {
		StatusBarColorChanger statusBarColorChanger = new StatusBarColorChanger(editorPart.getEditorSite().getShell()); 
		statusBarColorChanger.setActive(coloredProjectFeature.isChangeStatusBarActive()); 
		return statusBarColorChanger;  
	}
	
}
