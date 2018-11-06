package abapci.feature.activeFeature;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.InvocationTargetException;

import org.junit.Test;

public class ColoredProjectFeatureTest extends AbstractFeatureTest {

	private static final int TITLE_ICON_WIDTH = 30;
	private static final  int TITLE_ICON_HEIGHT = 100;

	@Test
	public void testColoredProjectFeature() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		ColoredProjectFeature cut = new ColoredProjectFeature(); 
		
		assertFalse(cut.isActive()); 
		testFieldMethods(cut, "titleIconActive");
		assertTrue(cut.isActive()); 
	
		cut.setTitleIconHeight(TITLE_ICON_HEIGHT);
		assertEquals(TITLE_ICON_HEIGHT, cut.getTitleIconHeight()); 
		cut.setTitleIconWidth(TITLE_ICON_WIDTH);
		assertEquals(TITLE_ICON_WIDTH, cut.getTitleIconWidth()); 
		
		testFieldMethods(cut, "changeStatusBarActive");
		testFieldMethods(cut, "dialogEnabled"); 
		testFieldMethods(cut,"leftRulerActive"); 
		testFieldMethods(cut, "rightRulerActive"); 
		testFieldMethods(cut, "statusBarWidgetActive"); 
	}
}
