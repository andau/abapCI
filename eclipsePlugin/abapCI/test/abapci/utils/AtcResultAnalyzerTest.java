package abapci.utils;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.eclipse.emf.common.util.BasicEList;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import com.sap.adt.atc.model.atcfinding.IAtcFinding;
import com.sap.adt.atc.model.atcfinding.IAtcFindingList;
import com.sap.adt.atc.model.atcobject.IAtcObject;
import com.sap.adt.atc.model.atcobject.IAtcObjectList;
import com.sap.adt.atc.model.atcworklist.IAtcWorklist;

import abapci.domain.Suppression;
import abapci.domain.TestResult;
import abapci.domain.TestState;
import abapci.views.ViewModel;

@SuppressWarnings("unused")
@RunWith(PowerMockRunner.class)
@PrepareForTest({ViewModel.class})
public class AtcResultAnalyzerTest {

	AtcResultAnalyzer atcResultAnalyzer;

	@Mock
	IAtcWorklist atcWorklist;

	@Mock
	IAtcObjectList atcObjectList;

	@Mock
	IAtcObject atcObject;

	@Mock
	IAtcFindingList atcFindingList;

	@Mock
	IAtcFinding emptyAtcFinding;

	@Mock
	IAtcFinding activeAtcFinding;

	@Mock
	IAtcFinding suppressedAtcFinding;

	

	@Before
	public void before() {
		final String SUPPRESSED_FINDING_DESC = "SUPPRESSED_FINDING"; 
		
		PowerMockito.when(atcWorklist.getObjects()).thenReturn(atcObjectList);
		PowerMockito.when(atcWorklist.getObjects().getObject()).thenReturn(new BasicEList<IAtcObject>(Arrays.asList(atcObject)));
		PowerMockito.when(atcObject.getFindings()).thenReturn(atcFindingList);
		
		PowerMockito.when(activeAtcFinding.getPriority()).thenReturn(1);
		
		PowerMockito.when(suppressedAtcFinding.getPriority()).thenReturn(1);
		PowerMockito.when(suppressedAtcFinding.getParentUri()).thenReturn(SUPPRESSED_FINDING_DESC);
		
		ViewModel viewModelInstance = PowerMockito.mock(ViewModel.class);
		Whitebox.setInternalState(ViewModel.class, "INSTANCE", viewModelInstance);
		PowerMockito.when(viewModelInstance.getSuppressions()).thenReturn(Arrays.asList(new Suppression(SUPPRESSED_FINDING_DESC)));
	}

	@Test
	public void emptyFindingTest() {
		PowerMockito.when(atcFindingList.getFinding())
				.thenReturn(new BasicEList<IAtcFinding>(Arrays.asList(emptyAtcFinding)));
		TestResult testResult = AtcResultAnalyzer.getTestResult(atcWorklist);
		assertEquals(TestState.OK, testResult.getTestState());
	}

	@Test
	public void oneActiveFindingTest() {
		PowerMockito.when(atcFindingList.getFinding())
				.thenReturn(new BasicEList<IAtcFinding>(Arrays.asList(activeAtcFinding)));
		TestResult testResult = AtcResultAnalyzer.getTestResult(atcWorklist);
		assertEquals(TestState.NOK, testResult.getTestState());
	}

	@Test
	public void oneSuppressedFindingTest() {
		PowerMockito.when(atcFindingList.getFinding())
				.thenReturn(new BasicEList<IAtcFinding>(Arrays.asList(suppressedAtcFinding)));
		TestResult testResult = AtcResultAnalyzer.getTestResult(atcWorklist);
		assertEquals(TestState.OK, testResult.getTestState());
	}

}
