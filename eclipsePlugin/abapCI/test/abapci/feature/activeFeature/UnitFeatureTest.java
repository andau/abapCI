package abapci.feature.activeFeature;

import org.junit.Test;

public class UnitFeatureTest extends AbstractFeatureTest {

	@Test
	public void testUnitFeature() {

		UnitFeature cut = new UnitFeature();
		testActiveField(cut);

		testFieldMethods(cut, "runActivatedObjectsOnly");

	}

}
