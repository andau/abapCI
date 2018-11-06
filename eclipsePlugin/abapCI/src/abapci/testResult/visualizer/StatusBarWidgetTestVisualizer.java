package abapci.testResult.visualizer;

import abapci.coloredProject.general.StatusBarWidget;

public class StatusBarWidgetTestVisualizer implements ITestResultVisualizer {

	StatusBarWidget statusBarWidget; 
	public StatusBarWidgetTestVisualizer(StatusBarWidget statusBarWidget) {
		this.statusBarWidget = statusBarWidget; 
	}

	@Override
	public void setResultVisualizerOutput(ResultVisualizerOutput resultVisualizerOutput) {
		String statusString = resultVisualizerOutput.getGlobalTestState() + ";     " + resultVisualizerOutput.getInfoline(); 
		statusBarWidget.setText(statusString );
		statusBarWidget.setBackgroundColor(resultVisualizerOutput.getBackgroundColor());
		statusBarWidget.setTextColor(resultVisualizerOutput.getForegroundColor());
	}

}
