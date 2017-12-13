package abapci.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class TestResult {

	private boolean undefined;
	private boolean testrunOk;
	private List<InvalidItem> invalidItems;
	private Date lastRun;

	public TestResult(boolean testrunOk, List<InvalidItem> invalidItems) {
		this.testrunOk = testrunOk;
		this.invalidItems = invalidItems;
		this.lastRun = Calendar.getInstance().getTime(); 
	}

	public TestResult() {
		undefined = true;
		this.invalidItems = new ArrayList<>(); 
		this.lastRun = Calendar.getInstance().getTime(); 
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
		return invalidItems.stream().filter(item -> !item.isSuppressed()).collect(Collectors.toList());
	}

	public List<InvalidItem> getSuppressedErrors() {
		return invalidItems.stream().filter(item -> item.isSuppressed()).collect(Collectors.toList());
	}

	public String getTestResultInfo() {
		if (getTestState() == TestState.NOK) 
		{
			return "Errors: " + getActiveErrors().size(); 
		}
		else 
		{
			return getTestState().toString(); 
		}
	}

	public Date getLastRun() {
		return lastRun;
	}

}
