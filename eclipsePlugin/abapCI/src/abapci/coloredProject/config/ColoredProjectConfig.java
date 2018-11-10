package abapci.coloredProject.config;

import org.eclipse.swt.graphics.Color;

import abapci.coloredProject.model.projectColor.IProjectColor;

public class ColoredProjectConfig implements IColoringConfig {

	private final String projectName;
	private final Color statusBarColor;
	private final Color leftAnnotationBarColor;
	private final Color rightAnnotationBarColor;
	private final Color titleColor;
	private final Color statusWidgetColor;

	public ColoredProjectConfig(String projectName, IProjectColor statusBarProjectColor,
			IProjectColor leftAnnotationBarProjectColor, IProjectColor rightAnnotationBarProjectColor,
			IProjectColor titleProjectColor, IProjectColor statusWidgetProjectColor) {

		this.projectName = projectName;
		this.statusBarColor = statusBarProjectColor.getColor();
		this.leftAnnotationBarColor = leftAnnotationBarProjectColor.getColor();
		this.rightAnnotationBarColor = rightAnnotationBarProjectColor.getColor();
		this.titleColor = titleProjectColor.getColor();
		this.statusWidgetColor = statusWidgetProjectColor.getColor();
	}

	public ColoredProjectConfig(String projectName, Color color, boolean suppressed) {
		this.projectName = projectName;
		this.statusBarColor = color;
		this.leftAnnotationBarColor = color;
		this.rightAnnotationBarColor = color;
		this.titleColor = color;
		this.statusWidgetColor = color;
	}

	@Override
	public String getProjectName() {
		return projectName;
	}

	@Override
	public Color getStatusBarColor() {
		return statusBarColor;
	}

	@Override
	public Color getLeftAnnotationBarColor() {
		return leftAnnotationBarColor;
	}

	@Override
	public Color getRightAnnotationBarColor() {
		return rightAnnotationBarColor;
	}

	@Override
	public Color getTitleColor() {
		return titleColor;
	}

	@Override
	public Color getStatusWidgetColor() {
		return statusWidgetColor;
	}

}
