package abapci.feature.activeFeature;

import org.junit.Test;

public class AbapGitPackageChangeFeatureTest1 extends AbstractFeatureTest {

	@Test
	public void testAbapGitPackageChangeFeature() {
		final AbapGitFeature cut = new AbapGitFeature();
		testActiveField(cut);
	}

}
