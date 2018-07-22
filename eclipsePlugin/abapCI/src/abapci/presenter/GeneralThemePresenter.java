package abapci.presenter;

import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

import abapci.Exception.AbapCiColoredProjectFileParseException;
import abapci.domain.UiColor;
import abapci.domain.UiTheme;
import abapci.model.ColoredProjectModel;
import abapci.utils.ColorToThemeMapper;

public class GeneralThemePresenter {

	private static final String THEME_PREFIX = "com.abapCi.custom.";

	private ColoredProjectModel model;

	public GeneralThemePresenter(ColoredProjectModel coloredProjectModel) {
		this.model = coloredProjectModel;
	}

	public void updateEditorLabel(UiColor uiColor) {
		UiTheme uiTheme = ColorToThemeMapper.mapUiColorToTheme(uiColor);

		final String changeToTheme = THEME_PREFIX + uiTheme.toString();

		Runnable task = () -> PlatformUI.getWorkbench().getThemeManager().setCurrentTheme(changeToTheme);
		Display.getDefault().asyncExec(task);

	}

	public UiColor getUiColor(String currentProjectname) throws AbapCiColoredProjectFileParseException {
		return model.getColorForProject(currentProjectname);
	}

}
