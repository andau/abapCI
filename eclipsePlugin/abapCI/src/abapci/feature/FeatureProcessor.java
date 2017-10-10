package abapci.feature;

import abapci.domain.TestState;
import abapci.manager.AUnitTestManager;
import abapci.manager.AtcTestManager;
import abapci.manager.DevelopmentProcessManager;
import abapci.manager.JenkinsManager;
import abapci.manager.ThemeUpdateManager;

public class FeatureProcessor {
	
	private AUnitTestManager aUnitTestManager;
	private JenkinsManager jenkinsManager;
	private AtcTestManager atcTestManager;
	
	private ThemeUpdateManager themeUpdateManager; 
	private DevelopmentProcessManager developmentProcessManager;  

	private FeatureFacade featureFacade; 

	public FeatureProcessor() 
	{
		aUnitTestManager = new AUnitTestManager();
		jenkinsManager = new JenkinsManager();
		atcTestManager = new AtcTestManager();
		
		themeUpdateManager = new ThemeUpdateManager(); 
		developmentProcessManager = new DevelopmentProcessManager(); 

		featureFacade = new FeatureFacade(); 

	}
	
	public void processEnabledFeatures() {
		
		if (featureFacade.getUnitFeature().isActive()) 
		{
			TestState unitTestState = aUnitTestManager.executeAllPackages();
			developmentProcessManager.setUnitTeststate(unitTestState); 
			themeUpdateManager.updateTheme(developmentProcessManager.getSourcecodeState());

			if (unitTestState == TestState.OK && featureFacade.getAtcFeature().isActive()) {
				TestState atcTestState = atcTestManager.executeAllPackages();
				developmentProcessManager.setAtcTeststate(atcTestState); 
			    themeUpdateManager.updateTheme(developmentProcessManager.getSourcecodeState());
			}

			if (unitTestState == TestState.OK && featureFacade.getJenkinsFeature().isActive()) {
				jenkinsManager.executeAllPackages();
			}
		}
	}

}
