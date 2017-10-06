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

	FeatureDecision featureDecision; 

	public FeatureProcessor() 
	{
		aUnitTestManager = new AUnitTestManager();
		jenkinsManager = new JenkinsManager();
		atcTestManager = new AtcTestManager();
		
		themeUpdateManager = new ThemeUpdateManager(); 
		developmentProcessManager = new DevelopmentProcessManager(); 

		featureDecision = new FeatureDecision(); 

	}
	
	public void processEnabledFeatures() {
		
		if (featureDecision.runUnitTestsOnSave()) 
		{
			TestState unitTestState = aUnitTestManager.executeAllPackages();
			developmentProcessManager.setUnitTeststate(unitTestState); 
			themeUpdateManager.updateTheme(developmentProcessManager.getSourcecodeState());

			if (unitTestState == TestState.OK && featureDecision.runAtcAfterUnitTestTurnOk()) {
				TestState atcTestState = atcTestManager.executeAllPackages();
				developmentProcessManager.setAtcTeststate(atcTestState); 
			    themeUpdateManager.updateTheme(developmentProcessManager.getSourcecodeState());
			}

			if (unitTestState == TestState.OK && featureDecision.runJenkinsAfterUnitTestTurnOk()) {
				jenkinsManager.executeAllPackages();
			}
		}
	}

}
