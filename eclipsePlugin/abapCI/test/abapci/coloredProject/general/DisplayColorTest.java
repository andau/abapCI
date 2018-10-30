package abapci.coloredProject.general;

import static org.junit.Assert.*;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.junit.Test;

public class DisplayColorTest {

	private static final RGB TEST_RGB_GENERAL = new RGB(100, 100, 100);
	private static final RGB TEST_RGB_STATUS_BAR = new RGB(11, 100, 100);
	private static final RGB TEST_RGB_ANNOTATION_BAR = new RGB(22, 100, 100);
	private static final RGB TEST_RGB_TITLE_BAR = new RGB(33, 33, 100);
	private static final RGB TEST_RGB_STATUS_WIDGET_BACKGROUND_COLOR = new RGB(44, 44, 44);

	@Test
	public void testStandardGeneralDisplayColor() {
		
		DisplayColor displayColor = new DisplayColor(new Color(Display.getCurrent(), TEST_RGB_GENERAL), false);

		assertEquals(TEST_RGB_GENERAL, displayColor.getStatusBarColor().getColor().getRGB());
		assertEquals(TEST_RGB_GENERAL, displayColor.getStatusWidgetBackgroundColor().getColor().getRGB());
		assertEquals(TEST_RGB_GENERAL, displayColor.getTitleIconColor().getColor().getRGB());
	}

	@Test
	public void testStandardDisplayColor() {

		Color statusBarColor = new Color(Display.getCurrent(), TEST_RGB_STATUS_BAR);
		Color annotationBarColor = new Color(Display.getCurrent(), TEST_RGB_ANNOTATION_BAR);
		Color titleBarColor = new Color(Display.getCurrent(), TEST_RGB_TITLE_BAR);
		Color statusWidgetBackgroundColor = new Color(Display.getCurrent(), TEST_RGB_STATUS_WIDGET_BACKGROUND_COLOR);

		DisplayColor displayColor = new DisplayColor(statusBarColor, annotationBarColor, titleBarColor,
				statusWidgetBackgroundColor);
		assertEquals(TEST_RGB_STATUS_BAR, displayColor.getStatusBarColor().getColor().getRGB()); 
		assertEquals(TEST_RGB_ANNOTATION_BAR, displayColor.getAnnotationBarColor().getColor().getRGB()); 
		assertEquals(TEST_RGB_TITLE_BAR, displayColor.getTitleIconColor().getColor().getRGB()); 
		assertEquals(TEST_RGB_STATUS_WIDGET_BACKGROUND_COLOR, displayColor.getStatusWidgetBackgroundColor().getColor().getRGB()); 

	}
}
