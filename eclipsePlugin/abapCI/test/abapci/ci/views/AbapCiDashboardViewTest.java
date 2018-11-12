package abapci.ci.views;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;

import abapci.ci.views.AbapCiDashboardView;

public class AbapCiDashboardViewTest {

	private static final Color TEST_COLOR = new Color(Display.getDefault(), new RGB(255, 0, 0));
	private static final String LBL_OVERALL_INIT_TEXT = "No active test run found";
	private static final String INFOLINE_INIT_TEXT = "Activate Unit testrun or ATC testrun and restart";
	private static final String HYPERLINK_INIT_TEXT = "                                                                                                                ";
	private static final String TEST_LABEL_TEXT = null;
	AbapCiDashboardView cut;

	Shell shell = new Shell();

	Shell parentMock = Mockito.mock(Shell.class);
	Label lblOverallTestState = Mockito.mock(Label.class);
	Label infoline = Mockito.mock(Label.class);
	Hyperlink openErrorHyperlink = Mockito.mock(Hyperlink.class);

	@Before
	public void before() {
		cut = new AbapCiDashboardView();
		Whitebox.setInternalState(cut, "parent", parentMock);
		Whitebox.setInternalState(cut, "lblOverallTestState", lblOverallTestState);
		Whitebox.setInternalState(cut, "infoline", infoline);
		Whitebox.setInternalState(cut, "openErrorHyperlink", openErrorHyperlink);
	}

	@Test
	public void testSetBackgroundColor() {
		cut.setBackgroundColor(TEST_COLOR);
		Mockito.verify(parentMock, times(1)).setBackground(TEST_COLOR);
	}

	@Test
	public void testSetForegroundColor() {
		cut.setForegroundColor(TEST_COLOR);
		Mockito.verify(lblOverallTestState, times(1)).setForeground(TEST_COLOR);
		Mockito.verify(infoline, times(1)).setForeground(TEST_COLOR);
		Mockito.verify(openErrorHyperlink, times(1)).setForeground(TEST_COLOR);

	}

	@Test
	public void createPartControl() {
		cut.createPartControl(shell);
		assertEquals(LBL_OVERALL_INIT_TEXT, ((Label) Whitebox.getInternalState(cut, "lblOverallTestState")).getText());
		assertEquals(INFOLINE_INIT_TEXT, ((Label) Whitebox.getInternalState(cut, "infoline")).getText());
		assertEquals(HYPERLINK_INIT_TEXT, ((Hyperlink) Whitebox.getInternalState(cut, "openErrorHyperlink")).getText());
	}

	@Test
	public void testSetLabelOverallTestStateText() {
		cut.setLabelOverallTestStateText(TEST_LABEL_TEXT);
		Mockito.verify(lblOverallTestState).setText(TEST_LABEL_TEXT);
	}

	@Test
	public void testSetInfolineText() {
		cut.setInfolineText(TEST_LABEL_TEXT);
		Mockito.verify(infoline).setText(TEST_LABEL_TEXT);
	}

}
