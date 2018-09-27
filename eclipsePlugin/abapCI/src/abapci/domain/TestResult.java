package abapci.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import abapci.activation.Activation;

public class TestResult {

	private boolean activated;
	private boolean undefined;
	private boolean testrunOk;
	private int numTests;
	private Collection<InvalidItem> invalidItems;
	private Date lastRun;
	private Collection<Activation> activatedObjects;

	public TestResult(boolean testrunOk, int numTests, Collection<InvalidItem> invalidItems,
			Collection<Activation> inactiveObjects) {
		this.activated = true;
		this.testrunOk = testrunOk;
		this.numTests = numTests;
		this.invalidItems = new ArrayList<InvalidItem>(invalidItems);
		this.activatedObjects = inactiveObjects;
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

	public Collection<InvalidItem> getActiveErrors() {
		return invalidItems.stream().filter(item -> !item.isSuppressed()).collect(Collectors.toList());
	}

	public Collection<InvalidItem> getActiveErrors(ErrorPriority priority) {
		return invalidItems.stream().filter(item -> !item.isSuppressed() && priority.equals(item.getPriority()))
				.collect(Collectors.toList());
	}

	public Collection<InvalidItem> getSuppressedErrors() {
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
		return getActiveErrors(ErrorPriority.ERROR).isEmpty() ? null
				: getActiveErrors(ErrorPriority.ERROR).iterator().next();
	}

	public Collection<Activation> getActivatedObjects() {
		return activatedObjects;
	}

	public void removeActiveErrorsFor(Collection<Activation> activations) {
		List<InvalidItem> newInvalidItems = invalidItems.stream().collect(Collectors.toList());
		for (InvalidItem invalidItem : invalidItems) {
			if (activations.stream().anyMatch(
					item -> item.getObjectName().toLowerCase().contains(invalidItem.getClassName().toLowerCase()))) {
				newInvalidItems.remove(invalidItem);
			}
		}

		invalidItems = newInvalidItems;

	}

	public void addErrors(Collection<InvalidItem> activeErrors) {
		invalidItems.addAll(activeErrors);
	}

}
