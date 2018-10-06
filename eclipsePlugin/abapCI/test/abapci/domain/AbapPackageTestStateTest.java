package abapci.domain;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;

import abapci.result.TestResult;

public class AbapPackageTestStateTest {

	@Test
	public void abapPackageTestStateInitializationTest() {
		AbapPackageTestState abapPackageTestState = new AbapPackageTestState("TESTPROJECT", "TESTPACKAGE");
		assertEquals(TestState.UNDEF, abapPackageTestState.getUnitTestState());
		assertEquals(TestState.UNDEF.toString(), abapPackageTestState.getJenkinsInfo());
		assertEquals(TestState.UNDEF, abapPackageTestState.getAtcTestState());

		Collection<InvalidItem> invalidItems = new ArrayList<InvalidItem>();
		invalidItems.add(new InvalidItem("TESTPACKAGE", "", false, null, "", ErrorPriority.ERROR));
		abapPackageTestState.setUnitTestResult(new TestResult(true, 1, invalidItems, null));
		assertEquals(TestState.NOK, abapPackageTestState.getUnitTestState());
		abapPackageTestState.setUnitTestResult(new TestResult(true, 1, new ArrayList<InvalidItem>(), null));
		assertEquals(TestState.OK, abapPackageTestState.getUnitTestState());

		abapPackageTestState.setAtcTestResult(new TestResult(true, 1, invalidItems, null));
		assertEquals(TestState.NOK, abapPackageTestState.getAtcTestState());

		abapPackageTestState.setJenkinsInfo(TestState.UNDEF.toString());
		assertEquals(TestState.UNDEF.toString(), abapPackageTestState.getJenkinsInfo());

	}

}
