package abapci.coloredProject.model.projectColor;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

public class ProjectColorFactory implements IProjectColorFactory {

	public IProjectColor create(RGB rgb) {
		return create(rgb,false); 
	}

	public IProjectColor create(RGB rgb, boolean suppressed) {
		Color color =  (rgb != null) ? new StandardProjectColor(rgb).getColor() : null; 
		return create(color, suppressed); 
	}

	@Override
	public IProjectColor create(Color generalColor) {
		return create(generalColor, false); 
	}

	@Override
	public IProjectColor create(Color generalColor, boolean suppressed) {
		IProjectColor projectColor =  (generalColor == null || generalColor.getRGB() == null) ? (IProjectColor) new DefaultEclipseProjectColor()
				: (IProjectColor) new StandardProjectColor(generalColor);
		
		if (suppressed) projectColor.setSuppressed(); 

		return projectColor; 
	}


}
