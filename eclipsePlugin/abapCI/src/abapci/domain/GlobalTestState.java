package abapci.domain;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

import abapci.feature.FeatureFacade;
import abapci.feature.activeFeature.ColorFeature;
import abapci.feature.activeFeature.TddModeFeature;

public class GlobalTestState {

	public static final String THINK = "THINK";
	public static final String REFACTOR = "REFACTOR";
	public static final String WRITE_TEST = "WRITE TEST";
	public static final String WRITE_CODE = "WRITE CODE";
	private SourcecodeState sourcecodeState;

	private ColorFeature testRunFailColorFeature;
	private ColorFeature atcRunFailColorFeature;
	private ColorFeature testRunOkColorFeature;
	private TddModeFeature tddModeFeature;

	public GlobalTestState(SourcecodeState sourcecodeState) {
		this.sourcecodeState = sourcecodeState;
		initFeatures();
	}

	private void initFeatures() {
		FeatureFacade featureFacade = new FeatureFacade();
		testRunFailColorFeature = featureFacade.getTestRunFailColorFeature();
		atcRunFailColorFeature = featureFacade.getAtcRunFailColorFeature();
		testRunOkColorFeature = featureFacade.getTestRunOkColorFeature();
		tddModeFeature = featureFacade.getTddModeFeature();
	}

	public String getTestStateOutputForDashboard() {

		String testStateOutput = "";
		if (tddModeFeature.isActive()) {
			switch (this.sourcecodeState) {
			case UT_FAIL:
				testStateOutput = WRITE_CODE;
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
				testStateOutput = "State N/A";
				break;
			}

		} else {

			switch (this.sourcecodeState) {
			case UT_FAIL:
				testStateOutput = "TESTS FAIL";
				break;
			case ATC_FAIL:
				testStateOutput = "ATC ERRORS";
				break;
			case OK:
				testStateOutput = "OK";
				break;
			case OFFL:
				testStateOutput = "No connection";
				break;
			case UNDEF:
			default:
				testStateOutput = "State n/a";
				break;
			}

		}
		return testStateOutput;
	}

	public Color getColor() {

		RGB rgbColor = new RGB(211, 211, 211);

		switch (this.sourcecodeState) {
		case UT_FAIL:
			rgbColor = testRunFailColorFeature.getColor();
			break;
		case ATC_FAIL:
			rgbColor = atcRunFailColorFeature.getColor();
			break;
		case OK:
			rgbColor = testRunOkColorFeature.getColor();
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
