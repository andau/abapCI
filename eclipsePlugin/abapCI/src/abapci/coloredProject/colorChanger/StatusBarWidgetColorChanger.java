package abapci.coloredProject.colorChanger;

import org.eclipse.swt.graphics.Color;

import abapci.AbapCiPluginHelper;
import abapci.coloredProject.general.IStatusBarWidget;
import abapci.coloredProject.model.projectColor.IProjectColor;
import abapci.utils.ColorChooser;
import abapci.utils.StringUtils;

public class StatusBarWidgetColorChanger extends ColorChanger {
	ColorChooser contrastColorDeterminer = new ColorChooser();
	private final String displayText;

	public StatusBarWidgetColorChanger(IProjectColor projectColor, String displayText) {
		this.projectColor = projectColor;
		this.displayText = displayText;
	}

	@Override
	public void change() {

		final AbapCiPluginHelper abapCiPluginHelper = new AbapCiPluginHelper();
		final IStatusBarWidget statusBarWidget = abapCiPluginHelper.getStatusBarWidget();
		if (statusBarWidget.isVisible()) {
			final Color backgroundColor = StatusBarColorHelper.getColor(projectColor);
			statusBarWidget.setBackgroundColor(backgroundColor);
			statusBarWidget.setTextColor(contrastColorDeterminer.getContrastColor(backgroundColor));
			statusBarWidget.setVisible(true);
			if (displayText != null && !displayText.equals(StringUtils.EMPTY)) {
				statusBarWidget.setText(displayText);
			}
		}
	}
}
