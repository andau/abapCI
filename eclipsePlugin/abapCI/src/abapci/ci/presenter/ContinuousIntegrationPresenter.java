package abapci.ci.presenter;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

import abapci.AbapCiPluginHelper;
import abapci.Exception.ContinuousIntegrationConfigFileParseException;
import abapci.ci.model.IContinuousIntegrationModel;
import abapci.ci.views.AbapCiDashboardView;
import abapci.ci.views.AbapCiMainView;
import abapci.coloredProject.colorChanger.StatusBarColorChanger;
import abapci.coloredProject.config.IColoringConfig;
import abapci.coloredProject.config.IColoringConfigFactory;
import abapci.coloredProject.config.TestStateColoringConfigFactory;
import abapci.coloredProject.general.IStatusBarWidget;
import abapci.coloredProject.general.WorkspaceColorConfiguration;
import abapci.coloredProject.model.ColoredProject;
import abapci.coloredProject.model.projectColor.DefaultEclipseProjectColor;
import abapci.coloredProject.model.projectColor.IProjectColor;
import abapci.coloredProject.model.projectColor.IProjectColorFactory;
import abapci.coloredProject.model.projectColor.ProjectColorFactory;
import abapci.domain.AbapPackageTestState;
import abapci.domain.ContinuousIntegrationConfig;
import abapci.domain.GlobalTestState;
import abapci.domain.InvalidItem;
import abapci.domain.SourcecodeState;
import abapci.domain.TestState;
import abapci.feature.FeatureFacade;
import abapci.feature.SourceCodeVisualisationFeature;
import abapci.feature.activeFeature.AtcFeature;
import abapci.feature.activeFeature.TddModeFeature;
import abapci.feature.activeFeature.UnitFeature;
import abapci.testResult.SourceCodeStateEvaluator;
import abapci.testResult.SourceCodeStateInfo;
import abapci.testResult.TestResult;
import abapci.testResult.TestResultConsolidator;
import abapci.testResult.TestResultSummary;
import abapci.testResult.TestResultType;
import abapci.testResult.visualizer.ITestResultVisualizer;
import abapci.testResult.visualizer.ResultVisualizerOutput;
import abapci.utils.EditorHandler;

public class ContinuousIntegrationPresenter {

	private static final int MAX_INVALID_ITEMS_OPENED = 10;
	private AbapCiMainView view;
	private final IContinuousIntegrationModel model;
	private IProject currentProject;
	private final List<AbapPackageTestState> abapPackageTestStates;
	private AbapCiDashboardView abapCiDashboardView;
	private final TestResultConsolidator testResultConsolidator;
	private final SourceCodeStateEvaluator sourceCodeStateEvaluator;
	private final SourceCodeStateInfo sourceCodeStateInfo;
	private StatusBarColorChanger statusBarColorChanger;
	private final AbapCiPluginHelper abapCiPluginHelper;

	private SourceCodeVisualisationFeature sourceCodeVisualisationFeature;
	private UnitFeature unitFeature;
	private TddModeFeature tddModeFeature;
	private AtcFeature atcFeature;

	public ContinuousIntegrationPresenter(AbapCiMainView abapCiMainView,
			IContinuousIntegrationModel continuousIntegrationModel, IProject currentProject) {

		view = abapCiMainView;
		model = continuousIntegrationModel;
		this.currentProject = currentProject;
		abapPackageTestStates = new ArrayList<>();

		testResultConsolidator = new TestResultConsolidator();
		sourceCodeStateEvaluator = new SourceCodeStateEvaluator();
		sourceCodeStateInfo = new SourceCodeStateInfo();
		new ProjectColorFactory();
		abapCiPluginHelper = new AbapCiPluginHelper();

		loadPackages();
		setViewerInput();

		initFeatures();
		registerPreferencePropertyChangeListener();

	}

	private void initFeatures() {

		final FeatureFacade featureFacade = new FeatureFacade();
		sourceCodeVisualisationFeature = featureFacade.getSourceCodeVisualisationFeature();
		unitFeature = featureFacade.getUnitFeature();
		tddModeFeature = featureFacade.getTddModeFeature();
		atcFeature = featureFacade.getAtcFeature();
	}

	private void registerPreferencePropertyChangeListener() {

		abapCiPluginHelper.getPreferenceStore().addPropertyChangeListener(event -> {
			initFeatures();
			updateViewsAsync();
		});

	}

	public void setView(AbapCiMainView abapCiMainView) {
		view = abapCiMainView;
	}

