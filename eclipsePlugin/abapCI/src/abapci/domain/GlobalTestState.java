package abapci.domain;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

import abapci.feature.FeatureFacade;

public class GlobalTestState {

	public static final String THINK = "THINK";
	public static final String REFACTOR = "REFACTOR";
	public static final String WRITE_TEST = "WRITE TEST";
	private SourcecodeState sourcecodeState;
	private FeatureFacade featureFacade;

	public GlobalTestState() {
	}

	public GlobalTestState(SourcecodeState sourcecodeState) {
		this.sourcecodeState = sourcecodeState;
		featureFacade = new FeatureFacade();
	}

	public String getTestStateOutputForDashboard() {

		String testStateOutput = "";
		if (featureFacade.getTddModeFeature().isActive()) {
			switch (this.sourcecodeState) {
			case UT_FAIL:
				testStateOutput = "WRITE CODE";
				break;
			case ATC_FAIL:
				testStateOutput = REFACTOR;
				break;
			case OK:
				testStateOutput = WRITE_TEST;
				break;
			case OFFL:
				testStateOutput = THINK;
				break;
			case UNDEF:
			default:
				testStateOutput = "Tests n/a";
				break;
			}

		} else {

			switch (this.sourcecodeState) {
			case UT_FAIL:
				testStateOutput = "Tests fail";
				break;
			case ATC_FAIL:
				testStateOutput = "ATC errors";
				break;
			case OK:
				testStateOutput = "-      OK     -";
				break;
			case OFFL:
				testStateOutput = "No connection";
				break;
			case UNDEF:
			default:
				testStateOutput = "Tests n/a";
				break;
			}

		}
		return testStateOutput;
	}

	public Color getColor() {
		RGB rgbColor = new RGB(211, 211, 211);

		switch (this.sourcecodeState) {
		case UT_FAIL:
			rgbColor = featureFacade.getTestRunFailColorFeature().getColor();
			break;
		case ATC_FAIL:
			rgbColor = featureFacade.getAtcRunFailColorFeature().getColor();
			break;
		case OK:
			rgbColor = featureFacade.getTestRunOkColorFeature().getColor();
			break;
		case UNDEF:
		case OFFL:
		default:
			rgbColor = new RGB(211, 211, 211);
			break;
		}

		return new Color(Display.getCurrent(), rgbColor);
	}

	public void setSourcecodeState(SourcecodeState testState) {
		this.sourcecodeState = testState;

	}

	public void setUnitTeststate(TestState unittestState) {
		switch (unittestState) {
		case OK:
			sourcecodeState = (sourcecodeState == SourcecodeState.ATC_FAIL) ? SourcecodeState.ATC_FAIL
					: SourcecodeState.OK;
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
			sourcecodeState = (sourcecodeState == SourcecodeState.UT_FAIL) ? SourcecodeState.UT_FAIL
					: SourcecodeState.OK;
			break;
		case NOK:
			sourcecodeState = (sourcecodeState == SourcecodeState.UT_FAIL) ? SourcecodeState.UT_FAIL
					: SourcecodeState.ATC_FAIL;
			break;
		case OFFL:
			sourcecodeState = SourcecodeState.OFFL;
			break;
		default:
			sourcecodeState = SourcecodeState.UNDEF;
		}
	}

}
