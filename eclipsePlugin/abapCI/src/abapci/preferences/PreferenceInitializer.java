package abapci.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.swt.graphics.RGB;

import abapci.AbapCiPluginHelper;

public class PreferenceInitializer extends AbstractPreferenceInitializer {

	private final AbapCiPluginHelper abapCiPluginHelper;

	public PreferenceInitializer() {
		abapCiPluginHelper = new AbapCiPluginHelper();
	}

	@Override
	public void initializeDefaultPreferences() {

		IPreferenceStore store = abapCiPluginHelper.getPreferenceStore();
		store.setDefault(PreferenceConstants.PREF_UNIT_RUN_ON_SAVE, true);
		store.setDefault(PreferenceConstants.PREF_UNIT_RUN_ACTIVATED_OBJECTS_ONLY, false);

		store.setDefault(PreferenceConstants.PREF_ATC_RUN_ACTIVATED_OBJECTS_ONLY, true);
		store.setDefault(PreferenceConstants.PREF_ATC_VARIANT, "DEFAULT");
		store.setDefault(PreferenceConstants.PREF_TDD_MODE, false);
		store.setDefault(PreferenceConstants.PREF_TDD_MIN_REQUIRED_SECONDS, 30);

		store.setDefault(PreferenceConstants.PREF_JENKINS_URL, "<jenkins_host>:<jenkins_port>");
		store.setDefault(PreferenceConstants.PREF_JENKINS_USERNAME, "jenkins_username");
		store.setDefault(PreferenceConstants.PREF_JENKINS_PASSWORD, "jenkins_password");
		store.setDefault(PreferenceConstants.PREF_JENKINS_BUILD_TOKEN, "PUT_YOUR_OWN_TOKEN_FOR_ABAP_CI");
		store.setDefault(PreferenceConstants.PREF_JENKINS_RUN_AFTER_UNIT_TESTS_TURN_GREEN, false);
		store.setDefault(PreferenceConstants.PREF_VISUALISATION_STATUS_CHANGE_THEME_ENABLED, false);
		store.setDefault(PreferenceConstants.PREF_CHANGE_STATUS_BAR_BACKGROUND_COLOR, false);
		store.setDefault(PreferenceConstants.PREF_VISUALISATION_STATUS_BAR_WIDGET_ENABLED, true);

		store.setDefault(PreferenceConstants.PREF_SOURCE_CODE_FORMATTING_ENABLED, true);
		store.setDefault(PreferenceConstants.PREF_SOURCE_CODE_FORMATTING_PREFIX, "#autoformat");
		store.setDefault(PreferenceConstants.PREF_SOURCE_CODE_CLEANUP_NOT_USED_VARIABLES, false);

		store.setDefault(PreferenceConstants.PREF_COLORED_PROJECTS_LEFT_RULER_ENABLED, false);
		store.setDefault(PreferenceConstants.PREF_COLORED_PROJECTS_RIGHT_RULER_ENABLED, false);
		store.setDefault(PreferenceConstants.PREF_COLORED_PROJECTS_STATUS_BAR_ENABLED, false);
		store.setDefault(PreferenceConstants.PREF_COLORED_PROJECTS_TITLE_ICON_ENABLED, true);
		store.setDefault(PreferenceConstants.PREF_COLORED_PROJECTS_TITLE_ICON_WIDTH_PERCENT, 35);
		store.setDefault(PreferenceConstants.PREF_COLORED_PROJECTS_TITLE_ICON_HEIGTH_PERCENT, 100);
		store.setDefault(PreferenceConstants.PREF_COLORED_PROJECTS_STATUS_BAR_WIDGET_ENABLED, false);
		store.setDefault(PreferenceConstants.PREF_COLORED_PROJECTS_NEW_DIALOG_ENABLED, true);

		store.setDefault(PreferenceConstants.PREF_UNIT_RUN_CRITICAL_TESTS_ENABLED, true);
		store.setDefault(PreferenceConstants.PREF_UNIT_RUN_DANGEROUS_TESTS_ENABLED, true);
		store.setDefault(PreferenceConstants.PREF_UNIT_RUN_HARMLESS_TESTS_ENABLED, true);

		store.setDefault(PreferenceConstants.PREF_UNIT_RUN_DURATION_LONG_TESTS_ENABLED, true);
		store.setDefault(PreferenceConstants.PREF_UNIT_RUN_DURATION_MEDIUM_TESTS_ENABLED, true);
		store.setDefault(PreferenceConstants.PREF_UNIT_RUN_DURATION_SHORT_TESTS_ENABLED, true);
		store.setDefault(PreferenceConstants.PREF_DIALOG_NEW_PACKAGE_FOR_CI_RUN_ENABLED, true);

		PreferenceConverter.setDefault(store, PreferenceConstants.PREF_UNIT_TEST_OK_BACKGROUND_COLOR,
				new RGB(255, 255, 255));
		PreferenceConverter.setDefault(store, PreferenceConstants.PREF_UNIT_TEST_FAIL_BACKGROUND_COLOR,
				new RGB(255, 122, 122));

		PreferenceConverter.setDefault(store, PreferenceConstants.PREF_ATC_TEST_FAIL_BACKGROUND_COLOR,
				new RGB(122, 122, 255));

	}

}
