package abapci.coloredProject.colorChanger;

import org.eclipse.swt.graphics.Color;

import abapci.AbapCiPluginHelper;
import abapci.coloredProject.general.IStatusBarWidget;
import abapci.coloredProject.model.projectColor.IProjectColor;
import abapci.utils.ColorChooser;

public class StatusBarWidgetColorChanger extends ColorChanger {
	ColorChooser contrastColorDeterminer = new ColorChooser(); 
	public StatusBarWidgetColorChanger(IProjectColor projectColor) 
	{
		this.projectColor = projectColor; 
	}
	@Override
	public void change() {
		AbapCiPluginHelper abapCiPluginHelper = new AbapCiPluginHelper(); 
		IStatusBarWidget statusBarWidget = abapCiPluginHelper.getStatusBarWidget();
		Color backgroundColor = StatusBarColorHelper.getColor(projectColor); 
		statusBarWidget.setBackgroundColor(backgroundColor);
		statusBarWidget.setTextColor(contrastColorDeterminer.getContrastColor(backgroundColor));
	}
}
