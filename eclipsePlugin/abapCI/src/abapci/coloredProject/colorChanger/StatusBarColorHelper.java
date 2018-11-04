package abapci.coloredProject.colorChanger;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

import abapci.coloredProject.model.projectColor.IProjectColor;

public class StatusBarColorHelper { 

	public static Color getColor(IProjectColor projectColor) {
		return (projectColor == null || projectColor.isSuppressed())
				? Display.getDefault().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND)
				: projectColor.getColor();
	}

}
