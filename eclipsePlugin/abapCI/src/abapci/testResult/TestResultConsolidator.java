package abapci.testResult;

import java.util.List;

import abapci.domain.AbapPackageTestState;

public class TestResultConsolidator {

	public void computeConsolidatedTestResult(TestResultSummary testResultSummary,
			List<AbapPackageTestState> testStates, TestResultType testResultType) {

		for (AbapPackageTestState testState : testStates) {
			if (testState.getProjectName().equals(testResultSummary.getProject().getName())
					&& testState.getPackageName().equals(testResultSummary.getPackageName())) {
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

		TestResult mergedTestResult = new TestResult(true, currentTestResult.getNumItems(),
				currentTestResult.getActiveErrors(), currentTestResult.getActivatedObjects());

		mergedTestResult.removeActiveErrorsFor(newTestResult.getActivatedObjects());
		mergedTestResult.addErrors(newTestResult.getActiveErrors());
		mergedTestResult.addMissingItemsCount(newTestResult.getActivatedObjects());

		return mergedTestResult;
	}

}
