package abapci.coloredProject.general;

import org.eclipse.swt.graphics.Color;

import abapci.coloredProject.model.projectColor.IProjectColor;
import abapci.coloredProject.model.projectColor.IProjectColorFactory;
import abapci.coloredProject.model.projectColor.ProjectColorFactory;

public class DisplayColor {

	private IProjectColor statusBarColor;
	private IProjectColor annotationBarColor;
	private IProjectColor titleIconColor;
	private IProjectColor statusWidgetBackgroundColor;
	private boolean suppressed; 

	public DisplayColor(Color statusBarColor, Color annotationBarColor, Color titleIconColor, Color statusWidgetBackgroundColor) {
		
		IProjectColorFactory projectColorFactory = new ProjectColorFactory(); 

		this.statusBarColor = projectColorFactory.create(statusBarColor); 
		this.annotationBarColor = projectColorFactory.create(annotationBarColor);
		this.titleIconColor = projectColorFactory.create(titleIconColor);
		this.statusWidgetBackgroundColor = projectColorFactory.create(statusWidgetBackgroundColor); 
	}

	public DisplayColor(Color generalColor, boolean suppressed) {

		IProjectColorFactory projectColorFactory = new ProjectColorFactory(); 
		
		this.statusBarColor = projectColorFactory.create(generalColor, suppressed);
		this.annotationBarColor = projectColorFactory.create(generalColor, suppressed);
		this.titleIconColor = projectColorFactory.create(generalColor, suppressed);
		this.statusWidgetBackgroundColor = projectColorFactory.create(generalColor, suppressed); 
	}

	public IProjectColor getStatusBarColor() {
		return statusBarColor; 
	}
	
	public IProjectColor getAnnotationBarColor() {
		return annotationBarColor;
	}

	public IProjectColor getTitleIconColor() {
		return titleIconColor;
	}

	public IProjectColor getStatusWidgetBackgroundColor() {
		return statusWidgetBackgroundColor;
	}
	
	public boolean isSuppressed() 
	{
		return suppressed; 
	}

}
