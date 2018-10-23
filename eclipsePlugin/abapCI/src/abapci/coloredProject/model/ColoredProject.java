package abapci.coloredProject.model;

import org.eclipse.swt.graphics.Color;

public class ColoredProject {

	private String name;
	private Color color;
	private boolean suppressColoring;

	public ColoredProject(String name, Color color) {
		this(name, color, false);
	}

	public ColoredProject(String name, Color color, boolean suppressColoring) {
		this.name = name;
		this.suppressColoring = suppressColoring;
		this.color = color;
	}

	public String getName() {
		return name;
	}

	public Color getColor() {
		return color;
	}

	public boolean suppressColoring() {
		return suppressColoring;
	}
}
