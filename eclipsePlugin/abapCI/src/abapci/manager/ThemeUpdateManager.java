package abapci.manager;

import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

import abapci.domain.SourcecodeState;
import abapci.feature.FeatureFacade;

public class ThemeUpdateManager {

	private static final String THEME_PREFIX = "com.abapCi.custom.";
	private static final String ABAP_CI_STANDARD_THEME = THEME_PREFIX + "STANDARD_THEME";

	private static final String ABAP_CI_LIGHT_RED_CUSTOM_THEME = THEME_PREFIX + "LIGHT_RED_THEME";
	private static final String ABAP_CI_YELLOW_CUSTOM_THEME = THEME_PREFIX + "YELLOW_THEME";
	private static final String ABAP_CI_LIGHT_YELLOW_CUSTOM_THEME = THEME_PREFIX + "LIGHT_YELLOW_THEME";

	private FeatureFacade featureFacade;

	public ThemeUpdateManager() {
		featureFacade = new FeatureFacade();
	}

	public void updateTheme(SourcecodeState sourcecodeState) {

		if (featureFacade.getColorChangerFeature().isActive()) {

			String targetTheme;

			switch (sourcecodeState) {
			case UT_FAIL:
				targetTheme = ABAP_CI_LIGHT_RED_CUSTOM_THEME;
				break;
			case ATC_WARNING:
				targetTheme = ABAP_CI_LIGHT_YELLOW_CUSTOM_THEME;
				break;
			case ATC_FAIL:
				targetTheme = ABAP_CI_YELLOW_CUSTOM_THEME;
				break;
			case OK:
			default:
				targetTheme = ABAP_CI_STANDARD_THEME;
				break;
			}

			final String changeToTheme = targetTheme;
			Runnable task = () -> PlatformUI.getWorkbench().getThemeManager().setCurrentTheme(changeToTheme);
			Display.getDefault().asyncExec(task);
		}

	}

}
