package abapci.domain;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

public class GlobalTestState {

	private SourcecodeState sourcecodeState;

	public GlobalTestState() {
	}

	public GlobalTestState(SourcecodeState sourcecodeState) {
		this.sourcecodeState = sourcecodeState;
	}

	public String getTestStateOutputForDashboard() {
		String testStateOutput = "";

		switch (this.sourcecodeState) {
		case UT_FAIL:
			testStateOutput = "Tests fail";
			break;
		case ATC_FAIL:
			testStateOutput = "ATC findings";
			break;
		case OK:
			testStateOutput = "Tests OK";
			break;
		case OFFL:
			testStateOutput = "No connection";
			break;
		case UNDEF:
		default:
			testStateOutput = "Tests n/a";
			break;
		}
		return testStateOutput;
	}

	public Color getColor() {
		Color colorForTestState;

		switch (this.sourcecodeState) {
		case UT_FAIL:
			colorForTestState = Display.getCurrent().getSystemColor(SWT.COLOR_RED);
			break;
		case ATC_FAIL:
			colorForTestState = Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW);
			break;
		case OK:
			colorForTestState = Display.getCurrent().getSystemColor(SWT.COLOR_GREEN);
			break;
		case UNDEF:
		case OFFL:
		default:
			colorForTestState = Display.getCurrent().getSystemColor(SWT.COLOR_GRAY);
			break;
		}

		return colorForTestState;
	}

	public void setSourcecodeState(SourcecodeState testState) {
		this.sourcecodeState = testState;

	}

	public void setUnitTeststate(TestState unittestState) {
		switch (unittestState) {
		case OK:
			sourcecodeState = (sourcecodeState == SourcecodeState.ATC_FAIL) ? SourcecodeState.ATC_FAIL : SourcecodeState.OK;    
			break;
		case NOK:
			sourcecodeState = SourcecodeState.UT_FAIL;
			break;
		case OFFL:
			sourcecodeState = SourcecodeState.OFFL;
			break;
		default:
			sourcecodeState = SourcecodeState.UNDEF;
		}		
	}

	public void setAtcTeststate(TestState unittestState) {
		switch (unittestState) {
		case OK:
			sourcecodeState = (sourcecodeState == SourcecodeState.UT_FAIL) ? SourcecodeState.UT_FAIL : SourcecodeState.OK;    
			break;
		case NOK:
			sourcecodeState = (sourcecodeState == SourcecodeState.UT_FAIL) ? SourcecodeState.UT_FAIL : SourcecodeState.ATC_FAIL;    
			break;
		case OFFL:
			sourcecodeState = SourcecodeState.OFFL;
			break;
		default:
			sourcecodeState = SourcecodeState.UNDEF;
		}		
	}

}
