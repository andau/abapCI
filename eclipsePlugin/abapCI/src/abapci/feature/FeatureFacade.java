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
		return featureFactory.createAbapUnitFeature();
	}

	public AtcFeature getAtcFeature() {
		return featureFactory.createAtcFeature();
	}

	public ColorChangerFeature getColorChangerFeature() {
		return featureFactory.createColorChangerFeature();
	}

	public void setAtcFeatureVariant(String variant) {
		getPrefs().setValue(PreferenceConstants.PREF_ATC_VARIANT, variant);
	}

	public JenkinsFeature getJenkinsFeature() {
		return featureFactory.createJenkinsFeature();
	}

	public GeneralFeature getGeneralFeature() {
		// TODO Auto-generated method stub
		return featureFactory.createGeneralFeature();
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
}
