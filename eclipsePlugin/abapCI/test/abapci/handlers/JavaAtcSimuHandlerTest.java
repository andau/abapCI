package abapci.handlers;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import abapci.activation.Activation;
import abapci.domain.TestState;
import abapci.testResult.TestResultSummary;

public class JavaAtcSimuHandlerTest {

	private static final String TEST_PROJECT_NAME = "TEST_PROJECT_NAME";
	private static final String TEST_OBJECT_NAME_A = "A_TEST_OBJECT_NAME";
	private static final String TEST_OBJECT_NAME_T = "T_TEST_OBJECT_NAME";
	IProject project = Mockito.mock(IProject.class);

	@Before
	public void before() {
		Mockito.when(project.getName()).thenReturn(TEST_PROJECT_NAME);
	}

	@Test
	public void testJavaAtcSimuHandlerWithOkState() {
		JavaAtcSimuHandler javaAtcSimuHandler = new JavaAtcSimuHandler();
		Set<Activation> activations = new HashSet<>();

		TestResultSummary testResultSummaryObject = javaAtcSimuHandler.executeObjects(project, project.getName(),
				activations);
		assertEquals(project.getName(), testResultSummaryObject.getProject().getName());
		assertEquals(project.getName(), testResultSummaryObject.getPackageName());
		assertEquals(TestState.OK, testResultSummaryObject.getTestResult().getTestState());

		TestResultSummary testResultSummaryPackage = javaAtcSimuHandler.executePackage(project, project.getName(),
				activations);
		assertEquals(project.getName(), testResultSummaryPackage.getProject().getName());
		assertEquals(project.getName(), testResultSummaryPackage.getPackageName());
		assertEquals(TestState.OK, testResultSummaryPackage.getTestResult().getTestState());

	}

	@Test
	public void testJavaAtcSimuHandlerWithAtcError() {
		JavaAtcSimuHandler javaAtcSimuHandler = new JavaAtcSimuHandler();
		Set<Activation> activations = new HashSet<>();

		activations.add(new Activation(TEST_OBJECT_NAME_T, project.getName(), project, null, null));
		activations.add(new Activation(TEST_OBJECT_NAME_A, project.getName(), project, null, null));
		TestResultSummary testResultSummaryWithAtcError = javaAtcSimuHandler.executeObjects(project, project.getName(),
				activations);
		assertEquals(TestState.NOK, testResultSummaryWithAtcError.getTestResult().getTestState());
		assertEquals(1, testResultSummaryWithAtcError.getTestResult().getActiveErrors().size());

	}

}
