package abapci.coloredProject.general;

import static org.junit.Assert.assertEquals;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.junit.Test;
import org.mockito.Mockito;

import abapci.utils.StringUtils;

public class DisplayColorTest {

	private static final RGB TEST_RGB_GENERAL = new RGB(100, 100, 100);
	private static final RGB TEST_RGB_STATUS_BAR = new RGB(11, 100, 100);
	private static final RGB TEST_RGB_LEFT_ANNOTATION_BAR = new RGB(22, 100, 100);
	private static final RGB TEST_RGB_RIGHT_ANNOTATION_BAR = new RGB(22, 100, 100);
	private static final RGB TEST_RGB_TITLE_BAR = new RGB(33, 33, 100);
	private static final RGB TEST_RGB_STATUS_WIDGET_BACKGROUND_COLOR = new RGB(44, 44, 44);

	private static final Color TEST_COLOR_GENERAL = new Color(Display.getCurrent(), TEST_RGB_GENERAL);

	private final IProject project = Mockito.mock(IProject.class);

	@Test
	public void testStandardGeneralDisplayColor() {

		DisplayColor displayColor = new DisplayColor(project, TEST_COLOR_GENERAL, TEST_COLOR_GENERAL,
				TEST_COLOR_GENERAL, TEST_COLOR_GENERAL, TEST_COLOR_GENERAL, StringUtils.EMPTY);

		assertEquals(TEST_RGB_GENERAL, displayColor.getStatusBarColor().getColor().getRGB());
		assertEquals(TEST_RGB_GENERAL, displayColor.getStatusWidgetBackgroundColor().getColor().getRGB());
		assertEquals(TEST_RGB_GENERAL, displayColor.getTitleIconColor().getColor().getRGB());
	}

	@Test
	public void testStandardDisplayColor() {

		Color statusBarColor = new Color(Display.getCurrent(), TEST_RGB_STATUS_BAR);
		Color leftAnnotationBarColor = new Color(Display.getCurrent(), TEST_RGB_LEFT_ANNOTATION_BAR);
		Color rightAnnotationBarColor = new Color(Display.getCurrent(), TEST_RGB_LEFT_ANNOTATION_BAR);
		Color titleBarColor = new Color(Display.getCurrent(), TEST_RGB_TITLE_BAR);
		Color statusWidgetBackgroundColor = new Color(Display.getCurrent(), TEST_RGB_STATUS_WIDGET_BACKGROUND_COLOR);

		DisplayColor displayColor = new DisplayColor(project, statusBarColor, leftAnnotationBarColor,
				rightAnnotationBarColor, titleBarColor, statusWidgetBackgroundColor, StringUtils.EMPTY);
		assertEquals(TEST_RGB_STATUS_BAR, displayColor.getStatusBarColor().getColor().getRGB());
		assertEquals(TEST_RGB_LEFT_ANNOTATION_BAR, displayColor.getLeftAnnotationBarColor().getColor().getRGB());
		assertEquals(TEST_RGB_RIGHT_ANNOTATION_BAR, displayColor.getRightAnnotationBarColor().getColor().getRGB());
		assertEquals(TEST_RGB_TITLE_BAR, displayColor.getTitleIconColor().getColor().getRGB());
		assertEquals(TEST_RGB_STATUS_WIDGET_BACKGROUND_COLOR,
				displayColor.getStatusWidgetBackgroundColor().getColor().getRGB());

	}
}
