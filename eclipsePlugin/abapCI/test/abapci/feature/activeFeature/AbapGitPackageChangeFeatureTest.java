package abapci.feature.activeFeature;

import org.junit.Test;

public class AbapGitPackageChangeFeatureTest extends AbstractFeatureTest {

	@Test
	public void testAbapGitPackageChangeFeature() {
		AbapGitPackageChangeFeature  cut = new AbapGitPackageChangeFeature(); 
		testActiveField(cut); 
	}

}
