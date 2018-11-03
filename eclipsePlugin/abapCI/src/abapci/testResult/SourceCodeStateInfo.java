package abapci.testResult;

import java.time.Duration;
import java.time.LocalDateTime;

import abapci.domain.GlobalTestState;

public class SourceCodeStateInfo {
	private String globalTestState;
	private LocalDateTime stateActiveSince;

	public SourceCodeStateInfo() {
		globalTestState = GlobalTestState.THINK;
		stateActiveSince = LocalDateTime.now();
	}

	public void setSourceCodeState(String globalTestState) {
		if (!this.globalTestState.equals(globalTestState)) {
			this.globalTestState = globalTestState;
			this.stateActiveSince = LocalDateTime.now();
		}
	}

	public int secondsSinceLastStateChange() {
		LocalDateTime now = LocalDateTime.now();
		return (int) Duration.between(stateActiveSince, now).getSeconds();
	}

	public boolean refactorStepIsStillSuggested(int minimumRequiredRefactorTime) {
		return globalTestState.equals(GlobalTestState.REFACTOR)
				&& secondsSinceLastStateChange() < minimumRequiredRefactorTime;
	}

	public boolean nextPlannedStepIsRefactorStep() {
		return globalTestState.equals(GlobalTestState.WRITE_CODE);
	}

}
