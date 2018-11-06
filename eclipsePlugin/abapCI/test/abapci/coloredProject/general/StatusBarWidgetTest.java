package abapci.coloredProject.general;

import org.junit.Test;

import abapci.coloredProject.samples.ColoredProjectTestSample;

public class StatusBarWidgetTest {

	private static final String SAMPLE_TEXT = null;
	private static final String SAMPLE_TOOLTIP = null;

	@Test
	public void test() {
		StatusBarWidget statusBarWidget = new StatusBarWidget(); 
		statusBarWidget.setBackgroundColor(ColoredProjectTestSample.getRedProjectColor().getColor());
		statusBarWidget.setText(SAMPLE_TEXT);
		statusBarWidget.setTextColor(ColoredProjectTestSample.getRedProjectColor().getColor());
		statusBarWidget.setToolTip(SAMPLE_TOOLTIP);
	}

}
