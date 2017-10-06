package abapci.result;

import java.util.ArrayList;
import java.util.List;
import com.sap.adt.tools.abapsource.abapunit.AbapUnitAlertSeverity;
import com.sap.adt.tools.abapsource.abapunit.IAbapUnitAlert;
import com.sap.adt.tools.abapsource.abapunit.IAbapUnitResult;
import com.sap.adt.tools.abapsource.abapunit.IAbapUnitResultItem;
import abapci.domain.UnitTestResultSummary;
import abapci.domain.TestState;

public class TestResultSummaryFactory {
	private static final String UNDEFINED_PACKAGE_NAME = null;

	private TestResultSummaryFactory() {
	}

	public static UnitTestResultSummary create(String packageName, IAbapUnitResult abapUnitResult) {
		int numCritialAlerts = getCriticalAlerts(abapUnitResult.getAlerts()).size();

		for (IAbapUnitResultItem abapUnitResultItem : abapUnitResult.getItems()) {
			numCritialAlerts = numCritialAlerts + getNumCriticalAlerts(abapUnitResultItem);
		}
		TestState testState = numCritialAlerts == 0 ? TestState.OK : TestState.NOK;
		return new UnitTestResultSummary(packageName, testState);
	}

	private static int getNumCriticalAlerts(IAbapUnitResultItem abapUnitResultItem) {
		int numCritialAlerts = getCriticalAlerts(abapUnitResultItem.getAlerts()).size();
		for (IAbapUnitResultItem abapUnitResultSubItem : abapUnitResultItem.getChildItems()) {
			numCritialAlerts = numCritialAlerts + getNumCriticalAlerts(abapUnitResultSubItem);
		}
		return numCritialAlerts;
	}

	private static List<IAbapUnitAlert> getCriticalAlerts(List<IAbapUnitAlert> alerts) {
		List<IAbapUnitAlert> criticalAlerts = new ArrayList<>();
		for (IAbapUnitAlert alert : alerts) {
			if (alert != null && alert.getSeverity() != AbapUnitAlertSeverity.TOLERABLE
					&& (alert.getTitle() == null  || !alert.getTitle().contains("Invalid parameter ID"))) {
				criticalAlerts.add(alert);
			}
		}
		return criticalAlerts;
	}

	public static UnitTestResultSummary createUndefined(String packageName) {
		return new UnitTestResultSummary(packageName, TestState.UNDEF);
	}

	public static UnitTestResultSummary createUndefined() {
		return createUndefined(UNDEFINED_PACKAGE_NAME);
	}
}