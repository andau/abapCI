package abapci.feature.activeFeature;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TddModeFeatureTest extends AbstractFeatureTest {

	@Test
	public void test() {
		TddModeFeature cut = new TddModeFeature();

		testActiveField(cut);

		assertEquals(0, cut.getMinimumRequiredSeconds());
		cut.setMinimumRequiredSeconds(10);
		assertEquals(10, cut.getMinimumRequiredSeconds());
		
	}

}
