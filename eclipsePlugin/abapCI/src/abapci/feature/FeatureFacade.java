package abapci.feature;

import org.eclipse.jface.preference.IPreferenceStore;

import abapci.AbapCiPlugin;
import abapci.feature.activeFeature.AbapGitPackageChangeFeature;
import abapci.feature.activeFeature.ActiveFeature;
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

public class FeatureFacade {
	private IPreferenceStore prefs;
	private final FeatureCreator featureCreator;

	public FeatureFacade() {
		featureCreator = new FeatureCreator();
	}

	private IPreferenceStore getPrefs() {
		if (prefs == null) {
			prefs = AbapCiPlugin.getDefault().getPreferenceStore();
		}
		return prefs;
	}

	public UnitFeature getUnitFeature() {
		return featureCreator.createUnitFeature();
	}

	public AtcFeature getAtcFeature() {
		return featureCreator.createAtcFeature();
	}

	public JenkinsFeature getJenkinsFeature() {
		return featureCreator.createJenkinsFeature();
	}

	public PrettyPrinterFeature getSourcecodeFormattingFeature() {
		return featureCreator.createSourcecodeFormattingFeature();
	}

	public AbapGitPackageChangeFeature getAbapGitPackageChangeFeature() {
		return featureCreator.createAbapGitPackageChangeFeature();
	}

	public SimpleToggleFeature getUnitCriticalActiveFeature() {
		return featureCreator.createSimpleToggleFeature(FeatureType.UNIT_RUN_CRITICAL_TESTS);
	}

	public ActiveFeature getUnitDangerousActiveFeature() {
		return featureCreator.createSimpleToggleFeature(FeatureType.UNIT_RUN_DANGEROUS_TESTS);
	}

	public ActiveFeature getUnitHarmlessActiveFeature() {
		return featureCreator.createSimpleToggleFeature(FeatureType.UNIT_RUN_HARMLESS_TESTS);
	}

	public ActiveFeature getUnitTestDurationLongFeature() {
		return featureCreator.createSimpleToggleFeature(FeatureType.UNIT_RUN_DURATION_LONG_TESTS);
	}

	public ActiveFeature getUnitTestDurationMediumFeature() {
		return featureCreator.createSimpleToggleFeature(FeatureType.UNIT_RUN_DURATION_MEDIUM_TESTS);
	}

	public ActiveFeature getUnitTestDurationShortFeature() {
		return featureCreator.createSimpleToggleFeature(FeatureType.UNIT_RUN_DURATION_SHORT_TESTS);
	}

	public ActiveFeature getShowDialogNewPackageForCiRun() {
		return featureCreator.createSimpleToggleFeature(FeatureType.SHOW_DIALOG_NEW_PACKAGE_FOR_CI_RUN);
	}

	public void setShowDialogNewPackageForCiRun(boolean active) {
		SimpleToggleFeature simpleToggleFeature = featureCreator
				.createSimpleToggleFeature(FeatureType.SHOW_DIALOG_NEW_PACKAGE_FOR_CI_RUN);
		simpleToggleFeature.setActive(active);
		simpleToggleFeature.writePreference();
	}

	public void setAtcFeatureVariant(String variant) {
		getPrefs().setValue(PreferenceConstants.PREF_ATC_VARIANT, variant);
	}

	public ColorFeature getTestRunOkColorFeature() {
		return featureCreator.createSimpleColorFeature(ColorFeatureType.PREF_UNIT_TEST_OK_BACKGROUND_COLOR);
	}

	public ColorFeature getTestRunFailColorFeature() {
		return featureCreator.createSimpleColorFeature(ColorFeatureType.PREF_UNIT_TEST_FAIL_BACKGROUND_COLOR);
	}

	public ColorFeature getAtcRunFailColorFeature() {
		return featureCreator.createSimpleColorFeature(ColorFeatureType.PREF_ATC_TEST_FAIL_BACKGROUND_COLOR);
	}

	public ColoredProjectFeature getColoredProjectFeature() {
		return featureCreator.createColoredProjectFeature();
	}

	public TddModeFeature getTddModeFeature() {
		return featureCreator.createTddModeFeature();
	}

	public SourceCodeVisualisationFeature getSourceCodeVisualisationFeature() {
		return featureCreator.createSourceCodeVisualisationFeature();
	}

	public DeveloperFeature getDeveloperFeature() {
		return featureCreator.createDeveloperFeature();
	}
}
