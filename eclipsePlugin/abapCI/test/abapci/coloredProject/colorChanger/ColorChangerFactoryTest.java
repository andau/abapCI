package abapci.coloredProject.colorChanger;

import static org.junit.Assert.*;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPartSite;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import abapci.coloredProject.exeption.ColorChangerNotImplementedException;
import abapci.feature.ColoredProjectFeature;

public class ColorChangerFactoryTest {

	ColorChangerFactory cut;

	IEditorPart editorPart;	
	IWorkbenchPartSite site;
	Shell shell;
	ColoredProjectFeature coloredProjectFeature;

	@Before
	public void before() throws ColorChangerNotImplementedException {

		editorPart = Mockito.mock(IEditorPart.class);
		site = Mockito.mock(IWorkbenchPartSite.class);
		shell = Mockito.mock(Shell.class);
		Mockito.when(editorPart.getSite()).thenReturn(site);
		Mockito.when(site.getShell()).thenReturn(shell);
		coloredProjectFeature = Mockito.mock(ColoredProjectFeature.class);

		cut = new ColorChangerFactory();

	}

	@Test
	public void testStatusBarColorChanger() throws ColorChangerNotImplementedException {
		createAndTestColorChanger(ColorChangerType.STATUS_BAR, false, new StatusBarColorChanger(shell));
		Mockito.when(coloredProjectFeature.isChangeStatusBarActive()).thenReturn(true);
		createAndTestColorChanger(ColorChangerType.STATUS_BAR, true, new StatusBarColorChanger(shell));
	}

	@Test
	public void testLeftRulerColorChanger() throws ColorChangerNotImplementedException {
		createAndTestColorChanger(ColorChangerType.LEFT_RULER, false, new LeftRulerColorChanger(editorPart));
		Mockito.when(coloredProjectFeature.isLeftRulerActive()).thenReturn(true);
		createAndTestColorChanger(ColorChangerType.LEFT_RULER, true, new LeftRulerColorChanger(editorPart));
	}

	@Test
	public void testRightRulerColorChanger() throws ColorChangerNotImplementedException {
		createAndTestColorChanger(ColorChangerType.RIGHT_RULER, false, new RightRulerColorChanger(editorPart));
		Mockito.when(coloredProjectFeature.isRightRulerActive()).thenReturn(true);
		createAndTestColorChanger(ColorChangerType.RIGHT_RULER, true, new RightRulerColorChanger(editorPart));
	}

	@Test
	public void testStatusBarWidgetColorChanger() throws ColorChangerNotImplementedException {
		createAndTestColorChanger(ColorChangerType.STATUS_BAR_WIDGET, false, new StatusBarWidgetColorChanger());
		Mockito.when(coloredProjectFeature.isStatusBarWidgetActive()).thenReturn(true);
		createAndTestColorChanger(ColorChangerType.STATUS_BAR_WIDGET, true, new StatusBarWidgetColorChanger());
	}

	@Test
	public void testTitleIconColorChanger() throws ColorChangerNotImplementedException {
		createAndTestColorChanger(ColorChangerType.TITLE_ICON, false, new TitleIconColorChanger(editorPart));
		Mockito.when(coloredProjectFeature.isTitleIconActive()).thenReturn(true);
		createAndTestColorChanger(ColorChangerType.TITLE_ICON, true, new TitleIconColorChanger(editorPart));
	}

	private void createAndTestColorChanger(ColorChangerType colorChangerType, boolean expectedActiveState,
			ColorChanger expectedColorChanger) throws ColorChangerNotImplementedException {
		ColorChanger colorChanger = cut.create(colorChangerType, editorPart, coloredProjectFeature);
		assertTrue(colorChanger.getClass().equals(expectedColorChanger.getClass()));
		assertEquals(expectedActiveState, colorChanger.isActive());

	}

}
