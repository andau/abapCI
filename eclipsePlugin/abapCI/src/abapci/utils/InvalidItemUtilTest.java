package abapci.utils;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import abapci.domain.InvalidItem;

public class InvalidItemUtilTest {

	private static final String CLASS_NAME = "ClassName";
	private static final String CLASS_DESCRIPTION = "Class description";
	private static final String CLASS_DETAIL = "Class detail";
	private static final String CLASS_NAME_2 = "ClassName2";
	private static final String CLASS_DESCRIPTION_2 = "Class description 2";
	private static final String CLASS_DETAIL_2 = "Class detail 2";
	private final InvalidItem invalidItemOne = Mockito.mock(InvalidItem.class);
	private final InvalidItem invalidItemTwo = Mockito.mock(InvalidItem.class);

	String expectedUnitTestOutputOne;
	String expectedUnitTestOutputTwo;

	String expectedAtcOutputOne;
	private String expectedAtcOutputTwo;

	@Before
	public void before() {
		Mockito.when(invalidItemOne.getClassName()).thenReturn(CLASS_NAME);
		Mockito.when(invalidItemOne.getDescription()).thenReturn(CLASS_DESCRIPTION);
		Mockito.when(invalidItemOne.getDetail()).thenReturn(CLASS_DETAIL);

		Mockito.when(invalidItemTwo.getClassName()).thenReturn(CLASS_NAME_2);
		Mockito.when(invalidItemTwo.getDescription()).thenReturn(CLASS_DESCRIPTION_2);
		Mockito.when(invalidItemTwo.getDetail()).thenReturn(CLASS_DETAIL_2);

		expectedAtcOutputOne = invalidItemOne.getClassName() + ": " + invalidItemOne.getDescription();
		expectedAtcOutputTwo = invalidItemTwo.getClassName() + ": " + invalidItemTwo.getDescription();

		expectedUnitTestOutputOne = String.format("%s: %s", invalidItemOne.getClassName(),
				invalidItemOne.getDescription() + "; " + invalidItemOne.getDetail());
		expectedUnitTestOutputTwo = String.format("%s: %s", invalidItemTwo.getClassName(),
				invalidItemTwo.getDescription() + "; " + invalidItemTwo.getDetail());

	}

	@Test
	public void testGetOutputForUnitTestInvalidItem() {

		assertEquals(expectedUnitTestOutputOne, InvalidItemUtil.getOutputForUnitTest(invalidItemOne));
		assertEquals(expectedUnitTestOutputTwo, InvalidItemUtil.getOutputForUnitTest(invalidItemTwo));

	}

	@Test
	public void testGetOutputForUnitTestListOfInvalidItem() {
		final List<InvalidItem> invalidItems = new ArrayList<InvalidItem>();
		invalidItems.add(invalidItemOne);

		String expectedUnitTestListOutput = expectedUnitTestOutputOne + System.getProperty("line.separator");
		assertEquals(expectedUnitTestListOutput, InvalidItemUtil.getOutputForUnitTest(invalidItems));

		invalidItems.add(invalidItemTwo);
		expectedUnitTestListOutput += expectedUnitTestOutputTwo + System.getProperty("line.separator");
		expectedUnitTestListOutput += "There are in total 2 packages with failed unit tests";

		assertEquals(expectedUnitTestListOutput, InvalidItemUtil.getOutputForUnitTest(invalidItems));

	}

	@Test
	public void testGetOutputForAtcTestListOfInvalidItem() {
		final List<InvalidItem> invalidItems = new ArrayList<InvalidItem>();
		invalidItems.add(invalidItemOne);

		String expectedUnitTestListOutput = expectedAtcOutputOne + System.getProperty("line.separator");
		assertEquals(expectedUnitTestListOutput, InvalidItemUtil.getOutputForAtcTest(invalidItems));

		invalidItems.add(invalidItemTwo);
		expectedUnitTestListOutput += expectedAtcOutputTwo + System.getProperty("line.separator");
		expectedUnitTestListOutput += "There are in total 2 packages with ATC findings";

		assertEquals(expectedUnitTestListOutput, InvalidItemUtil.getOutputForAtcTest(invalidItems));
	}

	@Test
	public void testGetOutputForAtcTestInvalidItem() {
		assertEquals(expectedAtcOutputOne, InvalidItemUtil.getOutputForAtcTest(invalidItemOne));
	}

}
