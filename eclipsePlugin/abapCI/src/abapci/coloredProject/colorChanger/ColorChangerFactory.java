package abapci.coloredProject.colorChanger;

import org.eclipse.ui.IEditorPart;

import abapci.coloredProject.exeption.ColorChangerNotImplementedException;
import abapci.coloredProject.model.projectColor.IProjectColor;
import abapci.feature.activeFeature.ColoredProjectFeature;
import abapci.utils.StringUtils;

public class ColorChangerFactory {
	public ColorChanger create(ColorChangerType colorChangerType, IEditorPart editorPart,
			ColoredProjectFeature coloredProjectFeature, IProjectColor projectColor)
			throws ColorChangerNotImplementedException {
		ColorChanger colorChanger;

		switch (colorChangerType) {
		case STATUS_BAR:
			colorChanger = new StatusBarColorChanger(editorPart.getSite().getShell(), projectColor);
			colorChanger.setActive(coloredProjectFeature.isChangeStatusBarActive());
			break;
		case LEFT_RULER:
			colorChanger = new LeftRulerColorChanger(editorPart, projectColor);
			colorChanger.setActive(coloredProjectFeature.isLeftRulerActive());
			break;
		case RIGHT_RULER:
			colorChanger = new RightRulerColorChanger(editorPart, projectColor);
			colorChanger.setActive(coloredProjectFeature.isRightRulerActive());
			break;
		case TITLE_ICON:
			TitleIconOverlayRectangle rectangle = new TitleIconOverlayRectangle(
					coloredProjectFeature.getTitleIconWidth(), coloredProjectFeature.getTitleIconHeight());
			colorChanger = new TitleIconColorChanger(editorPart, projectColor, rectangle);
			colorChanger.setActive(coloredProjectFeature.isTitleIconActive());
			break;
		case STATUS_BAR_WIDGET:
			colorChanger = new StatusBarWidgetColorChanger(projectColor, StringUtils.EMPTY);
			colorChanger.setActive(coloredProjectFeature.isStatusBarWidgetActive());
			break;

		default:
			throw new ColorChangerNotImplementedException();
		}

		return colorChanger;
	}
}
