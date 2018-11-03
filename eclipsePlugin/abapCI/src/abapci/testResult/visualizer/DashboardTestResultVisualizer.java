package abapci.testResult.visualizer;

import org.eclipse.swt.graphics.Color;

import abapci.views.AbapCiDashboardView;

public class DashboardTestResultVisualizer implements ITestResultVisualizer {
	AbapCiDashboardView view;

	public DashboardTestResultVisualizer(AbapCiDashboardView abapCiDashboardView) {
		this.view = abapCiDashboardView;
	}

	@Override
	public void setResultVisualizerOutput(ResultVisualizerOutput resultVisualizerOutput) {
		if (view != null) {
			setGlobalTestState(resultVisualizerOutput.getGlobalTestState());
			setBackgroundColor(resultVisualizerOutput.getBackgroundColor());
		}
	}

	private void setGlobalTestState(String globalTestStateString) {
		view.setLabelOverallTestStateText(globalTestStateString);
	}

	private void setBackgroundColor(Color backgroundColor) {
		view.setBackgroundColor(backgroundColor);

	}

}
