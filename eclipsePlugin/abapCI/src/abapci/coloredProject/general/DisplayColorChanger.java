package abapci.coloredProject.general;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import abapci.coloredProject.colorChanger.StatusBarColorChanger;
import abapci.Exception.ActiveEditorNotSetException;
import abapci.coloredProject.colorChanger.ColorChanger;
import abapci.coloredProject.colorChanger.LeftRulerColorChanger;
import abapci.coloredProject.colorChanger.RightRulerColorChanger;
import abapci.coloredProject.colorChanger.StatusBarWidgetColorChanger;
import abapci.coloredProject.colorChanger.TitleIconColorChanger;
import abapci.coloredProject.exeption.ProjectColorNotSetException;
import abapci.coloredProject.model.projectColor.IProjectColor;
import abapci.feature.ColoredProjectFeature;

public class DisplayColorChanger {

	public void change(IEditorPart editorPart, DisplayColor displayColor, ColoredProjectFeature coloredProjectFeature) {

		Set<ColorChanger> activeColorChangers = getActiveColorChangers(editorPart, coloredProjectFeature, displayColor);

		for (ColorChanger colorChanger : activeColorChangers) {
				try {
					colorChanger.change();
				} catch (ActiveEditorNotSetException | ProjectColorNotSetException e) {
					// coloring will fail for specific editors
					// as this is no mandatory feature lets continue		}
					e.printStackTrace();
				} 
		}

	}

	private Set<ColorChanger> getActiveColorChangers(IEditorPart editorPart,
			ColoredProjectFeature coloredProjectFeature, DisplayColor displayColor) {

		
		Set<ColorChanger> activeColorChangers = new HashSet<>();

		if (coloredProjectFeature.isActive() && !displayColor.isSuppressed())
			activeColorChangers.add(new TitleIconColorChanger(editorPart, displayColor.getTitleIconColor(), coloredProjectFeature.getTitleIconWidth(), coloredProjectFeature.getTitleIconHeight()));

		if (coloredProjectFeature.isChangeStatusBarActive())
			activeColorChangers.add(new StatusBarColorChanger(Display.getCurrent().getActiveShell(), displayColor.getStatusBarColor()));

		if (coloredProjectFeature.isStatusBarWidgetActive())
			activeColorChangers.add(new StatusBarWidgetColorChanger(displayColor.getStatusBarColor()));

		if (coloredProjectFeature.isLeftRulerActive())
			activeColorChangers.add(new LeftRulerColorChanger(editorPart, displayColor.getAnnotationBarColor()));

		if (coloredProjectFeature.isRightRulerActive())
			activeColorChangers.add(new RightRulerColorChanger(editorPart, displayColor.getAnnotationBarColor()));

		return activeColorChangers;
	}
}
