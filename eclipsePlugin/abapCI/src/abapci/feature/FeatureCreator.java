package abapci.feature;

import org.eclipse.jface.preference.IPreferenceStore;

import abapci.AbapCiPluginHelper;
import abapci.feature.activeFeature.AbapGitPackageChangeFeature;
import abapci.feature.activeFeature.AtcFeature;
import abapci.feature.activeFeature.ColorFeature;
import abapci.feature.activeFeature.ColoredProjectFeature;
import abapci.feature.activeFeature.DeveloperFeature;
import abapci.feature.activeFeature.FeatureType;
import abapci.feature.activeFeature.JenkinsFeature;
import abapci.feature.activeFeature.PrettyPrinterFeature;
import abapci.feature.activeFeature.SimpleToggleFeature;
import abapci.feature.activeFeature.TddModeFeature;
import abapci.feature.activeFeature.UnitFeature;
import abapci.preferences.PreferenceConstants;

public class FeatureCreator {

	private IPreferenceStore prefs;
	AbapCiPluginHelper abapCiPluginHelper;

	public FeatureCreator() {
		abapCiPluginHelper = new AbapCiPluginHelper();
	}

	private void initPrefs() {
		prefs = (prefs == null) ? abapCiPluginHelper.getPreferenceStore() : prefs;
	}

	public UnitFeature createUnitFeature() {
		initPrefs();
		final UnitFeature feature = new UnitFeature();
		feature.setActive(prefs.getBoolean(PreferenceConstants.PREF_UNIT_RUN_ON_SAVE));
		feature.setRunActivatedObjectsOnly(prefs.getBoolean(PreferenceConstants.PREF_UNIT_RUN_ACTIVATED_OBJECTS_ONLY));
		return feature;
	}

	public AtcFeature createAtcFeature() {
		initPrefs();
		final AtcFeature feature = new AtcFeature();
		feature.setRunActivatedObjects(prefs.getBoolean(PreferenceConstants.PREF_ATC_RUN_ACTIVATED_OBJECTS_ONLY));
		feature.setAnnotationhandlingEnabled(
				prefs.getBoolean(PreferenceConstants.PREF_ATC_ANNOTATION_HANDLING_ENABLED));
		feature.setActive(feature.isRunActivatedObjects());
		feature.setVariant(prefs.getString(PreferenceConstants.PREF_ATC_VARIANT));
		return feature;
	}

	public SourceCodeVisualisationFeature createSourceCodeVisualisationFeature() {
		initPrefs();
		final SourceCodeVisualisationFeature feature = new SourceCodeVisualisationFeature();
		feature.setChangeStatusBarBackgroundColorEnabled(
				prefs.getBoolean(PreferenceConstants.PREF_CHANGE_STATUS_BAR_BACKGROUND_COLOR));
		feature.setShowStatusBarWidgetEnabled(
				prefs.getBoolean(PreferenceConstants.PREF_VISUALISATION_STATUS_BAR_WIDGET_ENABLED));
		feature.setThemeUpdateEnabled(
				prefs.getBoolean(PreferenceConstants.PREF_VISUALISATION_STATUS_CHANGE_THEME_ENABLED));
		return feature;

	}

	public TddModeFeature createTddModeFeature() {
		initPrefs();
		final TddModeFeature feature = new TddModeFeature();
		feature.setActive(prefs.getBoolean(PreferenceConstants.PREF_TDD_MODE));
		return feature;
	}

	public ColoredProjectFeature createColoredProjectFeature() {
		initPrefs();
		final ColoredProjectFeature feature = new ColoredProjectFeature();
		feature.setChangeStatusBarActive(
				prefs.getBoolean(PreferenceConstants.PREF_COLORED_PROJECTS_STATUS_BAR_ENABLED));
		feature.setLeftRulerActive(prefs.getBoolean(PreferenceConstants.PREF_COLORED_PROJECTS_LEFT_RULER_ENABLED));
		feature.setRightRulerActive(prefs.getBoolean(PreferenceConstants.PREF_COLORED_PROJECTS_RIGHT_RULER_ENABLED));
		feature.setStatusBarWidgetActive(
				prefs.getBoolean(PreferenceConstants.PREF_COLORED_PROJECTS_STATUS_BAR_WIDGET_ENABLED));
		feature.setTitleIconActive(prefs.getBoolean(PreferenceConstants.PREF_COLORED_PROJECTS_TITLE_ICON_ENABLED));
		feature.setTitleIconWidth(prefs.getInt(PreferenceConstants.PREF_COLORED_PROJECTS_TITLE_ICON_WIDTH_PERCENT));
		feature.setTitleIconHeight(prefs.getInt(PreferenceConstants.PREF_COLORED_PROJECTS_TITLE_ICON_HEIGTH_PERCENT));
		feature.setDialogEnabled((prefs.getBoolean(PreferenceConstants.PREF_COLORED_PROJECTS_NEW_DIALOG_ENABLED)));

		return feature;
	}

	public JenkinsFeature createJenkinsFeature() {
		initPrefs();
		final JenkinsFeature feature = new JenkinsFeature();
		feature.setActive(prefs.getBoolean(PreferenceConstants.PREF_JENKINS_RUN_AFTER_UNIT_TESTS_TURN_GREEN));
		return feature;
	}

	public PrettyPrinterFeature createSourcecodeFormattingFeature() {
		initPrefs();
		final String prefix = prefs.getString(PreferenceConstants.PREF_SOURCE_CODE_FORMATTING_PREFIX);
		final PrettyPrinterFeature feature = new PrettyPrinterFeature(prefix);
		feature.setActive(prefs.getBoolean(PreferenceConstants.PREF_SOURCE_CODE_FORMATTING_ENABLED));
		feature.setCleanupVariablesEnabled(
				prefs.getBoolean(PreferenceConstants.PREF_SOURCE_CODE_CLEANUP_NOT_USED_VARIABLES));
		return feature;
	}

	public AbapGitPackageChangeFeature createAbapGitPackageChangeFeature() {
		initPrefs();
		final AbapGitPackageChangeFeature feature = new AbapGitPackageChangeFeature();
		feature.setActive(prefs.getBoolean(PreferenceConstants.PREF_ABAP_GIT_PACKAGE_CHANGE_ENABLED));
		return feature;
	}

	public SimpleToggleFeature createSimpleToggleFeature(FeatureType featureType) {
		initPrefs();
		final SimpleToggleFeature feature = new SimpleToggleFeature();

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

		final ColorFeature colorFeature = new ColorFeature();

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

	public DeveloperFeature createDeveloperFeature() {
		final DeveloperFeature developerFeature = new DeveloperFeature();
		developerFeature
				.setJavaSimuModeEnabled(prefs.getBoolean(PreferenceConstants.PREF_DEVELOPER_JAVA_SIMU_MODE_ENABLED));
		developerFeature.setTracingEnabled(prefs.getBoolean(PreferenceConstants.PREF_DEVELOPER_TRACING_ENABLED));

		return developerFeature;
	}

}
