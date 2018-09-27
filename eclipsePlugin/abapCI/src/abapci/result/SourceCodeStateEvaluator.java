package abapci.result;

import java.util.List;

import abapci.domain.AbapPackageTestState;
import abapci.domain.SourcecodeState;
import abapci.domain.TestState;
import abapci.feature.FeatureFacade;
import abapci.manager.DevelopmentProcessManager;

public class SourceCodeStateEvaluator {
	FeatureFacade featureFacade;
	private DevelopmentProcessManager developmentProcessManager;

	public SourceCodeStateEvaluator() {
		featureFacade = new FeatureFacade();
		developmentProcessManager = new DevelopmentProcessManager();
	}

	public SourcecodeState evaluate(List<AbapPackageTestState> abapPackageTestStates) {

		TestState currentUnitTestState;
		if (!featureFacade.getUnitFeature().isActive()) {
			currentUnitTestState = TestState.DEACT;
		} else if (abapPackageTestStates.stream().anyMatch(item -> item.getUnitTestState().equals(TestState.NOK))) {
			currentUnitTestState = TestState.NOK;
		} else if (abapPackageTestStates.stream().anyMatch(item -> item.getUnitTestState().equals(TestState.OFFL)
				&& !featureFacade.getUnitFeature().isRunActivatedObjectsOnly())) {
			currentUnitTestState = TestState.OFFL;
		} else if (abapPackageTestStates.stream().anyMatch(item -> item.getUnitTestState().equals(TestState.OK))) {
			currentUnitTestState = TestState.OK;
		} else {
			currentUnitTestState = TestState.UNDEF;
		}

		developmentProcessManager.setUnitTeststate(currentUnitTestState);

		TestState currentAtcState = TestState.UNDEF;

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
			}
		}

		developmentProcessManager.setAtcTeststate(currentAtcState);
		return developmentProcessManager.getSourcecodeState();

	}

}
