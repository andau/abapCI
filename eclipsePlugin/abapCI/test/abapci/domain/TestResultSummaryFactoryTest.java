package abapci.domain;

import java.awt.List;
import java.util.ArrayList;
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

import abapci.domain.UnitTestResultSummary;
import abapci.result.TestResultSummaryFactory;

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
	

		UnitTestResultSummary unitTestResultSummary = TestResultSummaryFactory.create(TEST_PACKAGE_NAME, unitResultOkMock);
		Assert.assertEquals(TestState.OK, unitTestResultSummary.getTestState()); 
	}

	
	@Test
	public void testOneError() {
		PowerMockito.when(unitResultWithErrorMock.getAlerts()).thenReturn(Arrays.asList(abapUnitAlertMock));

		UnitTestResultSummary unitTestResultSummary = TestResultSummaryFactory.create(TEST_PACKAGE_NAME, unitResultWithErrorMock);
		Assert.assertEquals(TestState.NOK, unitTestResultSummary.getTestState()); 
	}

	@Test
	public void testOneSubError() {
		PowerMockito.when(abapUnitResultItemMock.getAlerts()).thenReturn(Arrays.asList(abapUnitAlertMock)); 
		PowerMockito.when(unitResultWithErrorMock.getItems()).thenReturn(Arrays.asList(abapUnitResultItemMock));



		UnitTestResultSummary unitTestResultSummary = TestResultSummaryFactory.create(TEST_PACKAGE_NAME, unitResultWithErrorMock);
		Assert.assertEquals(TestState.NOK, unitTestResultSummary.getTestState()); 
	}

	@Test
	public void testOneSubSubError() {
		PowerMockito.when(abapUnitResultItemMock.getAlerts()).thenReturn(Arrays.asList(abapUnitAlertMock)); 
		PowerMockito.when(unitResultWithErrorMock.getItems()).thenReturn(Arrays.asList(abapUnitResultItemMock));

		UnitTestResultSummary unitTestResultSummary = TestResultSummaryFactory.create(TEST_PACKAGE_NAME, unitResultWithErrorMock);
		Assert.assertEquals(TestState.NOK, unitTestResultSummary.getTestState()); 
	}

	@Test
	public void testOneSubSubSubError() {
		PowerMockito.when(abapUnitResultItemSubSubMock.getAlerts()).thenReturn(Arrays.asList(abapUnitAlertMock)); 
		PowerMockito.when(abapUnitResultItemSubMock.getChildItems()).thenReturn(Arrays.asList(abapUnitResultItemSubSubMock));
		PowerMockito.when(abapUnitResultItemMock.getChildItems()).thenReturn(Arrays.asList(abapUnitResultItemSubMock));
		PowerMockito.when(unitResultWithErrorMock.getItems()).thenReturn(Arrays.asList(abapUnitResultItemMock));

		UnitTestResultSummary unitTestResultSummary = TestResultSummaryFactory.create(TEST_PACKAGE_NAME, unitResultWithErrorMock);
		Assert.assertEquals(TestState.NOK, unitTestResultSummary.getTestState()); 
	}

}


