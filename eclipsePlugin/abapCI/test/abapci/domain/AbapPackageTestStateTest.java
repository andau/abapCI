package abapci.domain;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

public class AbapPackageTestStateTest {

	// TODO Fix Test
	// @Test
	public void abapPackageTestStateInitializationTest() {
		AbapPackageTestState abapPackageTestState = new AbapPackageTestState("TESTPROJECT", "TESTPACKAGE");
		assertEquals(TestState.UNDEF.toString(), abapPackageTestState.getAUnitInfo());
		assertEquals(TestState.UNDEF.toString(), abapPackageTestState.getAtcInfo());
		assertEquals(TestState.UNDEF.toString(), abapPackageTestState.getJenkinsInfo());

		List<InvalidItem> invalidItems = new ArrayList<InvalidItem>();
		invalidItems.add(new InvalidItem("TESTPACKAGE", "", false, null, ""));
		abapPackageTestState.setUnitTestResult(new TestResult(true, 1, invalidItems, null));
		assertEquals("Errors: 1", abapPackageTestState.getAUnitInfo());
		abapPackageTestState.setUnitTestResult(new TestResult(true, 1, new ArrayList<InvalidItem>(), null));
		assertEquals("OK", abapPackageTestState.getAUnitInfo());

		abapPackageTestState.setUnitTestResult(new TestResult(true, 1, invalidItems, null));
		assertEquals("Errors: 1", abapPackageTestState.getAtcInfo());

		abapPackageTestState.setJenkinsInfo(TestState.UNDEF.toString());
		assertEquals(TestState.UNDEF.toString(), abapPackageTestState.getJenkinsInfo());

	}

}
