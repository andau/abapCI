package abapci.feature.activeFeature;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class PrettyPrinterFeatureTest extends AbstractFeatureTest {
	String TEST_PREFIX = "TEST_PREFIX";

	@Test
	public void testPrettyPrinterFeature() {
		PrettyPrinterFeature cut = new PrettyPrinterFeature(TEST_PREFIX); 
        assertEquals(TEST_PREFIX,cut.getPrefix()); 

        testActiveField(cut);

        testFieldMethods(cut,"cleanupVariablesEnabled");
	}

}
