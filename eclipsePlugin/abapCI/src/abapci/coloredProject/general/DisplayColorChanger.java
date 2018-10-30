package abapci.coloredProject.general;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import abapci.coloredProject.colorChanger.StatusBarColorChanger;
import abapci.coloredProject.colorChanger.ColorChanger;
import abapci.coloredProject.colorChanger.LeftRulerColorChanger;
import abapci.coloredProject.colorChanger.RightRulerColorChanger;
import abapci.coloredProject.colorChanger.StatusBarWidgetColorChanger;
import abapci.coloredProject.colorChanger.TitleIconColorChanger;
import abapci.feature.ColoredProjectFeature;

public class DisplayColorChanger {

	public void change(IEditorPart editorPart, DisplayColor displayColor, ColoredProjectFeature coloredProjectFeature) {

		Set<ColorChanger> activeColorChangers = getActiveColorChangers(editorPart, coloredProjectFeature);

		for (ColorChanger colorChanger : activeColorChangers) {
			try {
				colorChanger.change(displayColor.getStatusBarColor());

			} catch (Exception e) {
				e.printStackTrace();
				// coloring will fail for specific editors
				// as this is no mandatory feature lets continue
			}
		}

	}

	private Set<ColorChanger> getActiveColorChangers(IEditorPart editorPart,
			ColoredProjectFeature coloredProjectFeature) {

		
		Set<ColorChanger> activeColorChangers = new HashSet<>();

		if (coloredProjectFeature.isActive())
			activeColorChangers.add(new TitleIconColorChanger(editorPart));

		if (coloredProjectFeature.isChangeStatusBarActive())
			activeColorChangers.add(new StatusBarColorChanger(Display.getCurrent().getActiveShell()));

		if (coloredProjectFeature.isChangeStatusBarActive())
			activeColorChangers.add(new StatusBarWidgetColorChanger());

		if (coloredProjectFeature.isLeftRulerActive())
			activeColorChangers.add(new LeftRulerColorChanger(editorPart));

		if (coloredProjectFeature.isRightRulerActive())
			activeColorChangers.add(new RightRulerColorChanger(editorPart));

		return activeColorChangers;
	}
}
