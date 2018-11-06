package abapci.feature.activeFeature;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class AbstractFeatureTest {

	private static final String ACTIVE_PROPERTYNAME = "active";

	protected void testActiveField(ActiveFeature cut) {
		testFieldMethods(cut, ACTIVE_PROPERTYNAME);
	}

	protected void testFieldMethods(ActiveFeature cut, String propertyname) {
		String methodPostfix = propertyname.substring(0, 1).toUpperCase() + propertyname.substring(1);
		Method setMethod;
		try {
			setMethod = cut.getClass().getMethod("set" + methodPostfix, boolean.class);
			Method isMethod = cut.getClass().getMethod("is" + methodPostfix);

			assertFalse((boolean) isMethod.invoke(cut));
			setMethod.invoke(cut, true);
			assertTrue((boolean) isMethod.invoke(cut));
			
		} catch (NoSuchMethodException | SecurityException  | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			fail(String.format("Test of fielMethods for field %s failed", ACTIVE_PROPERTYNAME));
		}

	}
}
