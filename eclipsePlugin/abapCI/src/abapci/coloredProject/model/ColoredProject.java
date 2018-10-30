package abapci.coloredProject.model;

import org.eclipse.swt.graphics.Color;

import abapci.coloredProject.model.projectColor.IProjectColor;

public class ColoredProject {

	private String name;
	private IProjectColor projectColor;
	private boolean suppressColoring;

	public ColoredProject(String name, IProjectColor projectColor) {
		this(name, projectColor, false);
	}

	public ColoredProject(String name, IProjectColor projectColor, boolean suppressColoring) {
		this.name = name;
		this.suppressColoring = suppressColoring;
		this.projectColor = projectColor;
	}

	public String getName() {
		return name;
	}

	public Color getColor() {
		return projectColor.getColor();
	}

	public boolean isSuppressedColoring() {
		return suppressColoring;
	}

	public boolean getSuppressed() {
		// TODO Auto-generated method stub
		return false;
	}
}
