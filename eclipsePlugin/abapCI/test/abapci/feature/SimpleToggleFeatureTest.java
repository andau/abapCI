package abapci.feature;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class SimpleToggleFeatureTest {

	@Test
	@Ignore // InitPrefs wird vom Test nicht aufgerufen
	public void testAllToggleFeatureImplemented() {
		FeatureFactory featureFactory = new FeatureFactory();

		for (FeatureType featureType : FeatureType.values()) {
			SimpleToggleFeature simpleToggleFeature = featureFactory.createSimpleToggleFeature(featureType);
			Assert.assertNotNull(simpleToggleFeature);
		}
	}
}
