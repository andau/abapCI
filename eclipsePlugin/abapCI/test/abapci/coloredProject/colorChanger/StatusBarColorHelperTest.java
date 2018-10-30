package abapci.coloredProject.colorChanger;

import static org.junit.Assert.*;

import org.eclipse.swt.graphics.RGB;
import org.junit.Test;

import abapci.coloredProject.model.projectColor.IProjectColor;
import abapci.coloredProject.model.projectColor.IProjectColorFactory;
import abapci.coloredProject.model.projectColor.ProjectColorFactory;

public class StatusBarColorHelperTest {

	static RGB TEST_RGB = new RGB(100,100,100); 
	
	@Test
	public void testStatusBarColorHelper() {
		assertNotNull(StatusBarColorHelper.getColor(null));
		
		IProjectColorFactory projectColorFactory = new ProjectColorFactory(); 
		IProjectColor projectColor = projectColorFactory.create(TEST_RGB); 
		assertEquals(StatusBarColorHelper.getColor(projectColor).getRGB(),  TEST_RGB); 
	}

}
