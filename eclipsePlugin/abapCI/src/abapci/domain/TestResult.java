package abapci.domain;

import java.util.List;

public class TestResult {

	private boolean undefined;
	private boolean testrunOk;
	private List<InvalidItem> invalidItems;

	public TestResult(boolean testrunOk, List<InvalidItem> invalidItems) {
		this.testrunOk = testrunOk;
		this.invalidItems = invalidItems;
	}

	public TestResult() {
		undefined = true;
	}

	public TestState getTestState() {
		
		if (undefined) {
			return TestState.UNDEF;
		}

		if (!testrunOk) {
			return TestState.OFFL;
		} else {
			return getActiveErrors().isEmpty() ? TestState.OK : TestState.NOK;
		}
	}

	public List<InvalidItem> getActiveErrors() {
		return InvalidItem.filterInvalidItems(invalidItems, InvalidItem.isActive());
	}

	public List<InvalidItem> getSuppressedErrors() {
		return InvalidItem.filterInvalidItems(invalidItems, InvalidItem.isSuppressed());
	}

}
