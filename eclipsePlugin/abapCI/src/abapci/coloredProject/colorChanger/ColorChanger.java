package abapci.coloredProject.colorChanger;

import abapci.Exception.ActiveEditorNotSetException;
import abapci.coloredProject.model.projectColor.IProjectColor;

public  abstract class ColorChanger {

	private boolean active;

	public abstract void change(IProjectColor projectColor) throws ActiveEditorNotSetException;
	public void setActive(boolean active) {
		this.active = active; 
	}; 
	
	public boolean isActive() 
	{
		return active; 
	}; 
}
