package abapci.feature;

import org.eclipse.jface.preference.IPreferenceStore;

import abapci.AbapCiPlugin;
import abapci.preferences.PreferenceConstants;

public class FeatureFactory {

	private IPreferenceStore prefs;

	private void initPrefs() {
		prefs = (prefs == null) ? AbapCiPlugin.getDefault().getPreferenceStore() : prefs;
	}

	public UnitFeature createUnitFeature() {
		initPrefs();
		UnitFeature feature = new UnitFeature();
		feature.setActive(prefs.getBoolean(PreferenceConstants.PREF_UNIT_RUN_ON_SAVE));
		feature.setRunActivatedObjectsOnly(prefs.getBoolean(PreferenceConstants.PREF_UNIT_RUN_ACTIVATED_OBJECTS_ONLY));
		return feature;
	}

	public AtcFeature createAtcFeature() {
		initPrefs();
		AtcFeature feature = new AtcFeature();
		feature.setRunActivatedObjects(prefs.getBoolean(PreferenceConstants.PREF_ATC_RUN_ACTIVATED_OBJECTS_ONLY));
		feature.setActive(feature.isRunActivatedObjects());
		feature.setVariant(prefs.getString(PreferenceConstants.PREF_ATC_VARIANT));
		return feature;
	}

	public SourceCodeVisualisationFeature createSourceCodeVisualisationFeature() {
		initPrefs();
		SourceCodeVisualisationFeature feature = new SourceCodeVisualisationFeature();
		feature.setChangeStatusBarBackgroundColorEnabled(
				prefs.getBoolean(PreferenceConstants.PREF_CHANGE_STATUS_BAR_BACKGROUND_COLOR));
		feature.setShowStatusBarWidgetEnabled(prefs.getBoolean(PreferenceConstants.PREF_VISUALISATION_STATUS_BAR_WIDGET_ENABLED));
		return feature;

	}

	public TddModeFeature createTddModeFeature() {
		initPrefs();
		TddModeFeature feature = new TddModeFeature();
		feature.setActive(prefs.getBoolean(PreferenceConstants.PREF_TDD_MODE));
		return feature;
	}

	public ColorChangerFeature createColorChangerFeature() {
		initPrefs();
		ColorChangerFeature feature = new ColorChangerFeature();
		feature.setActive(prefs.getBoolean(PreferenceConstants.PREF_CHANGE_COLOR_ON_FAILED_TESTS));
		return feature;
	}

	public ColoredProjectFeature createColoredProjectFeature() {
		initPrefs();
		ColoredProjectFeature feature = new ColoredProjectFeature();
		feature.setChangeStatusBarActive(prefs.getBoolean(PreferenceConstants.PREF_COLORED_PROJECTS_STATUS_BAR_ENABLED));
		feature.setLeftRulerActive(prefs.getBoolean(PreferenceConstants.PREF_COLORED_PROJECTS_LEFT_RULER_ENABLED));
		feature.setRightRulerActive(prefs.getBoolean(PreferenceConstants.PREF_COLORED_PROJECTS_RIGHT_RULER_ENABLED));
		feature.setStatusBarWidgetActive(prefs.getBoolean(PreferenceConstants.PREF_COLORED_PROJECTS_STATUS_BAR_WIDGET_ENABLED));
		feature.setTitleIconActive(prefs.getBoolean(PreferenceConstants.PREF_COLORED_PROJECTS_TITLE_ICON_ENABLED));
		feature.setTitleIconWidth(prefs.getInt(PreferenceConstants.PREF_COLORED_PROJECTS_TITLE_ICON_WIDTH_PERCENT)); 
		feature.setTitleIconHeight(prefs.getInt(PreferenceConstants.PREF_COLORED_PROJECTS_TITLE_ICON_HEIGTH_PERCENT));
		feature.setDialogEnabled((prefs.getBoolean(PreferenceConstants.PREF_COLORED_PROJECTS_NEW_DIALOG_ENABLED)));

		return feature;
	}

	public JenkinsFeature createJenkinsFeature() {
		initPrefs();
		JenkinsFeature feature = new JenkinsFeature();
		feature.setActive(prefs.getBoolean(PreferenceConstants.PREF_JENKINS_RUN_AFTER_UNIT_TESTS_TURN_GREEN));
		return feature;
	}

	public ColoredProjectsTabHeaderFeature createColoredProjectsTabHeaderFeature() {
		initPrefs();
		ColoredProjectsTabHeaderFeature feature = new ColoredProjectsTabHeaderFeature();
		feature.setActive(prefs.getBoolean(PreferenceConstants.PREF_COLORED_PROJECTS_TAB_HEADER_ENABLED));
		return feature;
	}

