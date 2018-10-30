package abapci.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class AbapPackageInfoTest {

	@Test
	public void testAbapPackageInfoInitialized() {
		final String TEST_PACKAGE = "TEST_PACKAGE"; 
		
		AbapPackageInfo abapPackageInfo = new AbapPackageInfo(TEST_PACKAGE); 
		assertNotNull(abapPackageInfo.getJenkinsRunInfo());
		assertNotNull(abapPackageInfo.getAbapUnitRunInfo());
		
		assertEquals(TEST_PACKAGE, abapPackageInfo.getPackageName()); 
		assertEquals("null null", abapPackageInfo.getPackageRunInfos());
	}

}
