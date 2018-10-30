package abapci.feature;

import java.util.ArrayList;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.preference.IPreferenceStore;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
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
import abapci.presenter.ContinuousIntegrationPresenter;

@RunWith(PowerMockRunner.class)
@PrepareForTest(AbapCiPlugin.class)
public class FeatureProcessorTest {

	FeatureProcessor featureProcessor;

	IPreferenceStore preferenceStore;
	FeatureFacade featureFacade;
	FeatureFactory featureFactory;
	AUnitTestManager aUnitTestManager;
	AtcTestManager atcTestManager;
	AbapCiPlugin abapCiPlugin;
	ThemeUpdateManager themeUpdateManager;
	ContinuousIntegrationPresenter continuousIntegrationPresenter;
	IProject project;

	@Before
	public void before() throws Exception {

		preferenceStore = Mockito.mock(IPreferenceStore.class);
		featureFacade = Mockito.mock(FeatureFacade.class);
		featureFactory= Mockito.mock(FeatureFactory.class);
		aUnitTestManager= Mockito.mock(AUnitTestManager.class);
		atcTestManager= Mockito.mock(AtcTestManager.class);
		abapCiPlugin= Mockito.mock(AbapCiPlugin.class);
		themeUpdateManager= Mockito.mock(ThemeUpdateManager.class);
		continuousIntegrationPresenter= Mockito.mock(ContinuousIntegrationPresenter.class);
		project = Mockito.mock(IProject.class);

		
		featureFacade = new FeatureFacade();
		featureFactory = new FeatureFactory();
		Whitebox.setInternalState(featureFactory, "prefs", preferenceStore);
		Whitebox.setInternalState(featureFacade, "featureFactory", featureFactory);

		ContinuousIntegrationPresenter presenter = new ContinuousIntegrationPresenter(null,
				new ContinuousIntegrationTestModel(), null);
		featureProcessor = new FeatureProcessor(presenter, project, new ArrayList<String>());
		Whitebox.setInternalState(featureProcessor, "featureFacade", featureFacade);
		Whitebox.setInternalState(featureProcessor, "aUnitTestManager", aUnitTestManager);
		Whitebox.setInternalState(featureProcessor, "atcTestManager", atcTestManager);
		Whitebox.setInternalState(featureProcessor, "themeUpdateManager", themeUpdateManager);
		Whitebox.setInternalState(featureProcessor, "presenter", continuousIntegrationPresenter);

	}

	@Test
	public void unitOkTest() throws Exception {

		setMockedPreferences(PreferenceConstants.PREF_UNIT_RUN_ON_SAVE, true);

		verifyUpdateThemeCallAfterUnitRun(TestState.OK, SourcecodeState.OK);
	}

	@Test
	public void unitNokTest() throws Exception {

		setMockedPreferences(PreferenceConstants.PREF_UNIT_RUN_ON_SAVE, true);

		verifyUpdateThemeCallAfterUnitRun(TestState.NOK, SourcecodeState.UT_FAIL);
	}

	@Test
	public void unitUndefTest() throws Exception {

		setMockedPreferences(PreferenceConstants.PREF_UNIT_RUN_ON_SAVE, true);

		verifyUpdateThemeCallAfterUnitRun(TestState.UNDEF, SourcecodeState.UNDEF);

	}

	@Test
	public void unitOfflTest() throws Exception {

		setMockedPreferences(PreferenceConstants.PREF_UNIT_RUN_ON_SAVE, true);

		verifyUpdateThemeCallAfterUnitRun(TestState.OFFL, SourcecodeState.OFFL);
	}

	@Test
	public void unitOkAndAtcNokTest() throws Exception {
		setMockedPreferences(PreferenceConstants.PREF_UNIT_RUN_ON_SAVE, true);

		callFeatureProcessor(TestState.OK, TestState.NOK, SourcecodeState.OK, SourcecodeState.ATC_FAIL);
	}

	@Test
	public void unitOkAndAtcOkTest() throws Exception {
		setMockedPreferences(PreferenceConstants.PREF_UNIT_RUN_ON_SAVE, true);

		callFeatureProcessor(TestState.OK, TestState.OK, SourcecodeState.OK, SourcecodeState.OK);

	}

	@Test
	public void unitNokAndAtcOkTest() throws Exception {
		setMockedPreferences(PreferenceConstants.PREF_UNIT_RUN_ON_SAVE, true);

		callFeatureProcessor(TestState.NOK, TestState.OK, SourcecodeState.UT_FAIL, null);

	}

	@Test
	public void unitNokAndAtcNokTest() throws Exception {
		setMockedPreferences(PreferenceConstants.PREF_UNIT_RUN_ON_SAVE, true);
		callFeatureProcessor(TestState.NOK, TestState.NOK, SourcecodeState.UT_FAIL, null);

	}

	private void verifyUpdateThemeCallAfterUnitRun(TestState testState, SourcecodeState sourcecodeState) {
		PowerMockito.when(aUnitTestManager.executeAllPackages(null, null, null)).thenReturn(testState);
		featureProcessor.processEnabledFeatures();
	}

	private void callFeatureProcessor(TestState unitTestState, TestState atcTestState, SourcecodeState stateAfterUnit,
			SourcecodeState stateAfterAtc) {
		PowerMockito.when(aUnitTestManager.executeAllPackages(null, null, null)).thenReturn(unitTestState);
		PowerMockito.when(atcTestManager.executeAllPackages(null, null, null)).thenReturn(atcTestState);
		featureProcessor.processEnabledFeatures();

	}

	private void setMockedPreferences(String preference, boolean isActive) {
		PowerMockito.when(preferenceStore.getBoolean(preference)).thenReturn(isActive);
	}

}