	public ColoredProjectsLeftRulerFeature createColoredProjectsLeftRulerFeature() {
		initPrefs();
		ColoredProjectsLeftRulerFeature feature = new ColoredProjectsLeftRulerFeature();
		feature.setActive(prefs.getBoolean(PreferenceConstants.PREF_COLORED_PROJECTS_LEFT_RULER_ENABLED));
		return feature;
	}

	public ColoredProjectsRightRulerFeature createColoredProjectsRightRulerFeature() {
		initPrefs();
		ColoredProjectsRightRulerFeature feature = new ColoredProjectsRightRulerFeature();
		feature.setActive(prefs.getBoolean(PreferenceConstants.PREF_COLORED_PROJECTS_RIGHT_RULER_ENABLED));
		return feature;
	}

	public SourcecodeFormattingFeature createSourcecodeFormattingFeature() {
		initPrefs();
		String prefix = prefs.getString(PreferenceConstants.PREF_SOURCE_CODE_FORMATTING_PREFIX);
		SourcecodeFormattingFeature feature = new SourcecodeFormattingFeature(prefix);
		feature.setActive(prefs.getBoolean(PreferenceConstants.PREF_SOURCE_CODE_FORMATTING_ENABLED));
		feature.setCleanupVariablesEnabled(
				prefs.getBoolean(PreferenceConstants.PREF_SOURCE_CODE_CLEANUP_NOT_USED_VARIABLES));
		return feature;
	}

	public AbapGitPackageChangeFeature createAbapGitPackageChangeFeature() {
		initPrefs();
		AbapGitPackageChangeFeature feature = new AbapGitPackageChangeFeature();
		feature.setActive(prefs.getBoolean(PreferenceConstants.PREF_ABAP_GIT_PACKAGE_CHANGE_ENABLED));
		return feature;
	}

	public SimpleToggleFeature createSimpleToggleFeature(FeatureType featureType) {
		initPrefs();
		SimpleToggleFeature feature = new SimpleToggleFeature();

		switch (featureType) {
		case UNIT_RUN_CRITICAL_TESTS:
			feature.setPreferenceConstant(PreferenceConstants.PREF_UNIT_RUN_CRITICAL_TESTS_ENABLED);
			feature.setActive(prefs.getBoolean(PreferenceConstants.PREF_UNIT_RUN_CRITICAL_TESTS_ENABLED));
			break;
		case UNIT_RUN_DANGEROUS_TESTS:
			feature.setActive(prefs.getBoolean(PreferenceConstants.PREF_UNIT_RUN_DANGEROUS_TESTS_ENABLED));
			break;
		case UNIT_RUN_HARMLESS_TESTS:
			feature.setActive(prefs.getBoolean(PreferenceConstants.PREF_UNIT_RUN_HARMLESS_TESTS_ENABLED));
			break;
		case UNIT_RUN_DURATION_LONG_TESTS:
			feature.setActive(prefs.getBoolean(PreferenceConstants.PREF_UNIT_RUN_DURATION_LONG_TESTS_ENABLED));
			break;
		case UNIT_RUN_DURATION_MEDIUM_TESTS:
			feature.setActive(prefs.getBoolean(PreferenceConstants.PREF_UNIT_RUN_DURATION_MEDIUM_TESTS_ENABLED));
			break;
		case UNIT_RUN_DURATION_SHORT_TESTS:
			feature.setActive(prefs.getBoolean(PreferenceConstants.PREF_UNIT_RUN_DURATION_SHORT_TESTS_ENABLED));
			break;
		case SHOW_DIALOG_NEW_PACKAGE_FOR_CI_RUN:
			feature.setPreferenceConstant(PreferenceConstants.PREF_DIALOG_NEW_PACKAGE_FOR_CI_RUN_ENABLED);
			feature.setActive(prefs.getBoolean(PreferenceConstants.PREF_DIALOG_NEW_PACKAGE_FOR_CI_RUN_ENABLED));
			break;

		default:
			throw new UnsupportedOperationException();
		}

		return feature;
	}

	public ColorFeature createSimpleColorFeature(ColorFeatureType colorFeatureType) {
		initPrefs();

		ColorFeature colorFeature = new ColorFeature();

		switch (colorFeatureType) {
		case PREF_UNIT_TEST_OK_BACKGROUND_COLOR:
			colorFeature.setPreferenceConstant(PreferenceConstants.PREF_UNIT_TEST_OK_BACKGROUND_COLOR);
			break;
		case PREF_UNIT_TEST_FAIL_BACKGROUND_COLOR:
			colorFeature.setPreferenceConstant(PreferenceConstants.PREF_UNIT_TEST_FAIL_BACKGROUND_COLOR);
			break;
		case PREF_ATC_TEST_FAIL_BACKGROUND_COLOR:
			colorFeature.setPreferenceConstant(PreferenceConstants.PREF_ATC_TEST_FAIL_BACKGROUND_COLOR);
			break;

		default:
			throw new UnsupportedOperationException();
		}

		return colorFeature;
	}

}
