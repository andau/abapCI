package abapci.domain;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

public class GlobalTestState {

	private TestState testState;

	public GlobalTestState() {
		this.testState = TestState.UNDEF;
	}

	public GlobalTestState(TestState testState) {
		this.testState = testState; 
	}

	public String getTestStateOutputForDashboard() {
		String testStateOutput = ""; 
		
		switch(this.testState) 
		{
		case UNDEF: 
			testStateOutput = "Unit testrun not completed"; 
			break; 
		case NOK: 
			testStateOutput = "Unit testrun has failures"; 
			break; 
		case OK: 
			testStateOutput = "Unit testrun successful"; 
			break; 
		}
		return testStateOutput; 
	}
		

	public Color getColor() {
		Color colorForTestState = Display.getCurrent().getSystemColor(SWT.COLOR_GRAY);

		switch (this.testState) {
		case UNDEF:
			colorForTestState = Display.getCurrent().getSystemColor(SWT.COLOR_GRAY);
			break;
		case NOK:
			colorForTestState = Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW);
			break;
		case OK:
			colorForTestState = Display.getCurrent().getSystemColor(SWT.COLOR_GREEN);
			break;
		}

		return colorForTestState;
	}

	public void setTestState(TestState testState) {
		this.testState = testState; 
		
	}

}
