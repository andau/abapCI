package abapci.result;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;

import com.sap.adt.tools.abapsource.abapunit.IAbapUnitResult;

import abapci.domain.TestState;
import abapci.domain.UnitTestResultSummary;

@RunWith(PowerMockRunner.class)
public class TestResultSummaryFactoryTest {

	@Mock
	IAbapUnitResult abapUnitResult;

	@Test
	public void emptyAbapUnitResultTest() {
		UnitTestResultSummary unitTestResultSummary = TestResultSummaryFactory.create("TESTPACKAGE", abapUnitResult);
		assertEquals("TESTPACKAGE", unitTestResultSummary.getPackageName()); 
		assertEquals(TestState.OK, unitTestResultSummary.getTestState());
		assertEquals(0, unitTestResultSummary.getNumSuppressedErrors()); 
	}
	
	

}
