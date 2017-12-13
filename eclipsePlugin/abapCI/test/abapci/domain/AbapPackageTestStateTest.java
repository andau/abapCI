package abapci.domain;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class AbapPackageTestStateTest {

	@Test
	public void abapPackageTestStateInitializationTest() {
		AbapPackageTestState abapPackageTestState = new AbapPackageTestState("TESTPACKAGE"); 
		assertEquals(TestState.UNDEF.toString(), abapPackageTestState.getAUnitInfo());
		assertEquals(TestState.UNDEF.toString(), abapPackageTestState.getAtcInfo());
		assertEquals(TestState.UNDEF.toString(), abapPackageTestState.getJenkinsInfo());
		
		List<InvalidItem> invalidItems = new ArrayList<InvalidItem>();
		invalidItems.add(new InvalidItem("TESTPACKAGE", "", false)); 
		abapPackageTestState.setAUnitInfo(new TestResult(true, invalidItems));
		assertEquals("Errors: 1", abapPackageTestState.getAUnitInfo());
		abapPackageTestState.setAUnitInfo(new TestResult(true, new ArrayList<InvalidItem>()));
		assertEquals("OK", abapPackageTestState.getAUnitInfo());

		abapPackageTestState.setAUnitInfo(new TestResult(true, invalidItems));
		assertEquals("Errors: 1", abapPackageTestState.getAtcInfo());

		abapPackageTestState.setJenkinsInfo(TestState.UNDEF.toString());
		assertEquals(TestState.UNDEF.toString(), abapPackageTestState.getJenkinsInfo());

	}

}
