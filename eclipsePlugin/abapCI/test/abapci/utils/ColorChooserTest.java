package abapci.utils;

import static org.junit.Assert.assertEquals;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.junit.Test;

public class ColorChooserTest {

	Color TEST_COLOR_WHITE = new Color(Display.getCurrent(), new RGB(255, 255, 255));
	Color TEST_COLOR_BLACK = new Color(Display.getCurrent(), new RGB(0, 0, 0));
	Color TEST_COLOR_RED = new Color(Display.getCurrent(), new RGB(255, 0, 0));
	Color TEST_COLOR_EDGE_DARK = new Color(Display.getCurrent(), new RGB(126, 126, 126));
	Color TEST_COLOR_EDGE_LIGHT = new Color(Display.getCurrent(), new RGB(129, 129, 129));

	@Test
	public void testGetContrastColor() {
		ColorChooser cut = new ColorChooser();
		assertEquals(TEST_COLOR_BLACK, cut.getContrastColor(TEST_COLOR_WHITE));
		assertEquals(TEST_COLOR_WHITE, cut.getContrastColor(TEST_COLOR_BLACK));
		assertEquals(TEST_COLOR_BLACK, cut.getContrastColor(TEST_COLOR_RED));
		assertEquals(TEST_COLOR_WHITE, cut.getContrastColor(TEST_COLOR_EDGE_DARK));
		assertEquals(TEST_COLOR_BLACK, cut.getContrastColor(TEST_COLOR_EDGE_LIGHT));
	}

}
