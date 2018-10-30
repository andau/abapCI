package abapci.coloredProject.model.projectColor;

import org.eclipse.swt.graphics.Color;

public class DefaultEclipseProjectColor implements IProjectColor {

	private boolean suppressed;

	public DefaultEclipseProjectColor() 
	{
		
	}
	
	public Color getColor() {
		return null; 
	}

	@Override
	public boolean isSuppressed() {
		// TODO Auto-generated method stub
		return suppressed;
	}

	@Override
	public void setSuppressed() {
		suppressed = true;  
	}
}
