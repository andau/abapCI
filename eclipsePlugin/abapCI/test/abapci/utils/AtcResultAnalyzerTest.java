package abapci.utils;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.eclipse.emf.common.util.BasicEList;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import com.sap.adt.atc.model.atcfinding.IAtcFinding;
import com.sap.adt.atc.model.atcfinding.IAtcFindingList;
import com.sap.adt.atc.model.atcobject.IAtcObject;
import com.sap.adt.atc.model.atcobject.IAtcObjectList;
import com.sap.adt.atc.model.atcworklist.IAtcWorklist;

import abapci.ci.views.ViewModel;
import abapci.domain.Suppression;
import abapci.domain.TestState;
import abapci.testResult.TestResult;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ ViewModel.class })
public class AtcResultAnalyzerTest {

	AtcResultAnalyzer atcResultAnalyzer;

	IAtcWorklist atcWorklist;
	IAtcObjectList atcObjectList;
	IAtcObject atcObject;
	IAtcFindingList atcFindingList;
	IAtcFinding emptyAtcFinding;
	IAtcFinding activeAtcFinding;
	IAtcFinding suppressedAtcFinding;

	@Before
	public void before() {
		final String SUPPRESSED_FINDING_LOC = "SUPPRESSED_FINDING";
		final String SUPPRESSED_FINDING_URI = "\test\testdetail";

		atcWorklist = PowerMockito.mock(IAtcWorklist.class);
		atcObjectList = PowerMockito.mock(IAtcObjectList.class);
		atcObject = PowerMockito.mock(IAtcObject.class);
		atcFindingList = PowerMockito.mock(IAtcFindingList.class);
		emptyAtcFinding = PowerMockito.mock(IAtcFinding.class);
		activeAtcFinding = PowerMockito.mock(IAtcFinding.class);
		suppressedAtcFinding = PowerMockito.mock(IAtcFinding.class);

		PowerMockito.when(atcWorklist.getObjects()).thenReturn(atcObjectList);
		PowerMockito.when(atcWorklist.getObjects().getObject())
				.thenReturn(new BasicEList<>(Arrays.asList(atcObject)));
		PowerMockito.when(atcObject.getFindings()).thenReturn(atcFindingList);

		PowerMockito.when(activeAtcFinding.getPriority()).thenReturn(1);

		PowerMockito.when(suppressedAtcFinding.getPriority()).thenReturn(1);
		PowerMockito.when(suppressedAtcFinding.getLocation()).thenReturn("/" + SUPPRESSED_FINDING_LOC + "/");
		PowerMockito.when(suppressedAtcFinding.getUri()).thenReturn(SUPPRESSED_FINDING_URI);
		ViewModel viewModelInstance = PowerMockito.mock(ViewModel.class);
		Whitebox.setInternalState(ViewModel.class, "INSTANCE", viewModelInstance);
		PowerMockito.when(viewModelInstance.getSuppressions())
				.thenReturn(Arrays.asList(new Suppression(SUPPRESSED_FINDING_LOC)));
	}

	@Test
	@Ignore
	public void oneActiveFindingTest() {
		PowerMockito.when(atcFindingList.getFinding())
				.thenReturn(new BasicEList<>(Arrays.asList(activeAtcFinding)));
		TestResult testResult = AtcResultAnalyzer.getTestResult(atcWorklist, null);
		assertEquals(TestState.NOK, testResult.getTestState());
	}

	@Test
	@Ignore
	public void oneSuppressedFindingTest() {
		PowerMockito.when(atcFindingList.getFinding())
				.thenReturn(new BasicEList<>(Arrays.asList(suppressedAtcFinding)));
		TestResult testResult = AtcResultAnalyzer.getTestResult(atcWorklist, null);
		assertEquals(TestState.OK, testResult.getTestState());
	}

}
