package abapci.coloredProject.model.projectColor;

import static org.junit.Assert.*;

import org.eclipse.swt.graphics.RGB;
import org.junit.Test;

public class ProjectColorFactoryTest {

	private static final RGB RED_COLOR = new RGB(255,0,0);

	@Test
	public void testCreateRGB() {
		ProjectColorFactory projectColorFactory = new ProjectColorFactory(); 
		RGB nullRGB = null; 
		assertTrue(projectColorFactory.create(nullRGB) instanceof DefaultEclipseProjectColor); 
		assertTrue(projectColorFactory.create(RED_COLOR) instanceof StandardProjectColor); 
	}

	@Test
	public void testCreateRGBBoolean() {
		ProjectColorFactory projectColorFactory = new ProjectColorFactory(); 
		IProjectColor suppressedColor = projectColorFactory.create(RED_COLOR, true); 
		assertTrue( suppressedColor instanceof StandardProjectColor); 
		assertTrue(suppressedColor.isSuppressed()); 
		assertTrue(projectColorFactory.create(RED_COLOR, false) instanceof StandardProjectColor); 
		
		RGB nullRGB = null; 
		assertTrue(projectColorFactory.create(nullRGB, false) instanceof DefaultEclipseProjectColor); 

	}

}
