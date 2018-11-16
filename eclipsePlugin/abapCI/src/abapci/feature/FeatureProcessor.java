package abapci.feature;

import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

import abapci.AbapCiPluginHelper;
import abapci.activation.Activation;
import abapci.ci.presenter.ContinuousIntegrationPresenter;
import abapci.feature.activeFeature.AtcFeature;
import abapci.feature.activeFeature.DeveloperFeature;
import abapci.feature.activeFeature.UnitFeature;
import abapci.manager.AtcTestManager;
import abapci.manager.IAtcTestManager;
import abapci.manager.JavaSimAtcTestManager;
import abapci.manager.ThemeUpdateManager;
import abapci.manager.UnitTestManager;

public class FeatureProcessor {

	private final UnitTestManager aUnitTestManager;
	private IAtcTestManager atcTestManager;

	private final ThemeUpdateManager themeUpdateManager;

	private final ContinuousIntegrationPresenter presenter;
	private List<Activation> inactiveObjects;
	private UnitFeature unitFeature;
	private AtcFeature atcFeature;
	private SourceCodeVisualisationFeature sourceCodeVisualisationFeature;
	private DeveloperFeature developerFeature;

	public FeatureProcessor(ContinuousIntegrationPresenter presenter, IProject project, List<String> initialPackages) {

		this.presenter = presenter;

		aUnitTestManager = new UnitTestManager(presenter, project, initialPackages);

		themeUpdateManager = new ThemeUpdateManager();

		initFeatures();

		registerPreferencePropertyChangeListener();

		if (developerFeature.isJavaSimuModeEnabled()) {
			atcTestManager = new JavaSimAtcTestManager(presenter, project, initialPackages);
		} else {
			atcTestManager = new AtcTestManager(presenter, project, initialPackages);
		}
	}

	private void initFeatures() {

		final FeatureFacade featureFacade = new FeatureFacade();
		unitFeature = featureFacade.getUnitFeature();
		atcFeature = featureFacade.getAtcFeature();
		sourceCodeVisualisationFeature = featureFacade.getSourceCodeVisualisationFeature();
		developerFeature = featureFacade.getDeveloperFeature();

	}

	private void registerPreferencePropertyChangeListener() {

		final AbapCiPluginHelper abapCiPluginHelper = new AbapCiPluginHelper();
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

				if (unitFeature.isRunActivatedObjectsOnly()) {
					if (inactiveObjects != null) {
						aUnitTestManager.executeAllPackages(presenter.getCurrentProject(),
								presenter.getAbapPackageTestStatesForCurrentProject(), inactiveObjects);
					}
				} else {
					aUnitTestManager.executeAllPackages(presenter.getCurrentProject(),
							presenter.getAbapPackageTestStatesForCurrentProject(), null);
				}

				if (sourceCodeVisualisationFeature.isThemeUpdateEnabled()) {
					themeUpdateManager.updateTheme(presenter.getSourcecodeState());
				}

			}

			if (atcFeature.isActive()) {
				if (atcFeature.isRunActivatedObjects() && inactiveObjects != null) {
					atcTestManager.executeAllPackages(presenter.getCurrentProject(),
							presenter.getAbapPackageTestStatesForCurrentProject(), inactiveObjects);
				}
			}

			if (sourceCodeVisualisationFeature.isThemeUpdateEnabled()) {
				themeUpdateManager.updateTheme(presenter.getSourcecodeState());
			}

		} catch (final Exception ex) {
			presenter.setStatusMessage("Testrun failed, exception: " + ex.getMessage(),
					new Color(Display.getCurrent(), new RGB(255, 0, 0)));
		}

	}

}
