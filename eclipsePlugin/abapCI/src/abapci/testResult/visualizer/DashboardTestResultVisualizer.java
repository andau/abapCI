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

		final List<AbapPackageTestState> packagesWithFailedTests = abapPackageTestStates.stream()
				.filter(item -> item.getFirstFailedUnitTest() != null)
				.collect(Collectors.<AbapPackageTestState>toList());

		final List<AbapPackageTestState> packagesWithFailedAtc = abapPackageTestStates.stream()
				.filter(item -> item.getFirstFailedAtc() != null).collect(Collectors.<AbapPackageTestState>toList());

		if (packagesWithFailedTests.size() > 0) {
			link.setVisible(true);
			final List<InvalidItem> firstFailedTests = packagesWithFailedTests.stream()
					.map(item -> item.getFirstFailedUnitTest()).collect(Collectors.toList());
			link.setText(InvalidItemUtil.getOutputForUnitTest(firstFailedTests));
		} else {
			if (packagesWithFailedAtc.size() > 0) {
				link.setVisible(true);
				final List<InvalidItem> firstFailedTests = packagesWithFailedAtc.stream()
						.map(item -> item.getFirstFailedAtc()).collect(Collectors.toList());
				link.setText(InvalidItemUtil.getOutputForAtcTest(firstFailedTests));
			} else {
				link.setVisible(false);
			}

		}
	}

}
