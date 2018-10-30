package abapci.coloredProject.samples;

import org.eclipse.swt.graphics.RGB;

import abapci.coloredProject.model.ColoredProject;
import abapci.coloredProject.model.projectColor.IProjectColor;
import abapci.coloredProject.model.projectColor.IProjectColorFactory;
import abapci.coloredProject.model.projectColor.ProjectColorFactory;

public class ColoredProjectTestSample {

	private static final String RED_SAMPLE_PROJECT = "RED_SAMPLE_PROJECT";
	private static RGB COLOR_RED = new RGB(255,0,0);

	public static IProjectColor getRedProjectColor() {
		IProjectColorFactory projectColorFactory = new ProjectColorFactory();
		return projectColorFactory.create(COLOR_RED );
	}

	public static ColoredProject getRedColoredProject() {
        return new ColoredProject(RED_SAMPLE_PROJECT, getRedProjectColor());  
	}

	public static ColoredProject getSuppressedColoredProject() {
		return new ColoredProject(RED_SAMPLE_PROJECT, getRedProjectColor(), true);
	}

}
