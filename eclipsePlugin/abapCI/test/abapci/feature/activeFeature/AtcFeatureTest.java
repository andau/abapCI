package abapci.feature.activeFeature;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class AtcFeatureTest extends AbstractFeatureTest {

	private static final String TEST_VARIANT = "TEST_VARIANT";

	@Test
	public void testAtcFeature() {

		AtcFeature cut = new AtcFeature(); 
		testActiveField(cut);

		testFieldMethods(cut, "runActivatedObjects");

		assertEquals(null, cut.getVariant()); 
		cut.setVariant(TEST_VARIANT);
		assertEquals(TEST_VARIANT, cut.getVariant()); 
	}

}