	public void removeContinousIntegrationConfig(ContinuousIntegrationConfig ciConfig) {
		try {
			model.remove(ciConfig);
			deleteAbapPackage(ciConfig);
		} catch (final ContinuousIntegrationConfigFileParseException e) {
			setStatusMessage("Parsing of xml file failed");
			e.printStackTrace();
		}
	}

	public void addContinousIntegrationConfig(ContinuousIntegrationConfig ciConfig) {
		try {
			if (ciConfig != null
					&& model.getAll().stream().anyMatch(item -> item.getPackageName().equals(ciConfig.getPackageName())
							&& item.getProjectName().equals(ciConfig.getProjectName()))) {
				model.remove(ciConfig);
			}

			model.add(ciConfig);
			addOrUpdateAbapPackage(ciConfig);
			updateViewsAsync();
		} catch (final ContinuousIntegrationConfigFileParseException e) {
			setStatusMessage("Parsing error when updating project",
					new Color(Display.getCurrent(), new RGB(255, 0, 0)));
		} catch (final Exception ex) {
			setStatusMessage(String.format("General error when updating project, errormessage %s", ex.getMessage()),
					new Color(Display.getCurrent(), new RGB(255, 0, 0)));
		}

	}

	private void addOrUpdateAbapPackage(ContinuousIntegrationConfig ciConfig) {
		boolean updated = false;

		final AbapPackageTestState packageForCiConfig = new AbapPackageTestState(ciConfig.getProjectName(),
				ciConfig.getPackageName(), "UNDEF", new TestResult(ciConfig.getUtActivated()),
				new TestResult(ciConfig.getAtcActivated()));

		final ListIterator<AbapPackageTestState> iterator = abapPackageTestStates.listIterator();
		while (iterator.hasNext()) {
			final AbapPackageTestState abapPackageTestState = iterator.next();
			if (abapPackageTestState.getProjectName().equals(ciConfig.getProjectName())
					&& abapPackageTestState.getPackageName().equals(ciConfig.getPackageName())) {
				iterator.set(packageForCiConfig);
				updated = true;
			}
		}

		if (updated == false) {
			abapPackageTestStates.add(packageForCiConfig);
		}

	}

	private void deleteAbapPackage(ContinuousIntegrationConfig ciConfig) {
		final ListIterator<AbapPackageTestState> iterator = abapPackageTestStates.listIterator();
		while (iterator.hasNext()) {
			final AbapPackageTestState abapPackageTestState = iterator.next();
			if (abapPackageTestState.getProjectName().equals(ciConfig.getProjectName())
					&& abapPackageTestState.getPackageName().equals(ciConfig.getPackageName())) {
				iterator.remove();
			}

		}
	}

	public void setViewerInput() {
		try {
			if (currentProject != null) {
				supplementAbapPackageTestStatesForProject(currentProject);
				if (view != null) {
					view.setViewerInput(getAbapPackageTestStatesForCurrentProject());
				}
			}
		} catch (final ContinuousIntegrationConfigFileParseException e) {
			setStatusMessage("Parsing of xml file failed");
			e.printStackTrace();
		}

	}

	public void loadPackages() {
		try {
			abapPackageTestStates.clear();
			List<ContinuousIntegrationConfig> ciConfigs;
			ciConfigs = model.getAll();
			for (final ContinuousIntegrationConfig ciConfig : ciConfigs) {
				abapPackageTestStates
						.add(new AbapPackageTestState(ciConfig.getProjectName(), ciConfig.getPackageName(), "UNDEF",
								new TestResult(ciConfig.getUtActivated()), new TestResult(ciConfig.getAtcActivated())));
			}
		} catch (final ContinuousIntegrationConfigFileParseException e) {
			setStatusMessage("Parsing of xml file failed");
		}

	}

	private void supplementAbapPackageTestStatesForProject(IProject project)
			throws ContinuousIntegrationConfigFileParseException {

		final List<ContinuousIntegrationConfig> ciConfigs = model.getAllForProjectAndGeneral(project.getName());
		for (final ContinuousIntegrationConfig ciConfig : ciConfigs) {
			if (!abapPackageTestStates.stream().anyMatch(item -> item.getPackageName().equals(ciConfig.getPackageName())
					&& item.getProjectName().equals(ciConfig.getProjectName()))) {
				abapPackageTestStates
						.add(new AbapPackageTestState(ciConfig.getProjectName(), ciConfig.getPackageName()));
			}
		}

	}

