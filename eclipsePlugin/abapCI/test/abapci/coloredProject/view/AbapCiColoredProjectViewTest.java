package abapci.coloredProject.view;

import org.eclipse.swt.widgets.Composite;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;

import abapci.AbapCiPluginHelper;
import abapci.coloredProject.model.ColoredProjectModel;
import abapci.coloredProject.presenter.ColoredProjectsPresenter;

public class AbapCiColoredProjectViewTest {

	AbapCiPluginHelper abapCiPluginHelper; 
	ColoredProjectModel coloredProjectModel; 
	
	@Test
	public void test() {
		AbapCiColoredProjectView cut = new AbapCiColoredProjectView(); 

		Mockito.mock(Composite.class); 
		abapCiPluginHelper = Mockito.mock(AbapCiPluginHelper.class); 
		coloredProjectModel = Mockito.mock(ColoredProjectModel.class); 
	
		Whitebox.setInternalState(cut, "abapCiPluginHelper", abapCiPluginHelper);

		ColoredProjectsPresenter coloredProjectsPresenter = new ColoredProjectsPresenter(null, coloredProjectModel);
		Mockito.when(abapCiPluginHelper.getColoredProjectsPresenter()).thenReturn(coloredProjectsPresenter ); 
		
		//TODO further tests if not too much work 
		
	}

}
