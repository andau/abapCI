package abapci.coloredProject.general;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;

import abapci.Exception.ActiveEditorNotSetException;
import abapci.coloredProject.colorChanger.ColorChanger;
import abapci.coloredProject.colorChanger.LeftRulerColorChanger;
import abapci.coloredProject.colorChanger.RightRulerColorChanger;
import abapci.coloredProject.colorChanger.StatusBarColorChanger;
import abapci.coloredProject.colorChanger.StatusBarWidgetColorChanger;
import abapci.coloredProject.colorChanger.TitleIconColorChanger;
import abapci.coloredProject.colorChanger.TitleIconOverlayRectangle;
import abapci.coloredProject.exeption.ProjectColorNotSetException;
import abapci.feature.activeFeature.ColoredProjectFeature;

public class DisplayColorChanger {

	public void change(IEditorPart editorPart, DisplayColor displayColor, ColoredProjectFeature coloredProjectFeature) {

		Set<ColorChanger> activeColorChangers = getActiveColorChangers(editorPart, coloredProjectFeature, displayColor);

		for (ColorChanger colorChanger : activeColorChangers) {
			try {
				colorChanger.change();
			} catch (ActiveEditorNotSetException | ProjectColorNotSetException e) {
				// coloring will fail for specific editors
				// as this is no mandatory feature lets continue }
				e.printStackTrace();
			}
		}

	}

	private Set<ColorChanger> getActiveColorChangers(IEditorPart editorPart,
			ColoredProjectFeature coloredProjectFeature, DisplayColor displayColor) {

		Set<ColorChanger> activeColorChangers = new HashSet<>();

		if (!displayColor.getTitleIconColor().isSuppressed()) {
			TitleIconOverlayRectangle rectangle = new TitleIconOverlayRectangle(
					coloredProjectFeature.getTitleIconWidth(), coloredProjectFeature.getTitleIconHeight());
			activeColorChangers.add(new TitleIconColorChanger(editorPart, displayColor.getTitleIconColor(), rectangle));
		}

		if (!displayColor.getStatusBarColor().isSuppressed()) {
			activeColorChangers.add(
					new StatusBarColorChanger(Display.getCurrent().getActiveShell(), displayColor.getStatusBarColor()));
		}

		if (!displayColor.getStatusWidgetBackgroundColor().isSuppressed()) {
			activeColorChangers.add(new StatusBarWidgetColorChanger(displayColor.getStatusWidgetBackgroundColor()));
		}

		if (!displayColor.getLeftAnnotationBarColor().isSuppressed()) {
			activeColorChangers.add(new LeftRulerColorChanger(editorPart, displayColor.getLeftAnnotationBarColor()));
		}

		if (!displayColor.getRightAnnotationBarColor().isSuppressed()) {
			activeColorChangers.add(new RightRulerColorChanger(editorPart, displayColor.getRightAnnotationBarColor()));
		}

		return activeColorChangers;
	}
}
