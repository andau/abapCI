package abapci.handlers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;

import com.sap.adt.tools.abapsource.abapunit.AbapUnitTask;
import com.sap.adt.tools.abapsource.abapunit.IAbapUnitAlert;
import com.sap.adt.tools.abapsource.abapunit.IAbapUnitResult;
import com.sap.adt.tools.abapsource.abapunit.IAbapUnitService;
import com.sap.adt.tools.abapsource.abapunit.TestRunException;
import com.sap.adt.tools.abapsource.abapunit.services.IAdtServicesFactory;

import abapci.activation.Activation;
import abapci.domain.TestState;
import abapci.testResult.TestResultSummary;

public class AbapUnitHandlerTest {

	private static final String TEST_PACKAGE_NAME = "TEST_PACKAGE_NAME";
	AbapUnitHandler cut;

	IProject project = Mockito.mock(IProject.class);
	AdtServicePluginHelper adtServicePluginHelper = Mockito.mock(AdtServicePluginHelper.class);
	IAdtServicesFactory servicesFactory = Mockito.mock(IAdtServicesFactory.class);
	IAbapUnitService abapUnitService = Mockito.mock(IAbapUnitService.class);
	IAbapUnitResult abapUnitResult = Mockito.mock(IAbapUnitResult.class);

	@Before
	public void before() throws TestRunException {
		cut = new AbapUnitHandler();

		Whitebox.setInternalState(cut, "adtServicePluginHelper", adtServicePluginHelper);

		Mockito.when(adtServicePluginHelper.getServiceFactory()).thenReturn(servicesFactory);
		Mockito.when(servicesFactory.createAbapUnitService(project.getName(), false)).thenReturn(abapUnitService);
		Mockito.when(abapUnitService.executeUnitTests(Mockito.any(AbapUnitTask.class), Mockito.eq(false),
				Mockito.eq(TEST_PACKAGE_NAME))).thenReturn(abapUnitResult);
		Mockito.when(abapUnitResult.getAlerts()).thenReturn(new ArrayList<IAbapUnitAlert>());
	}

	@Test
	public void testExecutePackage() {
		Set<Activation> activations = new HashSet<Activation>();
		TestResultSummary testResultSummary = cut.executePackage(project, TEST_PACKAGE_NAME, activations);
		assertEquals(TEST_PACKAGE_NAME, testResultSummary.getPackageName());
		assertEquals(project.getName(), testResultSummary.getProject().getName());
		assertEquals(0, testResultSummary.getTestResult().getActivatedObjects().size());
		assertEquals(0, testResultSummary.getTestResult().getActiveErrors().size());
		assertEquals(0, testResultSummary.getTestResult().getSuppressedErrors().size());
		assertEquals(null, testResultSummary.getTestResult().getFirstInvalidItem());
		assertEquals(TestState.OK, testResultSummary.getTestResult().getTestState());

	}

	@Test
	public void testExecuteObjects() {
		Set<Activation> activations = new HashSet<Activation>();
		activations.add(new Activation("Testobject", TEST_PACKAGE_NAME, project, null, "ABAP_CLASS_TYPE"));
		TestResultSummary testResultSummary = cut.executeObjects(project, TEST_PACKAGE_NAME, activations);
		assertEquals(1, testResultSummary.getTestResult().getActivatedObjects().size());
		assertEquals(0, testResultSummary.getTestResult().getActiveErrors().size());
	}

}
