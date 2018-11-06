package abapci.feature.activeFeature;

import org.junit.Test;

public class JenkinsFeatureTest extends AbstractFeatureTest {

	@Test
	public void testJenkinsFeature() {
		JenkinsFeature cut = new JenkinsFeature(); 
		testActiveField(cut);
	}

}
