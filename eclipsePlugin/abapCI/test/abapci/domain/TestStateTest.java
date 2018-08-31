package abapci.domain;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestStateTest {
	private GlobalTestState globalTestState;

	@Before
	public void before() {
		globalTestState = new GlobalTestState(SourcecodeState.UNDEF);
	}

	@Test
	public void globalTestStateInitializedTestU() {
		Assert.assertEquals("Tests n/a", globalTestState.getTestStateOutputForDashboard());
		Assert.assertEquals(new Color(Display.getDefault(), new RGB(211, 211, 211)), globalTestState.getColor());
	}

}
