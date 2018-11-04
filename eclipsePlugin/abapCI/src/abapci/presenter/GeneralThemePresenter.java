package abapci.presenter;

import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

import abapci.coloredProject.model.ColoredProjectModel;
import abapci.domain.UiColor;
import abapci.domain.UiTheme;
import abapci.utils.ColorToThemeMapper;

public class GeneralThemePresenter {

	private static final String THEME_PREFIX = "com.abapCi.custom.";

	public GeneralThemePresenter(ColoredProjectModel coloredProjectModel) {
	}

	public void updateEditorLabel(UiColor uiColor) {
		UiTheme uiTheme = ColorToThemeMapper.mapUiColorToTheme(uiColor);

		final String changeToTheme = THEME_PREFIX + uiTheme.toString();

		Runnable task = () -> PlatformUI.getWorkbench().getThemeManager().setCurrentTheme(changeToTheme);
		Display.getDefault().asyncExec(task);

	}
}
