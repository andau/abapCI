package abaci.utils;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

import com.sap.adt.tools.abapsource.abapunit.IAbapUnitAlert;
import com.sap.adt.tools.abapsource.abapunit.IAbapUnitAlertDetail;

import abapci.utils.AlertDetailMessageExtractor;

@RunWith(PowerMockRunner.class)
public class AlertDetailMessageExtractorTest {

	@Mock
	IAbapUnitAlert unitalert;

	@Mock
	IAbapUnitAlertDetail unitAlertDetailOne;

	@Mock
	IAbapUnitAlertDetail childAlertDetailOne;

	@Before
	public void before() {
		PowerMockito.when(unitalert.getDetails()).thenReturn(Arrays.asList(unitAlertDetailOne));
		PowerMockito.when(unitAlertDetailOne.getText()).thenReturn("MainText");
		PowerMockito.when(unitAlertDetailOne.getChildDetails()).thenReturn(Arrays.asList(childAlertDetailOne));
		PowerMockito.when(childAlertDetailOne.getText()).thenReturn("DetailText");
	}

	@org.junit.Test
	public void extractMessageForUiTest() {
		String messageForUi = AlertDetailMessageExtractor.extractMessageForUi(unitalert);
		assertEquals("MainText, DetailText", messageForUi);
	}
}
