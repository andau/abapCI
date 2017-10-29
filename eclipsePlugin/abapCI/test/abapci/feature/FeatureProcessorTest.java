package abapci.feature;

import org.eclipse.jface.preference.IPreferenceStore;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Matchers.any;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import abapci.AbapCiPlugin;
import abapci.domain.SourcecodeState;
import abapci.domain.TestState;
import abapci.manager.AUnitTestManager;
import abapci.manager.AtcTestManager;
import abapci.manager.ThemeUpdateManager;
import abapci.preferences.PreferenceConstants;

@RunWith(PowerMockRunner.class)
@PrepareForTest(AbapCiPlugin.class)
public class FeatureProcessorTest {

	FeatureProcessor featureProcessor;

	@Mock
	IPreferenceStore preferenceStore;
	@Mock
	FeatureFacade featureFacade;
	@Mock
	FeatureFactory featureFactory;
	@Mock
	AUnitTestManager aUnitTestManager;
	@Mock
	AtcTestManager atcTestManager;
	@Mock
	AbapCiPlugin abapCiPlugin;
	@Mock
	ThemeUpdateManager themeUpdateManager;

	@Before
	public void setUp() throws Exception {

		featureFacade = new FeatureFacade();
		featureFactory = new FeatureFactory();
		Whitebox.setInternalState(featureFactory, "prefs", preferenceStore);
		Whitebox.setInternalState(featureFacade, "featureFactory", featureFactory);

		featureProcessor = new FeatureProcessor();
		Whitebox.setInternalState(featureProcessor, "featureFacade", featureFacade);
		Whitebox.setInternalState(featureProcessor, "aUnitTestManager", aUnitTestManager);
		Whitebox.setInternalState(featureProcessor, "atcTestManager", atcTestManager);
		Whitebox.setInternalState(featureProcessor, "themeUpdateManager", themeUpdateManager);

	}

	@Test
	public void unitOkTest() throws Exception {

		setMockedPreferences(PreferenceConstants.PREF_UNIT_RUN_ON_SAVE, true);
		setMockedPreferences(PreferenceConstants.PREF_ATC_RUN_AFTER_UNIT_TESTS_TURN_GREEN, false);

		verifyUpdateThemeCallAfterUnitRun(TestState.OK, SourcecodeState.OK);
	}

	@Test
	public void unitNokTest() throws Exception {

		setMockedPreferences(PreferenceConstants.PREF_UNIT_RUN_ON_SAVE, true);
		setMockedPreferences(PreferenceConstants.PREF_ATC_RUN_AFTER_UNIT_TESTS_TURN_GREEN, false);

		verifyUpdateThemeCallAfterUnitRun(TestState.NOK, SourcecodeState.UT_FAIL);
	}

	@Test
	public void unitUndefTest() throws Exception {

		setMockedPreferences(PreferenceConstants.PREF_UNIT_RUN_ON_SAVE, true);
		setMockedPreferences(PreferenceConstants.PREF_ATC_RUN_AFTER_UNIT_TESTS_TURN_GREEN, false);

		verifyUpdateThemeCallAfterUnitRun(TestState.UNDEF, SourcecodeState.UNDEF);

	}

	@Test
	public void unitOfflTest() throws Exception {

		setMockedPreferences(PreferenceConstants.PREF_UNIT_RUN_ON_SAVE, true);
		setMockedPreferences(PreferenceConstants.PREF_ATC_RUN_AFTER_UNIT_TESTS_TURN_GREEN, false);

		verifyUpdateThemeCallAfterUnitRun(TestState.OFFL, SourcecodeState.OFFL);
	}

	@Test
	public void unitOkAndAtcNokTest() throws Exception {
		setMockedPreferences(PreferenceConstants.PREF_UNIT_RUN_ON_SAVE, true);
		setMockedPreferences(PreferenceConstants.PREF_ATC_RUN_AFTER_UNIT_TESTS_TURN_GREEN, true);

		verfifyUpdateThemeCallAfterUnitAndAtcRun(TestState.OK, TestState.NOK, SourcecodeState.OK,
				SourcecodeState.ATC_FAIL);
	}

	@Test
	public void unitOkAndAtcOkTest() throws Exception {
		setMockedPreferences(PreferenceConstants.PREF_UNIT_RUN_ON_SAVE, true);
		setMockedPreferences(PreferenceConstants.PREF_ATC_RUN_AFTER_UNIT_TESTS_TURN_GREEN, true);

		verfifyUpdateThemeCallAfterUnitAndAtcRun(TestState.OK, TestState.OK, SourcecodeState.OK, SourcecodeState.OK);

	}

	@Test
	public void unitNokAndAtcOkTest() throws Exception {
		setMockedPreferences(PreferenceConstants.PREF_UNIT_RUN_ON_SAVE, true);
		setMockedPreferences(PreferenceConstants.PREF_ATC_RUN_AFTER_UNIT_TESTS_TURN_GREEN, true);

		verfifyUpdateThemeCallAfterUnitAndAtcRun(TestState.NOK, TestState.OK, SourcecodeState.UT_FAIL, null);

	}

	@Test
	public void unitNokAndAtcNokTest() throws Exception {
		setMockedPreferences(PreferenceConstants.PREF_UNIT_RUN_ON_SAVE, true);
		setMockedPreferences(PreferenceConstants.PREF_ATC_RUN_AFTER_UNIT_TESTS_TURN_GREEN, true);

		verfifyUpdateThemeCallAfterUnitAndAtcRun(TestState.NOK, TestState.NOK, SourcecodeState.UT_FAIL, null);

	}

	private void verifyUpdateThemeCallAfterUnitRun(TestState testState, SourcecodeState sourcecodeState) {
		PowerMockito.when(aUnitTestManager.executeAllPackages()).thenReturn(testState);
		featureProcessor.processEnabledFeatures();

		Mockito.verify(themeUpdateManager, Mockito.times(1)).updateTheme(sourcecodeState);
		Mockito.verify(themeUpdateManager, Mockito.times(1)).updateTheme(any(SourcecodeState.class));
	}

	private void verfifyUpdateThemeCallAfterUnitAndAtcRun(TestState unitTestState, TestState atcTestState,
			SourcecodeState stateAfterUnit, SourcecodeState stateAfterAtc) {
		PowerMockito.when(aUnitTestManager.executeAllPackages()).thenReturn(unitTestState);
		PowerMockito.when(atcTestManager.executeAllPackages()).thenReturn(atcTestState);
		featureProcessor.processEnabledFeatures();

		int expectedUpdateThemeCalls = stateAfterAtc == null ? 1 : 2;
		Mockito.verify(themeUpdateManager, Mockito.times(expectedUpdateThemeCalls))
				.updateTheme(any(SourcecodeState.class));

		if (stateAfterAtc == null || stateAfterUnit == stateAfterAtc) {
			Mockito.verify(themeUpdateManager, Mockito.times(expectedUpdateThemeCalls)).updateTheme(stateAfterUnit);
		} else {
			Mockito.verify(themeUpdateManager, Mockito.times(1)).updateTheme(stateAfterUnit);
			Mockito.verify(themeUpdateManager, Mockito.times(1)).updateTheme(stateAfterAtc);
		}
	}

	private void setMockedPreferences(String preference, boolean isActive) {
		PowerMockito.when(preferenceStore.getBoolean(preference)).thenReturn(isActive);
	}

}
