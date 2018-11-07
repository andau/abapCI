package abapci.feature;

import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

import abapci.AbapCiPluginHelper;
import abapci.activation.Activation;
import abapci.domain.TestState;
import abapci.feature.activeFeature.AtcFeature;
import abapci.feature.activeFeature.ThemeColorChangerFeature;
import abapci.feature.activeFeature.UnitFeature;
import abapci.manager.DevelopmentProcessManager;
import abapci.manager.IAtcTestManager;
import abapci.manager.JavaSimAtcTestManager;
import abapci.manager.ThemeUpdateManager;
import abapci.manager.UnitTestManager;
import abapci.presenter.ContinuousIntegrationPresenter;

public class FeatureProcessor {

	private final UnitTestManager aUnitTestManager;
	private final IAtcTestManager atcTestManager;

	private final ThemeUpdateManager themeUpdateManager;

	private final ContinuousIntegrationPresenter presenter;
	private List<Activation> inactiveObjects;
	private final DevelopmentProcessManager developmentProcessManager;
	private UnitFeature unitFeature;
	private AtcFeature atcFeature;
	private ThemeColorChangerFeature themeColorChangerFeature;

	public FeatureProcessor(ContinuousIntegrationPresenter presenter, IProject project, List<String> initialPackages) {

		this.presenter = presenter;

		aUnitTestManager = new UnitTestManager(presenter, project, initialPackages);
		// For testing purposes
		atcTestManager = new JavaSimAtcTestManager(presenter, project, initialPackages);
		// atcTestManager = new AtcTestManager(presenter, project, initialPackages);

		developmentProcessManager = new DevelopmentProcessManager();

		themeUpdateManager = new ThemeUpdateManager();

		initFeatures();

		registerPreferencePropertyChangeListener();
	}

	private void initFeatures() {

		FeatureFacade featureFacade = new FeatureFacade();
		unitFeature = featureFacade.getUnitFeature();
		atcFeature = featureFacade.getAtcFeature();
		themeColorChangerFeature = featureFacade.getColorChangerFeature();

	}

	private void registerPreferencePropertyChangeListener() {

		AbapCiPluginHelper abapCiPluginHelper = new AbapCiPluginHelper();
		abapCiPluginHelper.getPreferenceStore().addPropertyChangeListener(event -> {
			initFeatures();
		});

	}

	public void setPackagesAndObjects(List<String> packageNames, List<Activation> inactiveObjects) {
		aUnitTestManager.setPackages(packageNames);
		atcTestManager.setPackages(packageNames);
		this.inactiveObjects = inactiveObjects;
	}

	public void processEnabledFeatures() {

		try {
			if (unitFeature.isActive()) {

				TestState unitTestState = TestState.UNDEF;
				if (unitFeature.isRunActivatedObjectsOnly()) {
					if (inactiveObjects != null) {
						unitTestState = aUnitTestManager.executeAllPackages(presenter.getCurrentProject(),
								presenter.getAbapPackageTestStatesForCurrentProject(), inactiveObjects);
					}
				} else {
					unitTestState = aUnitTestManager.executeAllPackages(presenter.getCurrentProject(),
							presenter.getAbapPackageTestStatesForCurrentProject(), null);
				}

				developmentProcessManager.setUnitTeststate(unitTestState);

				if (themeColorChangerFeature.isActive()) {
					themeUpdateManager.updateTheme(developmentProcessManager.getSourcecodeState());
				}

			}

			if (atcFeature.isActive()) {
				TestState atcTestState = null;
				if (atcFeature.isRunActivatedObjects() && inactiveObjects != null) {

					atcTestState = atcTestManager.executeAllPackages(presenter.getCurrentProject(),
							presenter.getAbapPackageTestStatesForCurrentProject(), inactiveObjects);
				}

				if (atcTestState != null) {
					developmentProcessManager.setAtcTeststate(atcTestState);
					themeUpdateManager.updateTheme(developmentProcessManager.getSourcecodeState());
				}
			}

			presenter.updateViewsAsync(developmentProcessManager.getSourcecodeState());

		} catch (Exception ex) {
			presenter.setStatusMessage("Testrun failed, exception: " + ex.getMessage(),
					new Color(Display.getCurrent(), new RGB(255, 0, 0)));
		}

	}

}
