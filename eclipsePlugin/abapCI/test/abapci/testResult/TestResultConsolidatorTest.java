package abapci.testResult;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;
import abapci.activation.Activation;
import abapci.domain.AbapPackageTestState;
import abapci.domain.ErrorPriority;
import abapci.domain.InvalidItem;
import abapci.domain.TestState;
import abapci.testResult.TestResult;
import abapci.testResult.TestResultConsolidator;
import abapci.testResult.TestResultSummary;
import abapci.testResult.TestResultType;

public class TestResultConsolidatorTest {

	TestResultConsolidator testResultConsolidator;
	List<AbapPackageTestState> testStates;

	TestResult unitTestResult1;
	TestResult atcTestResult1;

	TestResult unitTestResult2;
	TestResult atcTestResult2;

	TestResult newTestResult2;

	IProject project;
	IProject otherProject;

	@Before
	public void before() {
		project = PowerMockito.mock(IProject.class); 
		otherProject = PowerMockito.mock(IProject.class); 
		PowerMockito.when(project.getName()).thenReturn("TESTPROJECT_1");
		PowerMockito.when(otherProject.getName()).thenReturn("OTHER_TESTPROJECT");

		testResultConsolidator = new TestResultConsolidator();
		testStates = new ArrayList<AbapPackageTestState>();

		unitTestResult1 = new TestResult(true, 1, new ArrayList<InvalidItem>(), new ArrayList<Activation>());
		unitTestResult2 = new TestResult(true, 1, new ArrayList<InvalidItem>(), new ArrayList<Activation>());

		atcTestResult1 = new TestResult(true, 1, new ArrayList<InvalidItem>(), new ArrayList<Activation>());
		atcTestResult2 = new TestResult(true, 1, new ArrayList<InvalidItem>(), new ArrayList<Activation>());

		testStates
				.add(new AbapPackageTestState(project.getName(), "TESTPACKAGE_1", "", unitTestResult1, atcTestResult1));
		testStates
				.add(new AbapPackageTestState(project.getName(), "TESTPACKAGE_2", "", unitTestResult2, atcTestResult2));

	}

	@Test
	public void updateSecondPackageStandardTest() throws URISyntaxException {

		Collection<InvalidItem> oneInvalidErrorCollection = new ArrayList<InvalidItem>();
		oneInvalidErrorCollection
				.add(new InvalidItem("TESTCLASS_1", "", false, new URI("/uri/testclass1"), ErrorPriority.ERROR));
		newTestResult2 = new TestResult(true, 1, oneInvalidErrorCollection, new ArrayList<Activation>());
		TestResultSummary testResultSummary = new TestResultSummary(project, "TESTPACKAGE_2", newTestResult2);

		Assert.assertEquals(TestState.OK, testStates.get(0).getUnitTestState());
		Assert.assertEquals(TestState.OK, testStates.get(1).getUnitTestState());

		testResultConsolidator.computeConsolidatedTestResult(testResultSummary, testStates, TestResultType.UNIT);

		Assert.assertEquals(2, testStates.size());

		Assert.assertEquals(TestState.OK, testStates.get(0).getUnitTestState());
		Assert.assertEquals(TestState.NOK, testStates.get(1).getUnitTestState());
	}

	@Test
	public void updateSecondPackageWithWrongProjectTest() throws URISyntaxException {

		Collection<InvalidItem> oneInvalidErrorCollection = new ArrayList<InvalidItem>();
		oneInvalidErrorCollection
				.add(new InvalidItem("TESTCLASS_1", "", false, new URI("/uri/testclass1"), ErrorPriority.ERROR));
		newTestResult2 = new TestResult(true, 1, oneInvalidErrorCollection, new ArrayList<Activation>());
		TestResultSummary testResultSummary = new TestResultSummary(otherProject, "TESTPACKAGE_2", newTestResult2);

		Assert.assertEquals(TestState.OK, testStates.get(0).getUnitTestState());
		Assert.assertEquals(TestState.OK, testStates.get(1).getUnitTestState());

		testResultConsolidator.computeConsolidatedTestResult(testResultSummary, testStates, TestResultType.UNIT);

		Assert.assertEquals(2, testStates.size());

		Assert.assertEquals(TestState.OK, testStates.get(0).getUnitTestState());
		Assert.assertEquals(TestState.OK, testStates.get(1).getUnitTestState());
	}

}
