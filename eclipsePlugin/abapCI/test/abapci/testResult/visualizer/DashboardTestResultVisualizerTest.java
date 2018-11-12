package abapci.testResult.visualizer;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import abapci.ci.views.AbapCiDashboardView;
import abapci.domain.AbapPackageTestState;
import abapci.domain.ErrorPriority;
import abapci.domain.InvalidItem;
import abapci.testResult.TestResult;

public class DashboardTestResultVisualizerTest {

	private static final String TEST_GLOBAL_TESTSTATE = "Example Teststate";

	private static final String TEST_PROJECTNAME = "Testprojectname";

	
	DashboardTestResultVisualizer cut;
	
	private AbapCiDashboardView view = Mockito.mock(AbapCiDashboardView.class);
	private Hyperlink hyperlink = Mockito.mock(Hyperlink.class); 
	private IProject project = Mockito.mock(IProject.class); 
	private TestResult unitTestResult = Mockito.mock(TestResult.class); 
    private TestResult atcTestResult = Mockito.mock(TestResult.class);

	private Color TEST_COLOR = new Color(Display.getCurrent(), new RGB(100,100,100)); 
	
	@Before
	public void before() 
	{
		cut = new DashboardTestResultVisualizer(view ); 

		Mockito.when(project.getName()).thenReturn(TEST_PROJECTNAME); 
		Mockito.when(view.getOpenErrorHyperlink()).thenReturn(hyperlink); 
		Set<InvalidItem> invalidItems = new HashSet<InvalidItem>(); 
		invalidItems.add(new InvalidItem("Testclassname", "Testdescription", false,  URI.create(""), ErrorPriority.ERROR)); 
		Mockito.when(unitTestResult.getActiveErrors()).thenReturn(invalidItems); 
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
		Mockito.verify(view).setLabelOverallTestStateText(TEST_GLOBAL_TESTSTATE);  
		Mockito.verify(view).setBackgroundColor(TEST_COLOR);
	
		abapPackageTestStates.add(new AbapPackageTestState(project.getName(), project.getName(), null, unitTestResult, atcTestResult )); 
		resultVisualizerOutput.setAbapPackageTestStates(abapPackageTestStates);
		Mockito.reset(view);
		Mockito.when(view.getOpenErrorHyperlink()).thenReturn(hyperlink); 
		cut.setResultVisualizerOutput(resultVisualizerOutput); 
		Mockito.verify(view).setLabelOverallTestStateText(TEST_GLOBAL_TESTSTATE);  
		Mockito.verify(view).setBackgroundColor(TEST_COLOR);

	}

}
