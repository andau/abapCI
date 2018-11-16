package abapci.testResult;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;

import abapci.domain.AbapPackageTestState;
import abapci.domain.InvalidItem;
import abapci.testResult.visualizer.ResultVisualizerOutput;
import abapci.utils.StringUtils;

public class ResultVisualizerOutputTest {

	private static final String NOK_UNIT_INVALID_ITEM_NAME = "NOK_UNIT_INVALID_ITEM_NAME";

	private static final String NOK_UNIT_INVALID_ITEM_DESCRIPTION = "NOK_UNIT_INVALID_ITEM_DESCRIPTION";

	private static final String NOK_UNIT_INVALID_ITEM_DETAIL = "NOK_UNIT_INVALID_ITEM_DETAIL";

	private static final String NOK_ATC_INVALID_ITEM_NAME = "NOK_ATC_INVALID_ITEM";

	private static final String NOK_ATC_INVALID_ITEM_DESCRIPTION = "NOK_ATC_INVALID_ITEM_DESCRIPTION";

	private static final String NOK_ATC_INVALID_ITEM_DETAIL = "NOK_ATC_INVALID_ITEM_DETAIL";

	ResultVisualizerOutput cut;

	private final List<AbapPackageTestState> abapPackageTestStatesForCurrentProject = new ArrayList<AbapPackageTestState>();

	private final AbapPackageTestState okUnitTestStatePackage = Mockito.mock(AbapPackageTestState.class);
	private final TestResult okUnitTestResult = Mockito.mock(TestResult.class);

	private final AbapPackageTestState nokUnitTestStatePackage = Mockito.mock(AbapPackageTestState.class);
	private final TestResult nokUnitTestResult = Mockito.mock(TestResult.class);;
	private final InvalidItem nokUnitInvalidItem = Mockito.mock(InvalidItem.class);

	private final AbapPackageTestState nokAtcTestStatePackage = Mockito.mock(AbapPackageTestState.class);
	private final TestResult nokAtcTestResult = Mockito.mock(TestResult.class);;
	private final InvalidItem nokAtcInvalidItem = Mockito.mock(InvalidItem.class);

	@Before
	public void before() {

		cut = new ResultVisualizerOutput();
		Whitebox.setInternalState(cut, "abapPackageTestStatesForCurrentProject",
				abapPackageTestStatesForCurrentProject);

		Mockito.when(okUnitTestStatePackage.getUnitTestResult()).thenReturn(okUnitTestResult);
		Mockito.when(okUnitTestResult.getActiveErrors()).thenReturn(Collections.emptySet());

		final Set<InvalidItem> nokUnitInvalidItems = new HashSet<InvalidItem>();
		nokUnitInvalidItems.add(nokUnitInvalidItem);

		Mockito.when(nokUnitTestStatePackage.getUnitTestResult()).thenReturn(nokUnitTestResult);
		Mockito.when(nokUnitTestResult.getActiveErrors()).thenReturn(nokUnitInvalidItems);
		Mockito.when(nokUnitInvalidItem.getClassName()).thenReturn(NOK_UNIT_INVALID_ITEM_NAME);
		Mockito.when(nokUnitInvalidItem.getDescription()).thenReturn(NOK_UNIT_INVALID_ITEM_DESCRIPTION);
		Mockito.when(nokUnitInvalidItem.getDetail()).thenReturn(NOK_UNIT_INVALID_ITEM_DETAIL);

		final Set<InvalidItem> nokAtcInvalidItems = new HashSet<InvalidItem>();
		nokAtcInvalidItems.add(nokAtcInvalidItem);

		Mockito.when(nokAtcTestStatePackage.getUnitTestResult()).thenReturn(okUnitTestResult);
		Mockito.when(okUnitTestResult.getActiveErrors()).thenReturn(Collections.emptySet());
		Mockito.when(nokAtcTestStatePackage.getAtcTestResult()).thenReturn(nokAtcTestResult);
		Mockito.when(nokAtcTestResult.getActiveErrors()).thenReturn(nokAtcInvalidItems);
		Mockito.when(nokAtcInvalidItem.getClassName()).thenReturn(NOK_ATC_INVALID_ITEM_NAME);
		Mockito.when(nokAtcInvalidItem.getDescription()).thenReturn(NOK_ATC_INVALID_ITEM_DESCRIPTION);
		Mockito.when(nokAtcInvalidItem.getDetail()).thenReturn(NOK_ATC_INVALID_ITEM_DETAIL);

	}

	@Test
	public void testNoInvalidItems() {

		assertEquals(StringUtils.EMPTY, cut.getTooltip());
	}

	@Test
	public void testFailingUnitTests() {
		final ResultVisualizerOutput cut = new ResultVisualizerOutput();
		abapPackageTestStatesForCurrentProject.add(nokUnitTestStatePackage);
		cut.setAbapPackageTestStates(abapPackageTestStatesForCurrentProject);

		final String expectedString = ResultVisualizerOutput.FAILING_UNIT_TESTS_HEADER + System.lineSeparator()
				+ System.lineSeparator() + NOK_UNIT_INVALID_ITEM_NAME + ": " + NOK_UNIT_INVALID_ITEM_DESCRIPTION + "; "
				+ NOK_UNIT_INVALID_ITEM_DETAIL;
		assertEquals(expectedString, cut.getTooltip());
	}

	@Test
	public void testFailingAtcTests() {
		final ResultVisualizerOutput cut = new ResultVisualizerOutput();
		abapPackageTestStatesForCurrentProject.add(nokAtcTestStatePackage);
		cut.setAbapPackageTestStates(abapPackageTestStatesForCurrentProject);

		final String expectedString = ResultVisualizerOutput.FAILING_ATC_CHECKS_HEADER + System.lineSeparator()
				+ System.lineSeparator() + NOK_ATC_INVALID_ITEM_NAME + ": " + NOK_ATC_INVALID_ITEM_DESCRIPTION;
		assertEquals(expectedString, cut.getTooltip());

	}

}
