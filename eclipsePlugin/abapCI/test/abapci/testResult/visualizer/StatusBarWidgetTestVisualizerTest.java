package abapci.testResult.visualizer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import abapci.coloredProject.general.StatusBarWidget;
import abapci.domain.AbapPackageTestState;
import abapci.domain.InvalidItem;
import abapci.testResult.TestResult;

public class StatusBarWidgetTestVisualizerTest {

	private static final String TEST_GLOBAL_TESTSTATE = "Example Teststate";
	private static final String TEST_PROJECTNAME = "Testprojectname";
	
	StatusBarWidgetTestVisualizer cut;
	
	private StatusBarWidget statusBarWidget = Mockito.mock(StatusBarWidget.class);
	private IProject project = Mockito.mock(IProject.class); 
	private TestResult unitTestResult = Mockito.mock(TestResult.class); 
    private TestResult atcTestResult = Mockito.mock(TestResult.class);
	private Color TEST_COLOR = new Color(Display.getCurrent(), new RGB(100,100,100)); 
	
	@Before
	public void before() 
	{
		cut = new StatusBarWidgetTestVisualizer(statusBarWidget ); 

		Mockito.when(project.getName()).thenReturn(TEST_PROJECTNAME); 
		Mockito.when(unitTestResult.getActiveErrors()).thenReturn(new HashSet<InvalidItem>()); 
		Mockito.when(unitTestResult.getNumItems()).thenReturn(1); 
		
	}
	
	@Test
	public void testSetResultVisualizerOutput() {

		List<AbapPackageTestState> abapPackageTestStates = new ArrayList<AbapPackageTestState>(); 

		ResultVisualizerOutput resultVisualizerOutput = new ResultVisualizerOutput();
		resultVisualizerOutput.setGlobalTestState(TEST_GLOBAL_TESTSTATE);
		resultVisualizerOutput.setBackgroundColor(TEST_COLOR );
		resultVisualizerOutput.setCurrentProject(project);
		resultVisualizerOutput.setAbapPackageTestStates(abapPackageTestStates);
		cut.setResultVisualizerOutput(resultVisualizerOutput); 
		Mockito.verify(statusBarWidget).setText(TEST_GLOBAL_TESTSTATE +  ";     " + resultVisualizerOutput.getInfoline());  
		Mockito.verify(statusBarWidget).setBackgroundColor(TEST_COLOR);

		abapPackageTestStates.add(new AbapPackageTestState(project.getName(), project.getName(), null, unitTestResult, atcTestResult )); 
		resultVisualizerOutput.setAbapPackageTestStates(abapPackageTestStates);
		Mockito.reset(statusBarWidget);
		cut.setResultVisualizerOutput(resultVisualizerOutput); 
		Mockito.verify(statusBarWidget).setText(TEST_GLOBAL_TESTSTATE +  ";     " + resultVisualizerOutput.getInfoline());  
		Mockito.verify(statusBarWidget).setBackgroundColor(TEST_COLOR);

		abapPackageTestStates.add(new AbapPackageTestState(project.getName(), project.getName(), null, unitTestResult, atcTestResult )); 
		resultVisualizerOutput.setAbapPackageTestStates(abapPackageTestStates);
		resultVisualizerOutput.setShowAtcInfo(true);
		Mockito.reset(statusBarWidget);
		cut.setResultVisualizerOutput(resultVisualizerOutput); 
		Mockito.verify(statusBarWidget).setText(TEST_GLOBAL_TESTSTATE +  ";     " + resultVisualizerOutput.getInfoline());  
		Mockito.verify(statusBarWidget).setBackgroundColor(TEST_COLOR);

	}
}
