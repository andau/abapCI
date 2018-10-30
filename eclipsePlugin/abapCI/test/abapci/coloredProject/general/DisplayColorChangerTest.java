package abapci.coloredProject.general;

import static org.junit.Assert.*;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.part.EditorPart;
import org.junit.Ignore;
import org.junit.Test;

import abapci.feature.ColoredProjectFeature;

public class DisplayColorChangerTest {

	@Test
	public void testStandardChangeWithGeneralColorAndEditorNull() {
		DisplayColorChanger displayColorChanger = new DisplayColorChanger();

		ColoredProjectFeature coloredProjectFeature = new ColoredProjectFeature();
		coloredProjectFeature.setChangeStatusBarActive(true);
		coloredProjectFeature.setLeftRulerActive(true);
		coloredProjectFeature.setRightRulerActive(true);

		IEditorPart editorPart = null;

		DisplayColor displayColor = new DisplayColor(new Color(Display.getCurrent(), new RGB(0, 0, 0)), false);

		displayColorChanger.change(editorPart, displayColor, coloredProjectFeature);
	}

	@Test
	@Ignore
	public void testStandardChangeWithGeneralColor() {
		DisplayColorChanger displayColorChanger = new DisplayColorChanger();

		ColoredProjectFeature coloredProjectFeature = new ColoredProjectFeature();
		coloredProjectFeature.setChangeStatusBarActive(true);
		coloredProjectFeature.setLeftRulerActive(false);
		coloredProjectFeature.setRightRulerActive(false);

		//DisplayColor displayColor = new DisplayColor(new Color(Display.getCurrent(), new RGB(0, 0, 0)));
		//displayColorChanger.change(editorPart, displayColor, coloredProjectFeature);
	}

	@Test
	@Ignore 
	public void testStandardChangeWithParticularColor() {
		DisplayColorChanger displayColorChanger = new DisplayColorChanger();

		ColoredProjectFeature coloredProjectFeature = new ColoredProjectFeature();
		coloredProjectFeature.setChangeStatusBarActive(true);
		coloredProjectFeature.setLeftRulerActive(true);
		coloredProjectFeature.setRightRulerActive(true);

		IEditorPart editorPart = null;

		
		//TODO change with particular colors
		//DisplayColor displayColor = new DisplayColor(new Color(Display.getCurrent(), new RGB(0, 0, 0)));
		//displayColorChanger.change(editorPart, displayColor, coloredProjectFeature);
	}

	

}
