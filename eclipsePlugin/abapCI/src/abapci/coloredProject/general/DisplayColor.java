package abapci.coloredProject.general;

import org.eclipse.swt.graphics.Color;

import abapci.coloredProject.model.projectColor.IProjectColor;
import abapci.coloredProject.model.projectColor.IProjectColorFactory;
import abapci.coloredProject.model.projectColor.ProjectColorFactory;
import abapci.feature.activeFeature.ColoredProjectFeature;

public class DisplayColor {

	private final IProjectColor statusBarColor;
	private final IProjectColor leftAnnotationBarColor;
	private final IProjectColor rightAnnotationBarColor;
	private final IProjectColor titleIconColor;
	private final IProjectColor statusWidgetBackgroundColor;
	private boolean suppressed;

	public DisplayColor(Color statusBarColor, Color leftAnnotationBarColor, Color rightAnnotationBarColor,
			Color titleIconColor, Color statusWidgetBackgroundColor) {

		IProjectColorFactory projectColorFactory = new ProjectColorFactory();

		this.statusBarColor = projectColorFactory.create(statusBarColor);
		this.leftAnnotationBarColor = projectColorFactory.create(leftAnnotationBarColor);
		this.rightAnnotationBarColor = projectColorFactory.create(rightAnnotationBarColor);
		this.titleIconColor = projectColorFactory.create(titleIconColor);
		this.statusWidgetBackgroundColor = projectColorFactory.create(statusWidgetBackgroundColor);
	}

	public DisplayColor(boolean suppressed) {

		IProjectColorFactory projectColorFactory = new ProjectColorFactory();

		this.statusBarColor = projectColorFactory.createStandardColor();
		this.leftAnnotationBarColor = projectColorFactory.createStandardColor();
		this.rightAnnotationBarColor = projectColorFactory.createStandardColor();
		this.titleIconColor = projectColorFactory.createStandardColor();
		this.statusWidgetBackgroundColor = projectColorFactory.createStandardColor();
	}

	public DisplayColor(Color color, ColoredProjectFeature coloredProjectFeature) {
		IProjectColorFactory projectColorFactory = new ProjectColorFactory();

		this.statusBarColor = coloredProjectFeature.isChangeStatusBarActive() ? projectColorFactory.create(color)
				: projectColorFactory.createStandardColor();
		this.leftAnnotationBarColor = coloredProjectFeature.isLeftRulerActive() ? projectColorFactory.create(color)
				: projectColorFactory.createStandardColor();
		this.rightAnnotationBarColor = coloredProjectFeature.isRightRulerActive() ? projectColorFactory.create(color)
				: projectColorFactory.createStandardColor();
		this.titleIconColor = coloredProjectFeature.isTitleIconActive() ? projectColorFactory.create(color)
				: projectColorFactory.createStandardColor();
		this.statusWidgetBackgroundColor = coloredProjectFeature.isStatusBarWidgetActive()
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

}
