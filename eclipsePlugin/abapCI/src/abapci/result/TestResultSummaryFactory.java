package abapci.result;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IProject;

import com.sap.adt.tools.abapsource.abapunit.AbapUnitAlertSeverity;
import com.sap.adt.tools.abapsource.abapunit.AbapUnitResultItemType;
import com.sap.adt.tools.abapsource.abapunit.IAbapUnitAlert;
import com.sap.adt.tools.abapsource.abapunit.IAbapUnitAlertStackEntry;
import com.sap.adt.tools.abapsource.abapunit.IAbapUnitResult;
import com.sap.adt.tools.abapsource.abapunit.IAbapUnitResultItem;

import abapci.activation.Activation;
import abapci.domain.ErrorPriority;
import abapci.domain.InvalidItem;
import abapci.utils.AlertDetailMessageExtractor;
import abapci.utils.InvalidItemUtil;
import abapci.views.ViewModel;

public class TestResultSummaryFactory {
	private static final IProject UNDEFINED_PROJECT = null;
	private static final String UNDEFINED_PACKAGE_NAME = null;

	private TestResultSummaryFactory() {
	}

	public static TestResultSummary create(IProject project, String packageName, IAbapUnitResult abapUnitResult,
			Set<Activation> activations) {
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

			String extractedDetailMessage = AlertDetailMessageExtractor.extractMessageForUi(criticalAlert);
			invalidItems.add(new InvalidItem(InvalidItemUtil.extractClassName(firstStackEntry.getDescription()),
					criticalAlert.getTitle(), false, firstStackEntry.getUri(), extractedDetailMessage,
					ErrorPriority.ERROR));
		}

		int numTests = 0;
		for (IAbapUnitResultItem childItem : abapUnitResult.getItems()) {

			if (childItem.getType().equals(AbapUnitResultItemType.TEST_METHOD)) {
				numTests++;
			} else {
				numTests = numTests + (getNumTests(childItem));
			}

		}

		return new TestResultSummary(project, packageName, true, numTests, invalidItems, activations);
	}

	private static String extractUsefulMessage(String detailMessage) {
		return detailMessage.contains("===") ? detailMessage.substring(0, detailMessage.indexOf("===")) : detailMessage;
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

	public static TestResultSummary createUndefined(IProject project, String packageName) {
		return new TestResultSummary(project, packageName, false, 0, new ArrayList<InvalidItem>(),
				new HashSet<Activation>());
	}

	public static TestResultSummary createOffline(IProject project, String packageName) {
		return new TestResultSummary(project, packageName, false, 0, new ArrayList<InvalidItem>(),
				new HashSet<Activation>());
	}

	public static TestResultSummary createUndefined() {
		return createUndefined(UNDEFINED_PROJECT, UNDEFINED_PACKAGE_NAME);
	}

}