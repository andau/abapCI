package abapci.coloredProject.colorChanger;

import abapci.Exception.ActiveEditorNotSetException;
import abapci.coloredProject.exeption.ProjectColorNotSetException;
import abapci.coloredProject.model.projectColor.IProjectColor;

public  abstract class ColorChanger {

	private boolean active;
	protected IProjectColor projectColor; 

	public abstract void change() throws ActiveEditorNotSetException, ProjectColorNotSetException;
	public void setActive(boolean active) {
		this.active = active; 
	}; 
	
	public boolean isActive() 
	{
		return active; 
	}; 
}
