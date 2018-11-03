package abapci.coloredProject.colorChanger;

import static org.junit.Assert.*;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPartSite;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import abapci.coloredProject.exeption.ColorChangerNotImplementedException;
import abapci.coloredProject.model.projectColor.IProjectColor;
import abapci.feature.ColoredProjectFeature;

public class ColorChangerFactoryTest {

	ColorChangerFactory cut;

	IEditorPart editorPart = Mockito.mock(IEditorPart.class);;	
	IWorkbenchPartSite site = Mockito.mock(IWorkbenchPartSite.class);;
	Shell shell= Mockito.mock(Shell.class);;
	ColoredProjectFeature coloredProjectFeature= Mockito.mock(ColoredProjectFeature.class);;
	IProjectColor projectColor = Mockito.mock(IProjectColor.class); 

	@Before
	public void before() throws ColorChangerNotImplementedException {
		cut = new ColorChangerFactory();

		Mockito.when(editorPart.getSite()).thenReturn(site); 
		Mockito.when(site.getShell()).thenReturn(shell); 

	}

	@Test
	public void testStatusBarColorChanger() throws ColorChangerNotImplementedException {
		createAndTestColorChanger(ColorChangerType.STATUS_BAR, false, new StatusBarColorChanger(shell, projectColor));
		Mockito.when(coloredProjectFeature.isChangeStatusBarActive()).thenReturn(true);
		createAndTestColorChanger(ColorChangerType.STATUS_BAR, true, new StatusBarColorChanger(shell, projectColor));
	}

	@Test
	public void testLeftRulerColorChanger() throws ColorChangerNotImplementedException {
		createAndTestColorChanger(ColorChangerType.LEFT_RULER, false, new LeftRulerColorChanger(editorPart, projectColor));
		Mockito.when(coloredProjectFeature.isLeftRulerActive()).thenReturn(true);
		createAndTestColorChanger(ColorChangerType.LEFT_RULER, true, new LeftRulerColorChanger(editorPart, projectColor));
	}

	@Test
	public void testRightRulerColorChanger() throws ColorChangerNotImplementedException {
		createAndTestColorChanger(ColorChangerType.RIGHT_RULER, false, new RightRulerColorChanger(editorPart, projectColor));
		Mockito.when(coloredProjectFeature.isRightRulerActive()).thenReturn(true);
		createAndTestColorChanger(ColorChangerType.RIGHT_RULER, true, new RightRulerColorChanger(editorPart, projectColor));
	}

	@Test
	public void testStatusBarWidgetColorChanger() throws ColorChangerNotImplementedException {
		createAndTestColorChanger(ColorChangerType.STATUS_BAR_WIDGET, false, new StatusBarWidgetColorChanger(projectColor));
		Mockito.when(coloredProjectFeature.isStatusBarWidgetActive()).thenReturn(true);
		createAndTestColorChanger(ColorChangerType.STATUS_BAR_WIDGET, true, new StatusBarWidgetColorChanger(projectColor));
	}

	@Test
	public void testTitleIconColorChanger() throws ColorChangerNotImplementedException {
		createAndTestColorChanger(ColorChangerType.TITLE_ICON, false, new TitleIconColorChanger(editorPart, projectColor, 0, 0));
		Mockito.when(coloredProjectFeature.isTitleIconActive()).thenReturn(true);
		createAndTestColorChanger(ColorChangerType.TITLE_ICON, true, new TitleIconColorChanger(editorPart, projectColor, 0, 0));
	}

	private void createAndTestColorChanger(ColorChangerType colorChangerType, boolean expectedActiveState,
			ColorChanger expectedColorChanger) throws ColorChangerNotImplementedException {
		ColorChanger colorChanger = cut.create(colorChangerType, editorPart, coloredProjectFeature, projectColor);
		assertTrue(colorChanger.getClass().equals(expectedColorChanger.getClass()));
		assertEquals(expectedActiveState, colorChanger.isActive());

	}

}
