package abaci.utils;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;
import com.sap.adt.tools.abapsource.abapunit.IAbapUnitAlert;
import com.sap.adt.tools.abapsource.abapunit.IAbapUnitAlertDetail;

import abapci.utils.AlertDetailMessageExtractor;

public class AlertDetailMessageExtractorTest {
	
	IAbapUnitAlert unitalert; 
	IAbapUnitAlertDetail unitAlertDetailOne;
	IAbapUnitAlertDetail childAlertDetailOne;
	
	@Before
	public void before() {
		
		 unitalert = PowerMockito.mock(IAbapUnitAlert.class);
		 unitAlertDetailOne = PowerMockito.mock(IAbapUnitAlertDetail.class);
		 childAlertDetailOne = PowerMockito.mock(IAbapUnitAlertDetail.class);

		PowerMockito.when(unitalert.getDetails()).thenReturn(Arrays.asList(unitAlertDetailOne));
		PowerMockito.when(unitAlertDetailOne.getText()).thenReturn("MainText");
		PowerMockito.when(unitAlertDetailOne.getChildDetails()).thenReturn(Arrays.asList(childAlertDetailOne));
		PowerMockito.when(childAlertDetailOne.getText()).thenReturn("DetailText");
	}
 
	@Test 
	public void extractMessageForUiTest() {
		String messageForUi = AlertDetailMessageExtractor.extractMessageForUi(unitalert);
		assertEquals("MainText, DetailText", messageForUi);
	}
}
