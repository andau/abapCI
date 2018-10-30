package abapci.coloredProject.colorChanger;

import abapci.AbapCiPlugin;
import abapci.coloredProject.model.projectColor.IProjectColor;

public class StatusBarWidgetColorChanger extends ColorChanger {

	@Override
	public void change(IProjectColor projectColor) {
		AbapCiPlugin.getDefault().getStatusBarWidget().setBackgroundColor(StatusBarColorHelper.getColor(projectColor));
	}
}