	public void updatePackageTestStates(List<AbapPackageTestState> updateAbapPackageTestStates) {
		for (AbapPackageTestState packageTestState : abapPackageTestStates) {
			for (final AbapPackageTestState updatedPackageTestState : updateAbapPackageTestStates) {
				if (updatedPackageTestState.getProjectName().equals(packageTestState.getProjectName())
						&& updatedPackageTestState.getPackageName().equals(packageTestState.getPackageName())) {
					packageTestState = updatedPackageTestState;
				}
			}
		}

		updateViewsAsync();
	}

	public List<AbapPackageTestState> getAbapPackageTestStatesForCurrentProject() {
		final List<AbapPackageTestState> abapPackageTestStatesForCurrentProject = new ArrayList<>();
		for (final AbapPackageTestState abapPackageTestState : abapPackageTestStates) {
			if (currentProject == null || currentProject.getName().equals(abapPackageTestState.getProjectName())) {
				abapPackageTestStatesForCurrentProject.add(abapPackageTestState);
			}
		}
		return abapPackageTestStatesForCurrentProject;
	}

	public void updateViewsAsync() {
		updateViewsAsync(evalSourceCodeTestState());
	}

	public void updateViewsAsync(SourcecodeState sourcecodeState) {
		final Runnable task = () -> updateViews();
		Display.getDefault().asyncExec(task);
	}

	private void updateViews() {

		final List<AbapPackageTestState> abapPackageTestStatesForCurrentProject = getAbapPackageTestStatesForCurrentProject();

		SourcecodeState currentSourceCodeState = evalSourceCodeTestState();
		if (currentSourceCodeState.equals(SourcecodeState.OK) && (sourceCodeStateInfo.nextPlannedStepIsRefactorStep()
				|| sourceCodeStateInfo.refactorStepIsStillSuggested(tddModeFeature.getMinimumRequiredSeconds()))) {
			currentSourceCodeState = SourcecodeState.ATC_FAIL;
		}
		final GlobalTestState globalTestState = new GlobalTestState(currentSourceCodeState);
		final String globalTestStateString = globalTestState.getTestStateOutputForDashboard();

		sourceCodeStateInfo.setSourceCodeState(globalTestStateString);

		try {

			final IProjectColorFactory projectColorFactory = new ProjectColorFactory();
			IProjectColor projectColor;

			if (globalTestState.getColor() == null) {
				projectColor = new DefaultEclipseProjectColor();
			} else {
				final Color currentColor = globalTestState.getColor();
				projectColor = projectColorFactory.create(currentColor.getRGB());
			}

			final IColoringConfigFactory coloringConfigFatory = new TestStateColoringConfigFactory(
					sourceCodeVisualisationFeature);
			final ColoredProject coloredProject = new ColoredProject(currentProject.getName(), projectColor, false);
			final IColoringConfig testStateConfig = coloringConfigFatory.create(coloredProject);

			final WorkspaceColorConfiguration workspaceColorConfiguration = abapCiPluginHelper
					.getWorkspaceColorConfiguration();

			final ResultVisualizerOutput resultVisualizerOutput = new ResultVisualizerOutput();
			resultVisualizerOutput.setGlobalTestState(globalTestStateString);
			resultVisualizerOutput.setAbapPackageTestStates(abapPackageTestStatesForCurrentProject);
			resultVisualizerOutput.setBackgroundColor(workspaceColorConfiguration.getColoring(currentProject)
					.getStatusWidgetBackgroundColor().getColor());
			resultVisualizerOutput.setCurrentProject(currentProject);
			resultVisualizerOutput.setShowAtcInfo(atcFeature.isActive());

			workspaceColorConfiguration.addOrUpdateTestStateColoring(testStateConfig,
					resultVisualizerOutput.getInfoline());

			final IStatusBarWidget statusBarWidget = abapCiPluginHelper.getStatusBarWidget();

			if (sourceCodeVisualisationFeature.isShowStatusBarWidgetEnabled()) {
				statusBarWidget.setVisible(true);
				final ITestResultVisualizer statusBarWidgetVisualizer = statusBarWidget.getTestResultVisualizer();
				statusBarWidgetVisualizer.setResultVisualizerOutput(resultVisualizerOutput);
			} else {
				statusBarWidget.setVisible(false);
			}

			if (abapCiDashboardView != null) {
				final ITestResultVisualizer testResultVisualizer = abapCiDashboardView.getTestResultVisualizer();

				testResultVisualizer.setResultVisualizerOutput(resultVisualizerOutput);
			}

			if (sourceCodeVisualisationFeature.isChangeStatusBarBackgroundColorEnabled()) {

				statusBarColorChanger = new StatusBarColorChanger(Display.getCurrent().getActiveShell(),
						projectColorFactory.create(workspaceColorConfiguration.getColoring(currentProject)
								.getStatusBarColor().getColor()));
				statusBarColorChanger.change();
			}

			if (view != null && abapPackageTestStatesForCurrentProject != null) {
				view.setViewerInput(abapPackageTestStatesForCurrentProject);
				view.statusLabel.setText("CI run package summary updated");
			}

		} catch (final Exception ex) {
			// ABAP CI dashoard UI state update failed, lets go on
			// typically happens if CI Dashboard was closed during runtime
		}

	}

