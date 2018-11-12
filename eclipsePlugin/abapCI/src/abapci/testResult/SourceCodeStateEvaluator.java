package abapci.testResult;

import java.util.List;

import abapci.domain.AbapPackageTestState;
import abapci.domain.SourcecodeState;
import abapci.domain.TestState;
import abapci.feature.activeFeature.UnitFeature;
import abapci.manager.DevelopmentProcessManager;

public class SourceCodeStateEvaluator {
	private final DevelopmentProcessManager developmentProcessManager;

	public SourceCodeStateEvaluator() {

		developmentProcessManager = new DevelopmentProcessManager();
	}

	public SourcecodeState evaluate(List<AbapPackageTestState> abapPackageTestStates, UnitFeature unitFeature) {

		TestState currentUnitTestState;

		if (abapPackageTestStates.isEmpty()) {
			currentUnitTestState = TestState.NO_CONFIG;
		}
		if (!unitFeature.isActive()) {
			currentUnitTestState = TestState.DEACT;
		} else if (abapPackageTestStates.stream().anyMatch(item -> item.getUnitTestState().equals(TestState.NOK))) {
			currentUnitTestState = TestState.NOK;
		} else if (abapPackageTestStates.stream().anyMatch(
				item -> item.getUnitTestState().equals(TestState.OFFL) && !unitFeature.isRunActivatedObjectsOnly())) {
			currentUnitTestState = TestState.OFFL;
		} else if (abapPackageTestStates.stream().anyMatch(item -> item.getUnitTestState().equals(TestState.OK))) {
			currentUnitTestState = TestState.OK;
		} else {
			currentUnitTestState = TestState.UNDEF;
		}

		developmentProcessManager.setUnitTeststate(currentUnitTestState);

		TestState currentAtcState = TestState.NO_CONFIG;

		for (AbapPackageTestState testState : abapPackageTestStates) {
			switch (currentAtcState) {
			case NOK:
				// no change as this is the highest state
				break;
			case OFFL:
				currentAtcState = testState.getAtcTestState() == TestState.NOK ? testState.getAtcTestState()
						: currentAtcState;
			case UNDEF:
			case OK:
			case DEACT:
				currentAtcState = testState.getAtcTestState() != TestState.DEACT ? testState.getAtcTestState()
						: currentAtcState;
				break;
			case NO_CONFIG:
				currentAtcState = testState.getAtcTestState();
				break;
			}
		}

		developmentProcessManager.setAtcTeststate(currentAtcState);
		return developmentProcessManager.getSourcecodeState();

	}

}
