package abapci.domain;

import static org.junit.Assert.*;

import abapci.domain.AbapPackageInfo;

public class AbapPackageInfoTest {

	@org.junit.Test
	public void testAbapPackageInfoInitialized() {
		final String TEST_PACKAGE = "TEST_PACKAGE"; 
		
		AbapPackageInfo abapPackageInfo = new AbapPackageInfo(TEST_PACKAGE); 
		assertNotNull(abapPackageInfo.getJenkinsRunInfo());
		assertNotNull(abapPackageInfo.getAbapUnitRunInfo());
		
		assertEquals(TEST_PACKAGE, abapPackageInfo.getPackageName()); 
		assertEquals("null null", abapPackageInfo.getPackageRunInfos());
	}

}
