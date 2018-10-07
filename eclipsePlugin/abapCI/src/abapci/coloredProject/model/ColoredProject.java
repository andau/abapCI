package abapci.coloredProject.model;

import abapci.domain.UiColor;

public class ColoredProject {

	private String name;
	private UiColor uiColor;

	public ColoredProject(String name, UiColor uiColor) {
		this.name = name;
		this.uiColor = uiColor;
	}

	public String getName() {
		return name;
	}

	public UiColor getUiColor() {
		return uiColor;
	}
}
