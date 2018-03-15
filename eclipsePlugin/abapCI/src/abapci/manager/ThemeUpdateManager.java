package abapci.manager;

import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

import abapci.domain.SourcecodeState;
import abapci.feature.FeatureFacade;

public class ThemeUpdateManager {

	private static final String STANDARD_THEME = "org.eclipse.ui.r30";
	private static final String ABAP_CI_RED_CUSTOM_THEME = "com.abapCi.custom.redTheme";
	private static final String ABAP_CI_LIGHT_RED_CUSTOM_THEME = "com.abapCi.custom.lightRedTheme";

	private static final String ABAP_CI_YELLOW_CUSTOM_THEME = "com.abapCi.custom.yellowTheme";
	private static final String ABAP_CI_LIGHT_YELLOW_CUSTOM_THEME = "com.abapCi.custom.lightYellowTheme";
	
	private FeatureFacade featureFacade;

	public ThemeUpdateManager() {
		featureFacade = new FeatureFacade();
	}

	public void updateTheme(SourcecodeState sourcecodeState) {

		String targetTheme;

		switch (sourcecodeState) {
		case UT_FAIL:
			targetTheme = featureFacade.getColorChangerFeature().isActive() ? ABAP_CI_LIGHT_RED_CUSTOM_THEME : STANDARD_THEME;
			break;			
		case ATC_WARNING: 
			targetTheme = featureFacade.getColorChangerFeature().isActive() ? ABAP_CI_LIGHT_YELLOW_CUSTOM_THEME : STANDARD_THEME;
			break;									
		case ATC_FAIL: 
			targetTheme = featureFacade.getColorChangerFeature().isActive() ? ABAP_CI_YELLOW_CUSTOM_THEME : STANDARD_THEME;
			break;						
		case OK:
		default:
			targetTheme = STANDARD_THEME;
			break;
		}

		final String updateTargetTheme = targetTheme;

		if (featureFacade.getColorChangerFeature().isActive()) {
			Runnable task = () -> PlatformUI.getWorkbench().getThemeManager().setCurrentTheme(updateTargetTheme);
			Display.getDefault().asyncExec(task);
		}

	}

}
