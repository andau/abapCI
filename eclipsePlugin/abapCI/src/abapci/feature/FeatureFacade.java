package abapci.feature;

import org.eclipse.jface.preference.IPreferenceStore;

import abapci.AbapCiPlugin;
import abapci.preferences.PreferenceConstants;

public class FeatureFacade {
	private IPreferenceStore prefs;
	private FeatureFactory featureFactory;

	public FeatureFacade() {
		featureFactory = new FeatureFactory();
	}

	private IPreferenceStore getPrefs() {
		if (prefs == null) {
			prefs = AbapCiPlugin.getDefault().getPreferenceStore();
		}
		return prefs;
	}

	public UnitFeature getUnitFeature() {
		return featureFactory.createUnitFeature();
	}

	public AtcFeature getAtcFeature() {
		return featureFactory.createAtcFeature();
	}

	public ColorChangerFeature getColorChangerFeature() {
		return featureFactory.createColorChangerFeature();
	}

	public JenkinsFeature getJenkinsFeature() {
		return featureFactory.createJenkinsFeature();
	}

	public ColoredProjectsTabHeaderFeature getColoredProjectsTabHeaderFeature() {
		return featureFactory.createColoredProjectsTabHeaderFeature();
	}

	public ColoredProjectsLeftRulerFeature getColoredProjectsLeftRulerFeature() {
		return featureFactory.createColoredProjectsLeftRulerFeature();
	}

	public ColoredProjectsRightRulerFeature getColoredProjectsRightRulerFeature() {
		return featureFactory.createColoredProjectsRightRulerFeature();
	}

	public SourcecodeFormattingFeature getSourcecodeFormattingFeature() {
		return featureFactory.createSourcecodeFormattingFeature();
	}

	public AbapGitPackageChangeFeature getAbapGitPackageChangeFeature() {
		return featureFactory.createAbapGitPackageChangeFeature();
	}

	public SimpleToggleFeature getUnitCriticalActiveFeature() {
		return featureFactory.createSimpleToggleFeature(FeatureType.UNIT_RUN_CRITICAL_TESTS);
	}

	public ActiveFeature getUnitDangerousActiveFeature() {
		return featureFactory.createSimpleToggleFeature(FeatureType.UNIT_RUN_DANGEROUS_TESTS);
	}

	public ActiveFeature getUnitHarmlessActiveFeature() {
		return featureFactory.createSimpleToggleFeature(FeatureType.UNIT_RUN_HARMLESS_TESTS);
	}

	public ActiveFeature getUnitTestDurationLongFeature() {
		return featureFactory.createSimpleToggleFeature(FeatureType.UNIT_RUN_DURATION_LONG_TESTS);
	}

	public ActiveFeature getUnitTestDurationMediumFeature() {
		return featureFactory.createSimpleToggleFeature(FeatureType.UNIT_RUN_DURATION_MEDIUM_TESTS);
	}

	public ActiveFeature getUnitTestDurationShortFeature() {
		return featureFactory.createSimpleToggleFeature(FeatureType.UNIT_RUN_DURATION_SHORT_TESTS);
	}

	public ActiveFeature getShowDialogNewPackageForCiRun() {
		return featureFactory.createSimpleToggleFeature(FeatureType.SHOW_DIALOG_NEW_PACKAGE_FOR_CI_RUN);
	}

	public void setShowDialogNewPackageForCiRun(boolean active) {
		SimpleToggleFeature simpleToggleFeature = featureFactory
				.createSimpleToggleFeature(FeatureType.SHOW_DIALOG_NEW_PACKAGE_FOR_CI_RUN);
		simpleToggleFeature.setActive(active);
		simpleToggleFeature.writePreference();
	}

	public void setAtcFeatureVariant(String variant) {
		getPrefs().setValue(PreferenceConstants.PREF_ATC_VARIANT, variant);
	}

	public ColorFeature getTestRunOkColorFeature() {
		return featureFactory.createSimpleColorFeature(ColorFeatureType.PREF_UNIT_TEST_OK_BACKGROUND_COLOR);
	}

	public ColorFeature getTestRunFailColorFeature() {
		return featureFactory.createSimpleColorFeature(ColorFeatureType.PREF_UNIT_TEST_FAIL_BACKGROUND_COLOR);
	}

	public ColorFeature getAtcRunFailColorFeature() {
		return featureFactory.createSimpleColorFeature(ColorFeatureType.PREF_ATC_TEST_FAIL_BACKGROUND_COLOR);
	}

}
