package abapci.testResult.visualizer;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.Hyperlink;

import abapci.ci.views.AbapCiDashboardView;
import abapci.domain.AbapPackageTestState;
import abapci.domain.InvalidItem;
import abapci.utils.InvalidItemUtil;

public class DashboardTestResultVisualizer implements ITestResultVisualizer {
	private static final int MAX_NUMBER_OF_INVALID_ITEMS_VISUALIZED = 10;
	AbapCiDashboardView view;

	public DashboardTestResultVisualizer(AbapCiDashboardView abapCiDashboardView) {
		view = abapCiDashboardView;
	}

	@Override
	public void setResultVisualizerOutput(ResultVisualizerOutput resultVisualizerOutput) {
		if (view != null) {
			setGlobalTestState(resultVisualizerOutput.getGlobalTestState());
			setInfoLine(resultVisualizerOutput.getInfoline());
			view.setBackgroundColor(resultVisualizerOutput.getBackgroundColor());
			view.setForegroundColor(resultVisualizerOutput.getForegroundColor());
			rebuildHyperlink(view.getEntireContainer(), view.getOpenErrorHyperlink(),
					resultVisualizerOutput.getAbapPackageTestStates());
		}
	}

	private void setInfoLine(String infoline) {
		view.setInfolineText(infoline);
	}

	private void setGlobalTestState(String globalTestStateString) {
		view.setLabelOverallTestStateText(globalTestStateString);
	}

	private void rebuildHyperlink(Composite container, Hyperlink link,
			List<AbapPackageTestState> abapPackageTestStates) {

		final List<InvalidItem> invalidUnitTestItems = abapPackageTestStates.stream()
				.flatMap(item -> item.getFirstUnitTestErrors().stream()).limit(MAX_NUMBER_OF_INVALID_ITEMS_VISUALIZED)
				.collect(Collectors.<InvalidItem>toList());

		final List<InvalidItem> invalidAtcTestItems = abapPackageTestStates.stream()
				.flatMap(item -> item.getFirstFailedAtcErrors().stream()).limit(MAX_NUMBER_OF_INVALID_ITEMS_VISUALIZED)
				.collect(Collectors.<InvalidItem>toList());

		if (invalidUnitTestItems.size() > 0) {
			link.setVisible(true);
			link.setText(InvalidItemUtil.getOutputForUnitTest(invalidUnitTestItems));
		} else {
			if (invalidAtcTestItems.size() > 0) {
				link.setVisible(true);
				link.setText(InvalidItemUtil.getOutputForAtcTest(invalidAtcTestItems));
			} else {
				link.setVisible(false);
			}

		}
	}

}
