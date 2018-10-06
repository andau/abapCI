package abapci.result;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

import com.sap.adt.tools.abapsource.abapunit.AbapUnitAlertSeverity;
import com.sap.adt.tools.abapsource.abapunit.IAbapUnitAlert;
import com.sap.adt.tools.abapsource.abapunit.IAbapUnitResult;
import com.sap.adt.tools.abapsource.abapunit.IAbapUnitResultItem;

import abapci.domain.TestState;

@RunWith(PowerMockRunner.class)
public class TestResultSummaryFactoryTest {

	final String TEST_PACKAGE_NAME = "TEST_PACKAGE";

	@Mock
	IAbapUnitResult unitResultWithErrorMock;
	@Mock
	IAbapUnitResult unitResultOkMock;
	@Mock
	IAbapUnitAlert abapUnitAlertMock;
	@Mock
	IAbapUnitAlert abapUnitIgnoreAlertMock;
	@Mock
	IAbapUnitResultItem abapUnitResultItemMock;
	@Mock
	IAbapUnitResultItem abapUnitResultItemSubMock;
	@Mock
	IAbapUnitResultItem abapUnitResultItemSubSubMock;

	@Before
	public void before() {
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
