package abapci.domain;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestStateTest{
	private GlobalTestState globalTestState; 
	
	@Before
	public void before() {
		globalTestState = new GlobalTestState(SourcecodeState.UNDEF);
	}
	
	@Test
	public void globalTestStateInitializedTestU()
	{
		Assert.assertEquals("Tests n/a", globalTestState.getTestStateOutputForDashboard());
		Assert.assertEquals(Display.getDefault().getSystemColor(SWT.COLOR_GRAY), globalTestState.getColor()); 		
	}
	
	@Test
	public void globalsTestStateChangeTest() {
		globalTestState.setSourcecodeState(SourcecodeState.UT_FAIL); 
		Assert.assertEquals("Tests fail", globalTestState.getTestStateOutputForDashboard());
		Assert.assertEquals(Display.getDefault().getSystemColor(SWT.COLOR_YELLOW), globalTestState.getColor()); 		

		globalTestState.setSourcecodeState(SourcecodeState.OK); 
		Assert.assertEquals("Tests OK", globalTestState.getTestStateOutputForDashboard());
		Assert.assertEquals(Display.getDefault().getSystemColor(SWT.COLOR_GREEN), globalTestState.getColor()); 		

	}

}
