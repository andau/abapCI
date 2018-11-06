package abapci.feature.activeFeature;

import org.junit.Test;

public class ThemeColorChangerFeatureTest extends AbstractFeatureTest {

	@Test
	public void test() {

		ThemeColorChangerFeature cut = new ThemeColorChangerFeature(); 
		testActiveField(cut);
	
	}

}
