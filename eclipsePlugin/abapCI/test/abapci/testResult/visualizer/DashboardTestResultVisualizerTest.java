package abapci.testResult.visualizer;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import abapci.views.AbapCiDashboardView;

public class DashboardTestResultVisualizerTest {

	private static final String TEST_GLOBAL_TESTSTATE = "Example Teststate";
	DashboardTestResultVisualizer cut;
	private AbapCiDashboardView view = Mockito.mock(AbapCiDashboardView.class);
	private Color TEST_COLOR = new Color(Display.getCurrent(), new RGB(100,100,100)); 
	
	@Before
	public void before() 
	{
		cut = new DashboardTestResultVisualizer(view ); 
	}
	
	@Test
	public void testSetResultVisualizerOutput() {
		ResultVisualizerOutput resultVisualizerOutput = new ResultVisualizerOutput();
		resultVisualizerOutput.setGlobalTestState(TEST_GLOBAL_TESTSTATE);
		resultVisualizerOutput.setBackgroundColor(TEST_COLOR );
		cut.setResultVisualizerOutput(resultVisualizerOutput); 
		Mockito.verify(view).setLabelOverallTestStateText("Teststate");  
		Mockito.verify(view).setBackgroundColor(TEST_COLOR);
	}

}
