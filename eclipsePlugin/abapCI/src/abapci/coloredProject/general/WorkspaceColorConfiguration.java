package abapci.coloredProject.general;

import java.util.HashMap;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.graphics.Color;

import abapci.Exception.AbapCiColoredProjectFileParseException;
import abapci.coloredProject.config.ColoredProjectConfig;
import abapci.coloredProject.config.IColoringConfig;
import abapci.coloredProject.config.IColoringConfigFactory;
import abapci.coloredProject.config.ProjectColoringConfigFactory;
import abapci.coloredProject.model.ColoredProject;
import abapci.coloredProject.model.ColoredProjectModel;
import abapci.coloredProject.model.projectColor.DefaultEclipseProjectColor;
import abapci.feature.FeatureFacade;

public class WorkspaceColorConfiguration {

	private final HashMap<String, ColoredProjectConfigs> coloredProjectConfigProjects;
	private FeatureFacade featureFacade;
	private IColoringConfigFactory coloringConfigFactory;

	public WorkspaceColorConfiguration(boolean initialize) throws AbapCiColoredProjectFileParseException {
		coloredProjectConfigProjects = new HashMap<>();

		if (initialize) {
			initialize();
		}
	}

	private void initialize() throws AbapCiColoredProjectFileParseException {
		featureFacade = new FeatureFacade();

		coloringConfigFactory = new ProjectColoringConfigFactory(featureFacade.getColoredProjectFeature());

		ColoredProjectModel model = new ColoredProjectModel();
		List<ColoredProject> coloredProjects = model.getColoredProjects();
		for (ColoredProject coloredProject : coloredProjects) {
			IColoringConfig coloredProjectConfig = coloringConfigFactory.create(coloredProject);
			addOrUpdateProjectColoringConfig(coloredProjectConfig);
		}

	}

	public void addOrUpdateTestStateColoring(IColoringConfig testStateConfig) {

		if (coloredProjectConfigProjects.containsKey(testStateConfig.getProjectName())) {
			ColoredProjectConfigs testStateColor = coloredProjectConfigProjects.get(testStateConfig.getProjectName());
			testStateColor.setTestStateColoring(testStateConfig);
		} else {
			ColoredProjectConfig projectConfig = new ColoredProjectConfig(testStateConfig.getProjectName(), null,
					false);
			coloredProjectConfigProjects.put(testStateConfig.getProjectName(),
					new ColoredProjectConfigs(projectConfig, testStateConfig));
		}
	}

	public void addOrUpdateProjectColoringConfig(IColoringConfig projectConfig) {

		if (coloredProjectConfigProjects.containsKey(projectConfig.getProjectName())) {
			ColoredProjectConfigs displayColors = coloredProjectConfigProjects.get(projectConfig.getProjectName());
			displayColors.setProjectColoring(projectConfig);
		} else {
			ColoredProjectConfig testStateConfig = new ColoredProjectConfig(projectConfig.getProjectName(), null,
					false);
			coloredProjectConfigProjects.put(projectConfig.getProjectName(),
					new ColoredProjectConfigs(projectConfig, testStateConfig));
		}
	}

	public int getNumColoredProjects() {
		return coloredProjectConfigProjects.size();
	}

	public DisplayColor getColoring(IProject project) {
		ColoredProjectConfigs coloredProjectConfigs;

		if (coloredProjectConfigProjects.containsKey(project.getName())) {
			coloredProjectConfigs = coloredProjectConfigProjects.get(project.getName());
		} else {
			coloredProjectConfigs = new ColoredProjectConfigs(project.getName());
		}

		Color statusBarProjectColor = takeFirstConfiguredColor(
				coloredProjectConfigs.getTestStateColoredProjectConfig().getStatusBarColor(),
				coloredProjectConfigs.getProjectColoredProjectConfig().getStatusBarColor());

		Color leftAnnotationBarProjectColor = takeFirstConfiguredColor(
				coloredProjectConfigs.getTestStateColoredProjectConfig().getLeftAnnotationBarColor(),
				coloredProjectConfigs.getProjectColoredProjectConfig().getLeftAnnotationBarColor());

		Color rightAnnotationBarProjectColor = takeFirstConfiguredColor(
				coloredProjectConfigs.getTestStateColoredProjectConfig().getRightAnnotationBarColor(),
				coloredProjectConfigs.getProjectColoredProjectConfig().getRightAnnotationBarColor());

		Color titleIconProjectColor = takeFirstConfiguredColor(
				coloredProjectConfigs.getTestStateColoredProjectConfig().getTitleColor(),
				coloredProjectConfigs.getProjectColoredProjectConfig().getTitleColor());

		Color statusWidgetProjectBackgroundColor = takeFirstConfiguredColor(
				coloredProjectConfigs.getTestStateColoredProjectConfig().getStatusWidgetColor(),
				coloredProjectConfigs.getProjectColoredProjectConfig().getStatusWidgetColor());

		statusWidgetProjectBackgroundColor = replaceStatusWidgetNullColorWithStatusColor(statusBarProjectColor,
				statusWidgetProjectBackgroundColor);

		DisplayColor colorForEntireProject = new DisplayColor(statusBarProjectColor, leftAnnotationBarProjectColor,
				rightAnnotationBarProjectColor, titleIconProjectColor, statusWidgetProjectBackgroundColor);

		return colorForEntireProject;
	}

	private Color takeFirstConfiguredColor(Color firstColor, Color secondColor) {
		return firstColor == null ? secondColor : firstColor;
	}

	private Color replaceStatusWidgetNullColorWithStatusColor(Color statusBarProjectColor,
			Color statusWidgetProjectBackgroundColor) {

		if (statusWidgetProjectBackgroundColor == null && statusBarProjectColor != null) {
			statusWidgetProjectBackgroundColor = statusBarProjectColor;
		}
		return statusWidgetProjectBackgroundColor;

	}

	public boolean isConfigured(IProject project) {
		return coloredProjectConfigProjects.containsKey(project.getName());
	}

	private class ColoredProjectConfigs {
		private IColoringConfig projectColoringConfig;
		private IColoringConfig testStateColoringConfig;

		public ColoredProjectConfigs(IColoringConfig projectColoringConfig, IColoringConfig testStateColoringConfig) {
			this.projectColoringConfig = projectColoringConfig;
			this.testStateColoringConfig = testStateColoringConfig;
		}

		public ColoredProjectConfigs(String projectname) {
			projectColoringConfig = new ColoredProjectConfig(projectname, new DefaultEclipseProjectColor(),
					new DefaultEclipseProjectColor(), new DefaultEclipseProjectColor(),
					new DefaultEclipseProjectColor(), new DefaultEclipseProjectColor());
			testStateColoringConfig = new ColoredProjectConfig(projectname, new DefaultEclipseProjectColor(),
					new DefaultEclipseProjectColor(), new DefaultEclipseProjectColor(),
					new DefaultEclipseProjectColor(), new DefaultEclipseProjectColor());
		}

		public IColoringConfig getProjectColoredProjectConfig() {
			return projectColoringConfig;
		}

		public IColoringConfig getTestStateColoredProjectConfig() {
			return testStateColoringConfig;
		}

		public void setProjectColoring(IColoringConfig projectColorConfig) {
			this.projectColoringConfig = projectColorConfig;
		}

		public void setTestStateColoring(IColoringConfig projectColorConfig) {
			this.testStateColoringConfig = projectColorConfig;
		}

	}

}