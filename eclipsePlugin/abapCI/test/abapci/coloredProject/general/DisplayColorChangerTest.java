package abapci.coloredProject.general;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.junit.Ignore;
import org.junit.Test;

import abapci.feature.activeFeature.ColoredProjectFeature;

public class DisplayColorChangerTest {

	private static final Color RED_COLOR = new Color(Display.getCurrent(), new RGB(255, 0, 0));

	@Test
	public void testStandardChangeWithGeneralColorAndEditorNull() {
		DisplayColorChanger displayColorChanger = new DisplayColorChanger();

		ColoredProjectFeature coloredProjectFeature = new ColoredProjectFeature();
		coloredProjectFeature.setChangeStatusBarActive(true);
		coloredProjectFeature.setLeftRulerActive(true);
		coloredProjectFeature.setRightRulerActive(true);

		IEditorPart editorPart = null;

		DisplayColor displayColor = new DisplayColor(RED_COLOR, coloredProjectFeature);

		displayColorChanger.change(editorPart, displayColor, coloredProjectFeature);
	}

	@Test
	@Ignore
	public void testStandardChangeWithGeneralColor() {
		ColoredProjectFeature coloredProjectFeature = new ColoredProjectFeature();
		coloredProjectFeature.setChangeStatusBarActive(true);
		coloredProjectFeature.setLeftRulerActive(false);
		coloredProjectFeature.setRightRulerActive(false);

	}

	@Test
	@Ignore
	public void testStandardChangeWithParticularColor() {
		new DisplayColorChanger();

		ColoredProjectFeature coloredProjectFeature = new ColoredProjectFeature();
		coloredProjectFeature.setChangeStatusBarActive(true);
		coloredProjectFeature.setLeftRulerActive(true);
		coloredProjectFeature.setRightRulerActive(true);

	}

}