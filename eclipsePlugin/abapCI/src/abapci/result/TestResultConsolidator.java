package abapci.result;

import java.util.List;

import abapci.domain.AbapPackageTestState;
import abapci.domain.TestResult;
import abapci.domain.TestResultSummary;

public class TestResultConsolidator {

	public void computeConsolidatedTestResult(TestResultSummary testResultSummary,
			List<AbapPackageTestState> testStates, TestResultType testResultType) {

		for (AbapPackageTestState testState : testStates) {
			if (testState.getPackageName().equals(testResultSummary.getPackageName())) {
				TestResult currentTestResult = testResultType == TestResultType.ATC ? testState.getAtcTestResult()
						: testState.getUnitTestResult();
				TestResult newTestResult = mergeIntoExistingTestResult(currentTestResult,
						testResultSummary.getTestResult());
				if (testResultType == TestResultType.ATC) {
					testState.setAtcTestResult(newTestResult);
				} else {
					testState.setUnitTestResult(newTestResult);
				}
			}
		}

	}

	private TestResult mergeIntoExistingTestResult(TestResult currentTestResult, TestResult newTestResult) {

		TestResult mergedTestResult = new TestResult(true, 0, currentTestResult.getActiveErrors(),
				currentTestResult.getActivatedObjects());

		mergedTestResult.removeActiveErrorsFor(newTestResult.getActivatedObjects());
		mergedTestResult.addErrors(newTestResult.getActiveErrors());

		return mergedTestResult;
	}

}
