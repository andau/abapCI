package abapci.coloredProject.colorChanger;

import static org.junit.Assert.*;
import org.junit.Test;


public class TitleIconOverlayRectangleTest {

	private static final int ICON_OVERLAY_RECTANGLE_HEIGHT = 30;
	private static final int ICON_OVERLAY_RECTANGLE_WIDTH = 20;

	@Test
	public void testTitleIconOverlayRectangle() {
		TitleIconOverlayRectangle cut = new TitleIconOverlayRectangle(ICON_OVERLAY_RECTANGLE_WIDTH,ICON_OVERLAY_RECTANGLE_HEIGHT ); 
		assertEquals(ICON_OVERLAY_RECTANGLE_WIDTH,  cut.getWidth());
		assertEquals(ICON_OVERLAY_RECTANGLE_HEIGHT,  cut.getHeight());
	}

}
