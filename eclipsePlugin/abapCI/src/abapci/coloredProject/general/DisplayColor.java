package abapci.coloredProject.general;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.graphics.Color;

import abapci.coloredProject.model.projectColor.IProjectColor;
import abapci.coloredProject.model.projectColor.IProjectColorFactory;
import abapci.coloredProject.model.projectColor.ProjectColorFactory;
import abapci.feature.activeFeature.ColoredProjectFeature;

public class DisplayColor {

	private final IProject project;
	private final IProjectColor statusBarColor;
	private final IProjectColor leftAnnotationBarColor;
	private final IProjectColor rightAnnotationBarColor;
	private final IProjectColor titleIconColor;
	private final IProjectColor statusWidgetBackgroundColor;
	private boolean suppressed;
	private String testStateOutput;

	public DisplayColor(IProject project, Color statusBarColor, Color leftAnnotationBarColor,
			Color rightAnnotationBarColor, Color titleIconColor, Color statusWidgetBackgroundColor,
			String testStateOutput) {

		final IProjectColorFactory projectColorFactory = new ProjectColorFactory();

		this.project = project;
		this.statusBarColor = projectColorFactory.create(statusBarColor);
		this.leftAnnotationBarColor = projectColorFactory.create(leftAnnotationBarColor);
		this.rightAnnotationBarColor = projectColorFactory.create(rightAnnotationBarColor);
		this.titleIconColor = projectColorFactory.create(titleIconColor);
		this.statusWidgetBackgroundColor = projectColorFactory.create(statusWidgetBackgroundColor);
		this.testStateOutput = testStateOutput;
	}

	public DisplayColor(IProject project, boolean suppressed) {

		final IProjectColorFactory projectColorFactory = new ProjectColorFactory();

		this.project = project;
		statusBarColor = projectColorFactory.createStandardColor();
		leftAnnotationBarColor = projectColorFactory.createStandardColor();
		rightAnnotationBarColor = projectColorFactory.createStandardColor();
		titleIconColor = projectColorFactory.createStandardColor();
		statusWidgetBackgroundColor = projectColorFactory.createStandardColor();
	}

	public DisplayColor(IProject project, Color color, ColoredProjectFeature coloredProjectFeature) {
		final IProjectColorFactory projectColorFactory = new ProjectColorFactory();

		this.project = project;

		statusBarColor = coloredProjectFeature.isChangeStatusBarActive() ? projectColorFactory.create(color)
				: projectColorFactory.createStandardColor();
		leftAnnotationBarColor = coloredProjectFeature.isLeftRulerActive() ? projectColorFactory.create(color)
				: projectColorFactory.createStandardColor();
		rightAnnotationBarColor = coloredProjectFeature.isRightRulerActive() ? projectColorFactory.create(color)
				: projectColorFactory.createStandardColor();
		titleIconColor = coloredProjectFeature.isTitleIconActive() ? projectColorFactory.create(color)
				: projectColorFactory.createStandardColor();
		statusWidgetBackgroundColor = coloredProjectFeature.isStatusBarWidgetActive()
				? projectColorFactory.create(color)
				: projectColorFactory.createStandardColor();

	}

	public IProjectColor getStatusBarColor() {
		return statusBarColor;
	}

	public IProjectColor getLeftAnnotationBarColor() {
		return leftAnnotationBarColor;
	}

	public IProjectColor getRightAnnotationBarColor() {
		return rightAnnotationBarColor;
	}

	public IProjectColor getTitleIconColor() {
		return titleIconColor;
	}

	public IProjectColor getStatusWidgetBackgroundColor() {
		return statusWidgetBackgroundColor;
	}

	public boolean isSuppressed() {
		return suppressed;
	}

	public void setTestStateOutput(String testStateOutput) {
		this.testStateOutput = testStateOutput;
	}

	public String getTestStateOutput() {
		return testStateOutput == null ? "Project: " + project.getName() : testStateOutput;
	}

	public IProject getProject() {
		return project;
	}

}
