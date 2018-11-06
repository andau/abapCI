package abapci.testResult;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

import com.sap.adt.tools.abapsource.abapunit.AbapUnitAlertSeverity;
import com.sap.adt.tools.abapsource.abapunit.IAbapUnitAlert;
import com.sap.adt.tools.abapsource.abapunit.IAbapUnitResult;
import com.sap.adt.tools.abapsource.abapunit.IAbapUnitResultItem;

import abapci.domain.TestState;

public class TestResultSummaryFactoryTest {

	final String TEST_PACKAGE_NAME = "TEST_PACKAGE";

	IAbapUnitResult unitResultWithErrorMock;
	IAbapUnitResult unitResultOkMock;
	IAbapUnitAlert abapUnitAlertMock;
	IAbapUnitAlert abapUnitIgnoreAlertMock;
	IAbapUnitResultItem abapUnitResultItemMock;
	IAbapUnitResultItem abapUnitResultItemSubMock;
	IAbapUnitResultItem abapUnitResultItemSubSubMock;

	@Before
	public void before() {
		unitResultWithErrorMock = Mockito.mock(IAbapUnitResult.class);
		unitResultOkMock = Mockito.mock(IAbapUnitResult.class);
		abapUnitAlertMock = Mockito.mock(IAbapUnitAlert.class);
		abapUnitIgnoreAlertMock = Mockito.mock(IAbapUnitAlert.class);
		abapUnitResultItemMock = Mockito.mock(IAbapUnitResultItem.class);
		abapUnitResultItemSubMock = Mockito.mock(IAbapUnitResultItem.class);
		abapUnitResultItemSubSubMock = Mockito.mock(IAbapUnitResultItem.class);

		PowerMockito.when(abapUnitAlertMock.getTitle()).thenReturn("Testalert");
		PowerMockito.when(unitResultWithErrorMock.getAlerts()).thenReturn(Arrays.asList(abapUnitAlertMock));

		PowerMockito.when(abapUnitIgnoreAlertMock.getTitle()).thenReturn("Testalert");
		PowerMockito.when(abapUnitIgnoreAlertMock.getSeverity()).thenReturn(AbapUnitAlertSeverity.TOLERABLE);
		PowerMockito.when(unitResultOkMock.getAlerts()).thenReturn(Arrays.asList(abapUnitIgnoreAlertMock));

	}

	@Test
	public void testNoError() {
		TestResultSummary unitTestResultSummary = TestResultSummaryFactory.create(null, TEST_PACKAGE_NAME,
				unitResultOkMock, null);
		Assert.assertEquals(TestState.OK, unitTestResultSummary.getTestResult().getTestState());
	}

}
