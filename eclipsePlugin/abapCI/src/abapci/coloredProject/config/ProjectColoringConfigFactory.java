package abapci.coloredProject.config;

import abapci.coloredProject.model.ColoredProject;
import abapci.coloredProject.model.projectColor.DefaultEclipseProjectColor;
import abapci.coloredProject.model.projectColor.IProjectColor;
import abapci.coloredProject.model.projectColor.StandardProjectColor;
import abapci.feature.activeFeature.ColoredProjectFeature;

public class ProjectColoringConfigFactory implements IColoringConfigFactory {

	private final ColoredProjectFeature coloredProjectFeature;

	public ProjectColoringConfigFactory(ColoredProjectFeature coloredProjectFeature) {
		this.coloredProjectFeature = coloredProjectFeature;
	}

	@Override
	public IColoringConfig create(ColoredProject coloredProject) {

		IProjectColor statusBarProjectColor = coloredProjectFeature.isChangeStatusBarActive()
				? new StandardProjectColor(coloredProject.getColor())
				: new DefaultEclipseProjectColor();
		IProjectColor leftAnnotationBarProjectColor = coloredProjectFeature.isLeftRulerActive()
				? new StandardProjectColor(coloredProject.getColor())
				: new DefaultEclipseProjectColor();
		IProjectColor rightAnnotationBarProjectColor = coloredProjectFeature.isRightRulerActive()
				? new StandardProjectColor(coloredProject.getColor())
				: new DefaultEclipseProjectColor();
		IProjectColor titleProjectColor = coloredProjectFeature.isTitleIconActive()
				? new StandardProjectColor(coloredProject.getColor())
				: new DefaultEclipseProjectColor();
		IProjectColor statusWidgetProjectColor = coloredProjectFeature.isStatusBarWidgetActive()
				? new StandardProjectColor(coloredProject.getColor())
				: new DefaultEclipseProjectColor();
		return new ColoredProjectConfig(coloredProject.getName(), statusBarProjectColor, leftAnnotationBarProjectColor,
				rightAnnotationBarProjectColor, titleProjectColor, statusWidgetProjectColor);

	}

	public IColoringConfig createDefault(String projectName) {
		return new ColoredProjectConfig(projectName, new DefaultEclipseProjectColor(), new DefaultEclipseProjectColor(),
				new DefaultEclipseProjectColor(), new DefaultEclipseProjectColor(), new DefaultEclipseProjectColor());
	}

}