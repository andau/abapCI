package abapci.domain;

import static org.junit.Assert.*;

import org.junit.Test;

public class AbapPackageTestStateTest {

	@Test
	public void abapPackageTestStateInitializationTest() {
		AbapPackageTestState abapPackageTestState = new AbapPackageTestState("TESTPACKAGE"); 
		assertEquals(TestState.UNDEF.toString(), abapPackageTestState.getAUnitInfo());
		assertEquals(TestState.UNDEF.toString(), abapPackageTestState.getAtcInfo());
		assertEquals(TestState.UNDEF.toString(), abapPackageTestState.getJenkinsInfo());
		
		abapPackageTestState.setAUnitInfo("3 failed");
		assertEquals("3 failed", abapPackageTestState.getAUnitInfo());
		abapPackageTestState.setAUnitInfo(TestState.OK.toString());
		assertEquals("OK", abapPackageTestState.getAUnitInfo());

		abapPackageTestState.setAtcInfo("3 findings");
		assertEquals("3 findings", abapPackageTestState.getAtcInfo());

		abapPackageTestState.setJenkinsInfo(TestState.UNDEF.toString());
		assertEquals(TestState.UNDEF.toString(), abapPackageTestState.getJenkinsInfo());

	}

}
