package abapci.result;

import java.util.ArrayList;
import java.util.List;

import com.sap.adt.tools.abapsource.abapunit.AbapUnitAlertSeverity;
import com.sap.adt.tools.abapsource.abapunit.AbapUnitResultItemType;
import com.sap.adt.tools.abapsource.abapunit.IAbapUnitAlert;
import com.sap.adt.tools.abapsource.abapunit.IAbapUnitAlertStackEntry;
import com.sap.adt.tools.abapsource.abapunit.IAbapUnitResult;
import com.sap.adt.tools.abapsource.abapunit.IAbapUnitResultItem;

import abapci.domain.InvalidItem;
import abapci.domain.TestResultSummary;
import abapci.views.ViewModel;

public class TestResultSummaryFactory {
	private static final String UNDEFINED_PACKAGE_NAME = null;

	private TestResultSummaryFactory() {
	}

	public static TestResultSummary create(String packageName, IAbapUnitResult abapUnitResult) {
		List<IAbapUnitAlert> criticalAlerts = getCriticalAlerts(abapUnitResult.getAlerts(), false);

		for (IAbapUnitResultItem abapUnitResultItem : abapUnitResult.getItems()) {
			boolean isSuppressed = ViewModel.INSTANCE.getSuppressions().stream()
					.anyMatch(item -> item.getClassName().equals(abapUnitResultItem.getName()));
			criticalAlerts.addAll(getCriticalAlerts(abapUnitResultItem, isSuppressed));
		}

		// TODO Split criticalAlerts into active alerts and suppressed alerts

		List<InvalidItem> invalidItems = new ArrayList<>();
		for (IAbapUnitAlert criticalAlert : criticalAlerts) {
			IAbapUnitAlertStackEntry firstStackEntry = null;
			if (criticalAlert.getStackEntries() != null && !criticalAlert.getStackEntries().isEmpty()) {
				firstStackEntry = criticalAlert.getStackEntries().get(0);
			}
			invalidItems.add(new InvalidItem("", criticalAlert.getTitle(), false, firstStackEntry));

		}

		int numTests = 0;
		for (IAbapUnitResultItem childItem : abapUnitResult.getItems()) {

			if (childItem.getType().equals(AbapUnitResultItemType.TEST_METHOD)) {
				numTests++;
			} else {
				numTests = numTests + (getNumTests(childItem));
			}

		}

		return new TestResultSummary(packageName, true, numTests, invalidItems);
	}

	private static int getNumTests(IAbapUnitResultItem item) {
		int numTests = 0;
		for (IAbapUnitResultItem childItem : item.getChildItems()) {
			if (childItem.getType().equals(AbapUnitResultItemType.TEST_METHOD)) {
				numTests++;
			} else {
				numTests = numTests + (getNumTests(childItem));
			}
		}
		return numTests;
	}

	private static List<IAbapUnitAlert> getCriticalAlerts(IAbapUnitResultItem abapUnitResultItem,
			boolean isSuppressed) {

		List<IAbapUnitAlert> criticalAlerts = getCriticalAlerts(abapUnitResultItem.getAlerts(), isSuppressed);

		for (IAbapUnitResultItem abapUnitResultSubItem : abapUnitResultItem.getChildItems()) {
			criticalAlerts.addAll(getCriticalAlerts(abapUnitResultSubItem, isSuppressed));
		}

		return criticalAlerts;
	}

	private static List<IAbapUnitAlert> getCriticalAlerts(List<IAbapUnitAlert> alerts, boolean isSuppressed) {
		List<IAbapUnitAlert> criticalAlerts = new ArrayList<>();
		for (IAbapUnitAlert alert : alerts) {
			if (alert != null && alert.getSeverity() != AbapUnitAlertSeverity.TOLERABLE
					&& (alert.getTitle() == null || !alert.getTitle().contains("Invalid parameter ID"))) {

				if (!isSuppressed)
					criticalAlerts.add(alert);
			}
		}
		return criticalAlerts;
	}

	public static TestResultSummary createUndefined(String packageName) {
		return new TestResultSummary(packageName, false, 0, new ArrayList<InvalidItem>());
	}

	public static TestResultSummary createOffline(String packageName) {
		return new TestResultSummary(packageName, false, 0, new ArrayList<InvalidItem>());
	}

	public static TestResultSummary createUndefined() {
		return createUndefined(UNDEFINED_PACKAGE_NAME);
	}
}