package abapci.coloredProject.config;

import abapci.coloredProject.model.ColoredProject;
import abapci.coloredProject.model.projectColor.DefaultEclipseProjectColor;
import abapci.coloredProject.model.projectColor.IProjectColor;
import abapci.coloredProject.model.projectColor.StandardProjectColor;
import abapci.feature.SourceCodeVisualisationFeature;

public class TestStateColoringConfigFactory implements IColoringConfigFactory {

	private final SourceCodeVisualisationFeature sourceCodeVisualisationFeature;

	public TestStateColoringConfigFactory(SourceCodeVisualisationFeature sourceCodeVisualisationFeature) {
		this.sourceCodeVisualisationFeature = sourceCodeVisualisationFeature;
	}

	@Override
	public IColoringConfig create(ColoredProject coloredProject) {
		IProjectColor statusBarProjectColor = sourceCodeVisualisationFeature.isChangeStatusBarBackgroundColorEnabled()
				? new StandardProjectColor(coloredProject.getColor())
				: new DefaultEclipseProjectColor();
		IProjectColor statusWidgetProjectColor = sourceCodeVisualisationFeature.isShowStatusBarWidgetEnabled()
				? new StandardProjectColor(coloredProject.getColor())
				: new DefaultEclipseProjectColor();
		return new ColoredProjectConfig(coloredProject.getName(), statusBarProjectColor,
				new DefaultEclipseProjectColor(), new DefaultEclipseProjectColor(), new DefaultEclipseProjectColor(),
				statusWidgetProjectColor);
	}
}
