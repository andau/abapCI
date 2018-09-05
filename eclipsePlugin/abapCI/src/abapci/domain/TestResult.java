package abapci.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class TestResult {

	private boolean activated;
	private boolean undefined;
	private boolean testrunOk;
	private int numTests;
	private List<InvalidItem> invalidItems;
	private Date lastRun;
	private List<ActivationObject> activatedObjects;

	public TestResult(boolean testrunOk, int numTests, List<InvalidItem> invalidItems,
			List<ActivationObject> activatedObjects) {
		this.activated = true;
		this.testrunOk = testrunOk;
		this.numTests = numTests;
		this.invalidItems = invalidItems;
		this.activatedObjects = activatedObjects;
		this.lastRun = Calendar.getInstance().getTime();
	}

	public TestResult(boolean activated) {
		this.activated = activated;
		this.invalidItems = new ArrayList<>();
		this.lastRun = null;
	}

	public TestResult() {
		this.activated = true;
		undefined = true;
		this.invalidItems = new ArrayList<>();
		this.lastRun = null;
	}

	public TestState getTestState() {

		TestState testState;

		if (!activated) {
			testState = TestState.DEACT;
		} else if (undefined) {
			testState = TestState.UNDEF;
		} else if (!testrunOk) {
			testState = TestState.OFFL;
		} else {
			testState = getActiveErrors(ErrorPriority.ERROR).isEmpty() ? TestState.OK : TestState.NOK;
		}

		return testState;
	}

	public List<InvalidItem> getActiveErrors() {
		return invalidItems.stream().filter(item -> !item.isSuppressed()).collect(Collectors.toList());
	}

	public List<InvalidItem> getActiveErrors(ErrorPriority priority) {
		return invalidItems.stream().filter(item -> !item.isSuppressed() && priority.equals(item.getPriority()))
				.collect(Collectors.toList());
	}

	public List<InvalidItem> getSuppressedErrors() {
		return invalidItems.stream().filter(item -> item.isSuppressed()).collect(Collectors.toList());
	}

	public int getNumOk() {
		return numTests - getActiveErrors().size();
	}

	public String getTestResultInfo() {
		return getTestState().toString();
	}

	public Date getLastRun() {
		return lastRun;
	}

	public InvalidItem getFirstInvalidItem() {
		return getActiveErrors(ErrorPriority.ERROR).isEmpty() ? null : getActiveErrors(ErrorPriority.ERROR).get(0);
	}

	public List<ActivationObject> getActivatedObjects() {
		return activatedObjects;
	}

}
