package abapci.result;

import java.time.Duration;
import java.time.LocalDateTime;

import abapci.domain.SourcecodeState;

public class SourceCodeStateInfo {
	private SourcecodeState sourceCodeState;
	private LocalDateTime stateActiveSince;

	public SourceCodeStateInfo() {
		sourceCodeState = SourcecodeState.UNDEF;
		stateActiveSince = LocalDateTime.now();
	}

	public void setSourceCodeState(SourcecodeState sourcecodeState) {
		if (!this.sourceCodeState.equals(sourcecodeState)) {
			this.sourceCodeState = sourcecodeState;
			this.stateActiveSince = LocalDateTime.now();
		}
	}

	public int secondsSinceLastStateChange() {
		LocalDateTime now = LocalDateTime.now();
		return (int) Duration.between(now, stateActiveSince).getSeconds();
	}
}
