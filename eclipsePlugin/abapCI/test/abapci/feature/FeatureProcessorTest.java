package abapci.feature;

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
	public void featureProcessorUnitRunTest() throws Exception {
		
		PowerMockito.when(preferenceStore.getBoolean(PreferenceConstants.PREF_UNIT_RUN_ON_SAVE)).thenReturn(true);

		PowerMockito.when(aUnitTestManager.executeAllPackages()).thenReturn(TestState.OK); 
		featureProcessor.processEnabledFeatures();
		Mockito.verify(themeUpdateManager, Mockito.times(1)).updateTheme(SourcecodeState.CLEAN);
		
		PowerMockito.when(aUnitTestManager.executeAllPackages()).thenReturn(TestState.NOK); 
		featureProcessor.processEnabledFeatures();
		Mockito.verify(themeUpdateManager, Mockito.times(1)).updateTheme(SourcecodeState.UT_FAIL);

		PowerMockito.when(aUnitTestManager.executeAllPackages()).thenReturn(TestState.UNDEF); 
		featureProcessor.processEnabledFeatures();
		Mockito.verify(themeUpdateManager, Mockito.times(1)).updateTheme(SourcecodeState.UNDEF);
	}

	@Test
	public void featureProcessorUnitAndAtcRunTest() throws Exception {
		
		PowerMockito.when(preferenceStore.getBoolean(PreferenceConstants.PREF_UNIT_RUN_ON_SAVE)).thenReturn(true);
		PowerMockito.when(preferenceStore.getBoolean(PreferenceConstants.PREF_ATC_RUN_AFTER_UNIT_TESTS_TURN_GREEN)).thenReturn(true);

		PowerMockito.when(aUnitTestManager.executeAllPackages()).thenReturn(TestState.OK); 
		PowerMockito.when(atcTestManager.executeAllPackages()).thenReturn(TestState.NOK); 
		featureProcessor.processEnabledFeatures();
		Mockito.verify(themeUpdateManager, Mockito.times(1)).updateTheme(SourcecodeState.CLEAN);
		Mockito.verify(themeUpdateManager, Mockito.times(1)).updateTheme(SourcecodeState.ATC_FAIL);

		PowerMockito.when(aUnitTestManager.executeAllPackages()).thenReturn(TestState.NOK); 
		PowerMockito.when(atcTestManager.executeAllPackages()).thenReturn(TestState.OK); 
		featureProcessor.processEnabledFeatures();
		Mockito.verify(themeUpdateManager, Mockito.times(1)).updateTheme(SourcecodeState.UT_FAIL);

	}

}
