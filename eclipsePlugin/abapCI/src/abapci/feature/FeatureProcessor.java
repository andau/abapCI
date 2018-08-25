package abapci.feature;

import java.util.List;

import abapci.domain.SourcecodeState;
import abapci.domain.TestState;
import abapci.manager.AUnitTestManager;
import abapci.manager.AtcTestManager;
import abapci.manager.DevelopmentProcessManager;
import abapci.manager.JenkinsManager;
import abapci.manager.ThemeUpdateManager;
import abapci.presenter.ContinuousIntegrationPresenter;

public class FeatureProcessor {

	private AUnitTestManager aUnitTestManager;
	private JenkinsManager jenkinsManager;
	private AtcTestManager atcTestManager;

	private ThemeUpdateManager themeUpdateManager;
	private DevelopmentProcessManager developmentProcessManager;

	private FeatureFacade featureFacade;
	private ContinuousIntegrationPresenter presenter;

	public FeatureProcessor(ContinuousIntegrationPresenter presenter, String projectName,
			List<String> initialPackages) {

		this.presenter = presenter;
		aUnitTestManager = new AUnitTestManager(presenter, projectName, initialPackages);
		jenkinsManager = new JenkinsManager(presenter, projectName, initialPackages);
		atcTestManager = new AtcTestManager(presenter, projectName, initialPackages);

		themeUpdateManager = new ThemeUpdateManager();
		developmentProcessManager = new DevelopmentProcessManager();

		featureFacade = new FeatureFacade();

	}

	public void setPackages(List<String> packageNames) {
		aUnitTestManager.setPackages(packageNames);
		atcTestManager.setPackages(packageNames);
	}

	public void processEnabledFeatures() {

		try {
			if (featureFacade.getUnitFeature().isActive()) {
				SourcecodeState oldSourcecodeState = developmentProcessManager.getSourcecodeState();

				TestState unitTestState = aUnitTestManager.executeAllPackages(presenter.getCurrentProject(),
						presenter.getAbapPackageTestStatesForCurrentProject());
				developmentProcessManager.setUnitTeststate(unitTestState);
				themeUpdateManager.updateTheme(developmentProcessManager.getSourcecodeState());

				presenter.updateViewsAsync(developmentProcessManager.getSourcecodeState());

				if (featureFacade.getAtcFeature().isActive() && unitTestState == TestState.OK
						&& oldSourcecodeState != SourcecodeState.OK && oldSourcecodeState != SourcecodeState.ATC_FAIL) {
					TestState atcTestState = atcTestManager.executeAllPackages(presenter.getCurrentProject(),
							presenter.getAbapPackageTestStatesForCurrentProject());
					developmentProcessManager.setAtcTeststate(atcTestState);
					themeUpdateManager.updateTheme(developmentProcessManager.getSourcecodeState());
				}

				if (featureFacade.getJenkinsFeature().isActive() && unitTestState == TestState.OK
						&& oldSourcecodeState != SourcecodeState.OK && oldSourcecodeState != SourcecodeState.ATC_FAIL) {
					jenkinsManager.executeAllPackages();
				}

				presenter.updateViewsAsync(developmentProcessManager.getSourcecodeState());
			}
		} catch (Exception ex) {
			presenter.setStatusMessage("Testrun failed, exception: " + ex.getMessage());
		}

	}

}
