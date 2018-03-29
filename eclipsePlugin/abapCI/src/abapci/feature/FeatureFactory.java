package abapci.feature;

import org.eclipse.jface.preference.IPreferenceStore;

import abapci.AbapCiPlugin;
import abapci.preferences.PreferenceConstants;

public class FeatureFactory {

	private IPreferenceStore prefs;

	private void initPrefs() {
		prefs = (prefs == null) ? AbapCiPlugin.getDefault().getPreferenceStore() : prefs;
	}

	public GeneralFeature createGeneralFeature() {
		initPrefs();
		GeneralFeature feature = new GeneralFeature();
		feature.setDevelopmentProject(prefs.getString(PreferenceConstants.PREF_DEV_PROJECT));
		return feature;
	}

	public UnitFeature createAbapUnitFeature() {
		initPrefs();
		UnitFeature feature = new UnitFeature();
		feature.setActive(prefs.getBoolean(PreferenceConstants.PREF_UNIT_RUN_ON_SAVE));
		feature.setInterval(prefs.getInt(PreferenceConstants.PREF_UNIT_RUN_INTERVAL));
		return feature;
	}

	public AtcFeature createAtcFeature() {
		initPrefs();
		AtcFeature feature = new AtcFeature(); 
		feature.setActive(prefs.getBoolean(PreferenceConstants.PREF_ATC_RUN_AFTER_UNIT_TESTS_TURN_GREEN));
		feature.setVariant(prefs.getString(PreferenceConstants.PREF_ATC_VARIANT)); 
		return feature;
	}

	public ColorChangerFeature createColorChangerFeature() {
		initPrefs();
		ColorChangerFeature feature = new ColorChangerFeature(); 
		feature.setActive(prefs.getBoolean(PreferenceConstants.PREF_CHANGE_COLOR_ON_FAILED_TESTS));
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


	

}
