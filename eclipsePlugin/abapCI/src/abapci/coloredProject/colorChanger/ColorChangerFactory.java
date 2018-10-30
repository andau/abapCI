package abapci.coloredProject.colorChanger;

import org.eclipse.ui.IEditorPart;

import abapci.coloredProject.exeption.ColorChangerNotImplementedException;
import abapci.feature.ColoredProjectFeature;

public class ColorChangerFactory {
	public ColorChanger create(ColorChangerType colorChangerType, IEditorPart editorPart,
			ColoredProjectFeature coloredProjectFeature) throws ColorChangerNotImplementedException {
		ColorChanger colorChanger;

		switch (colorChangerType) {
		case STATUS_BAR:
			colorChanger = new StatusBarColorChanger(editorPart.getSite().getShell());
			colorChanger.setActive(coloredProjectFeature.isChangeStatusBarActive());
			break;
		case LEFT_RULER: 
			colorChanger = new LeftRulerColorChanger(editorPart);
			colorChanger.setActive(coloredProjectFeature.isLeftRulerActive());
			break;
		case RIGHT_RULER: 
			colorChanger = new RightRulerColorChanger(editorPart);
			colorChanger.setActive(coloredProjectFeature.isRightRulerActive());
			break;
		case TITLE_ICON: 
			colorChanger = new TitleIconColorChanger(editorPart);
			colorChanger.setActive(coloredProjectFeature.isTitleIconActive());
			break;
		case STATUS_BAR_WIDGET: 
			colorChanger = new StatusBarWidgetColorChanger();
			colorChanger.setActive(coloredProjectFeature.isStatusBarWidgetActive());
			break;
			
		default:
			throw new ColorChangerNotImplementedException();
		}

		return colorChanger;
	}
}