	public void openEditorsForFailedItems() {
		if (evalSourceCodeTestState().equals(SourcecodeState.ATC_FAIL)) {
			openEditorsForFailedAtc();
		} else {
			openEditorsForFailedTests();
		}
	}

	private void openEditorsForFailedTests() {

		final List<InvalidItem> invalidItems = getAbapPackageTestStatesForCurrentProject().stream()
				.flatMap(item -> item.getFirstUnitTestErrors().stream()).limit(MAX_INVALID_ITEMS_OPENED)
				.collect(Collectors.<InvalidItem>toList());

		EditorHandler.openInvalidItem(currentProject, invalidItems);
	}

	private void openEditorsForFailedAtc() {

		final List<InvalidItem> invalidItems = getAbapPackageTestStatesForCurrentProject().stream()
				.flatMap(item -> item.getFirstFailedAtcErrors().stream()).limit(MAX_INVALID_ITEMS_OPENED)
				.collect(Collectors.<InvalidItem>toList());

		EditorHandler.openInvalidItem(currentProject, invalidItems);

	}

	private SourcecodeState evalSourceCodeTestState() {
		return sourceCodeStateEvaluator.evaluate(getAbapPackageTestStatesForCurrentProject(), unitFeature);
	}

	public IProject getCurrentProject() {
		return currentProject;
	}

	public void setCurrentProject(IProject project) {
		currentProject = project;
	}

	public void setStatusMessage(String message) {
		setStatusMessage(message, new Color(Display.getCurrent(), new RGB(0, 0, 0)));
	}

	public void setStatusMessage(String message, Color color) {
		final Runnable task = () -> setStatusMessageInternal(message, color);
		Display.getDefault().asyncExec(task);
	}

	private void setStatusMessageInternal(String message, Color color) {

		try {
			view.statusLabel.setText(message);
			view.statusLabel.setForeground(color);
		} catch (final Exception ex) {
			// if the status message can not be set we will not stop working as this is not
			// critical
			ex.printStackTrace();

		}
	}

	public void registerDashboardView(AbapCiDashboardView abapCiDashboardView) {
		this.abapCiDashboardView = abapCiDashboardView;
	}

	public boolean containsPackage(String currentProjectName, String triggerPackage) {
		return abapPackageTestStates.stream().anyMatch(item -> item.getProjectName() != null
				&& item.getProjectName().equals(currentProjectName) && item.getPackageName().equals(triggerPackage));
	}

	public boolean runNecessary() {
		return !unitFeature.isRunActivatedObjectsOnly() && getAbapPackageTestStatesForCurrentProject().stream()
				.anyMatch(item -> item.getUnitTestState() == TestState.UNDEF
						|| item.getUnitTestState() == TestState.OFFL);
	}

	public void mergeUnitTestResultSummary(TestResultSummary unitTestResultSummary) {

		for (final AbapPackageTestState testState : getAbapPackageTestStatesForCurrentProject()) {
			if (testState.getPackageName().equals(unitTestResultSummary.getPackageName())) {
				testState.setUnitTestResult(unitTestResultSummary.getTestResult());
			}
		}

		updateViewsAsync();
	}

	public void mergeAtcWorklist(TestResultSummary atcTestResultSummary) {
		testResultConsolidator.computeConsolidatedTestResult(atcTestResultSummary,
				getAbapPackageTestStatesForCurrentProject(), TestResultType.ATC);

		updateViewsAsync();
	}

	public void mergeUnitTestResult(TestResultSummary unitTestResultSummary) {
		testResultConsolidator.computeConsolidatedTestResult(unitTestResultSummary,
				getAbapPackageTestStatesForCurrentProject(), TestResultType.UNIT);

		updateViewsAsync();
	}

	public AtcFeature getAtcFeature() {
		return atcFeature;
	}

	public UnitFeature getUnitFeature() {
		return unitFeature;
	}

	public SourcecodeState getSourcecodeState() {
		return evalSourceCodeTestState();
	}
}
