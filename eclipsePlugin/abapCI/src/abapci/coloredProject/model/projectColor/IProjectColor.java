package abapci.coloredProject.model.projectColor;

import org.eclipse.swt.graphics.Color;

public interface IProjectColor {

	Color getColor();
	
	boolean isSuppressed();

	void setSuppressed(); 
}
