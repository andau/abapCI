package abapci.feature;

import static org.mockito.Mockito.times;

import org.eclipse.jface.preference.IPreferenceStore;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;

import abapci.AbapCiPluginHelper;
import abapci.preferences.PreferenceConstants;

public class FeatureCreatorTest {

	FeatureCreator cut;

	IPreferenceStore prefs = Mockito.mock(IPreferenceStore.class);

	@Before
	public void before() {
		cut = new FeatureCreator();

		Mockito.reset(prefs);
		Whitebox.setInternalState(cut, "prefs", prefs);

	}

	@Test
	public void testAbapGitPackageChangeFeature() {

		cut.createAbapGitPackageChangeFeature();
		Mockito.verify(prefs, times(1)).getBoolean(PreferenceConstants.PREF_ABAP_GIT_PACKAGE_CHANGE_ENABLED);
		Mockito.verify(prefs, times(1)).getBoolean(Mockito.any(String.class));

	}

	@Test
	public void testCreateAtcFeature() {

		cut.createAtcFeature();
		Mockito.verify(prefs, times(1)).getBoolean(PreferenceConstants.PREF_ATC_RUN_ACTIVATED_OBJECTS_ONLY);
		Mockito.verify(prefs, times(2)).getBoolean(Mockito.any(String.class));

	}

	@Test
	public void testCreateColoredProjectFeature() {

		cut.createColoredProjectFeature();
		Mockito.verify(prefs, times(1)).getBoolean(PreferenceConstants.PREF_COLORED_PROJECTS_STATUS_BAR_ENABLED);
		Mockito.verify(prefs, times(6)).getBoolean(Mockito.any(String.class));

	}

	@Test
	public void testCreateJenkinsFeature() {

		cut.createJenkinsFeature();
		Mockito.verify(prefs, times(1)).getBoolean(PreferenceConstants.PREF_JENKINS_RUN_AFTER_UNIT_TESTS_TURN_GREEN);
		Mockito.verify(prefs, times(1)).getBoolean(Mockito.any(String.class));
	}

	@Test
	@Ignore
	public void testSimpleColorFeature() {
		AbapCiPluginHelper abapCiPluginHelper = Mockito.mock(AbapCiPluginHelper.class);
		IPreferenceStore preferenceStore = Mockito.mock(IPreferenceStore.class);
		Mockito.when(abapCiPluginHelper.getPreferenceStore()).thenReturn(preferenceStore);

		Whitebox.setInternalState(cut, "abapCiPluginHelper", abapCiPluginHelper);

	}

	@Test
	public void testSourcecodeFormattingFeature() {

		cut.createSourcecodeFormattingFeature();
		Mockito.verify(prefs, times(1)).getBoolean(PreferenceConstants.PREF_SOURCE_CODE_FORMATTING_ENABLED);
		Mockito.verify(prefs, times(1)).getBoolean(PreferenceConstants.PREF_SOURCE_CODE_CLEANUP_NOT_USED_VARIABLES);
		Mockito.verify(prefs, times(2)).getBoolean(Mockito.any(String.class));
	}

	@Test
	public void testSourceCodeVisualisationFeature() {

		cut.createSourceCodeVisualisationFeature();
		Mockito.verify(prefs, times(1)).getBoolean(PreferenceConstants.PREF_CHANGE_STATUS_BAR_BACKGROUND_COLOR);
		Mockito.verify(prefs, times(1)).getBoolean(PreferenceConstants.PREF_VISUALISATION_STATUS_BAR_WIDGET_ENABLED);

		Mockito.verify(prefs, times(2)).getBoolean(Mockito.any(String.class));
	}

	@Test
	public void testTddModeFeature() {

		cut.createTddModeFeature();
		Mockito.verify(prefs, times(1)).getBoolean(PreferenceConstants.PREF_TDD_MODE);
		Mockito.verify(prefs, times(1)).getBoolean(Mockito.any(String.class));

	}

	@Test
	public void testCreateUnitFeature() {

		cut.createUnitFeature();
		Mockito.verify(prefs, times(1)).getBoolean(PreferenceConstants.PREF_UNIT_RUN_ON_SAVE);
		Mockito.verify(prefs, times(1)).getBoolean(PreferenceConstants.PREF_UNIT_RUN_ACTIVATED_OBJECTS_ONLY);
		Mockito.verify(prefs, times(2)).getBoolean(Mockito.any(String.class));

	}

}
