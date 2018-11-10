package abapci.coloredProject.samples;

import org.eclipse.swt.graphics.RGB;

import abapci.coloredProject.model.ColoredProject;
import abapci.coloredProject.model.projectColor.IProjectColor;
import abapci.coloredProject.model.projectColor.IProjectColorFactory;
import abapci.coloredProject.model.projectColor.ProjectColorFactory;

public class ColoredProjectTestSample {

	private static final String SAMPLE_PROJECT = "SAMPLE_PROJECT";

	private static RGB COLOR_GREEN = new RGB(0, 255, 0);
	private static RGB COLOR_BLUE = new RGB(0, 0, 255);

	public static IProjectColor getGreenProjectColor() {
		IProjectColorFactory projectColorFactory = new ProjectColorFactory();
		return projectColorFactory.create(COLOR_GREEN);
	}

	private static IProjectColor getBlueProjectColor() {
		IProjectColorFactory projectColorFactory = new ProjectColorFactory();
		return projectColorFactory.create(COLOR_BLUE);
	}

	public static ColoredProject getGreenColoredProject() {
		return new ColoredProject(SAMPLE_PROJECT, getGreenProjectColor());
	}

	public static ColoredProject getBlueColoredProject() {
		return new ColoredProject(SAMPLE_PROJECT, getBlueProjectColor());
	}

	public static ColoredProject getSuppressedColoredProject() {
		return new ColoredProject(SAMPLE_PROJECT, getGreenProjectColor(), true);
	}

}
