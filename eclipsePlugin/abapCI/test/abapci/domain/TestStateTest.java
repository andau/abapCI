package abapci.domain;

import org.junit.Before;
import org.junit.Test;

public class TestStateTest {
	@Before
	public void before() {
		new GlobalTestState(SourcecodeState.UNDEF);
	}

	@Test
	public void globalTestStateInitializedTestU() {
		//TODO 
		//Assert.assertEquals("Tests n/a", globalTestState.getTestStateOutputForDashboard());
		//Assert.assertEquals(new Color(Display.getDefault(), new RGB(211, 211, 211)), globalTestState.getColor());
	}

}
