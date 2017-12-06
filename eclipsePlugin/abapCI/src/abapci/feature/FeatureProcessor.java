package abapci.feature;

import java.util.List;

import abapci.domain.SourcecodeState;
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
	
	public FeatureProcessor(List<String> initialPackages) {
		
		aUnitTestManager = new AUnitTestManager(initialPackages);
		jenkinsManager = new JenkinsManager(initialPackages);
		atcTestManager = new AtcTestManager(initialPackages);

		themeUpdateManager = new ThemeUpdateManager();
		developmentProcessManager = new DevelopmentProcessManager();

		featureFacade = new FeatureFacade();

	}
	
	public void setPackages(List<String> packageNames) 
	{
		aUnitTestManager.setPackages(packageNames);
		atcTestManager.setPackages(packageNames);
	}

	public void processEnabledFeatures() {

		if (featureFacade.getUnitFeature().isActive()) {
			SourcecodeState oldSourcecodeState = developmentProcessManager.getSourcecodeState();

			TestState unitTestState = aUnitTestManager.executeAllPackages();
			developmentProcessManager.setUnitTeststate(unitTestState);
			themeUpdateManager.updateTheme(developmentProcessManager.getSourcecodeState());

			if (featureFacade.getAtcFeature().isActive() && unitTestState == TestState.OK
					&& oldSourcecodeState != SourcecodeState.OK && oldSourcecodeState != SourcecodeState.ATC_FAIL) {
				TestState atcTestState = atcTestManager.executeAllPackages();
				developmentProcessManager.setAtcTeststate(atcTestState);
				themeUpdateManager.updateTheme(developmentProcessManager.getSourcecodeState());
			}

			if (featureFacade.getJenkinsFeature().isActive() && unitTestState == TestState.OK
					&& oldSourcecodeState != SourcecodeState.OK && oldSourcecodeState != SourcecodeState.ATC_FAIL) {
				jenkinsManager.executeAllPackages();
			}
		}
	}

}
