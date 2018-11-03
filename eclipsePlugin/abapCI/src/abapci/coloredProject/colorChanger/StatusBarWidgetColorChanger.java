package abapci.coloredProject.colorChanger;

import abapci.AbapCiPlugin;
import abapci.coloredProject.model.projectColor.IProjectColor;

public class StatusBarWidgetColorChanger extends ColorChanger {

	public StatusBarWidgetColorChanger(IProjectColor projectColor) 
	{
		this.projectColor = projectColor; 
	}
	@Override
	public void change() {
		AbapCiPlugin.getDefault().getStatusBarWidget().setBackgroundColor(StatusBarColorHelper.getColor(projectColor));
	}
}
