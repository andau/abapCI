package abapci.presenter;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.forms.widgets.Hyperlink;

import abapci.AbapCiPluginHelper;
import abapci.Exception.AbapCiColoredProjectFileParseException;
import abapci.Exception.ContinuousIntegrationConfigFileParseException;
import abapci.coloredProject.colorChanger.StatusBarColorChanger;
import abapci.coloredProject.general.IStatusBarWidget;
import abapci.coloredProject.general.WorkspaceColorProxySingleton;
import abapci.coloredProject.model.ColoredProject;
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
import abapci.model.IContinuousIntegrationModel;
import abapci.testResult.SourceCodeStateEvaluator;
import abapci.testResult.SourceCodeStateInfo;
import abapci.testResult.TestResult;
import abapci.testResult.TestResultConsolidator;
import abapci.testResult.TestResultSummary;
import abapci.testResult.TestResultType;
import abapci.testResult.visualizer.ITestResultVisualizer;
import abapci.testResult.visualizer.ResultVisualizerOutput;
import abapci.utils.EditorHandler;
import abapci.utils.InvalidItemUtil;
import abapci.views.AbapCiDashboardView;
import abapci.views.AbapCiMainView;

public class ContinuousIntegrationPresenter {

	private AbapCiMainView view;
	private IContinuousIntegrationModel model;
	private IProject currentProject;
	private List<AbapPackageTestState> abapPackageTestStates;
	private AbapCiDashboardView abapCiDashboardView;
	private FeatureFacade featureFacade;
	private TestResultConsolidator testResultConsolidator;
	private SourceCodeStateEvaluator sourceCodeStateEvaluator;
	private SourceCodeStateInfo sourceCodeStateInfo;
	private StatusBarColorChanger statusBarColorChanger;
	private IProjectColorFactory projectColorFactory;
	private AbapCiPluginHelper abapCiPluginHelper;

	public ContinuousIntegrationPresenter(AbapCiMainView abapCiMainView,
			IContinuousIntegrationModel continuousIntegrationModel, IProject currentProject) {
		this.view = abapCiMainView;
		this.model = continuousIntegrationModel;
		this.currentProject = currentProject;
		this.abapPackageTestStates = new ArrayList<AbapPackageTestState>();
		featureFacade = new FeatureFacade();
		testResultConsolidator = new TestResultConsolidator();
		sourceCodeStateEvaluator = new SourceCodeStateEvaluator();
		sourceCodeStateInfo = new SourceCodeStateInfo();
		projectColorFactory = new ProjectColorFactory();
		abapCiPluginHelper = new AbapCiPluginHelper();

		loadPackages();
		setViewerInput();
	}

	public void setView(AbapCiMainView abapCiMainView) {
		this.view = abapCiMainView;
	}

	public void removeContinousIntegrationConfig(ContinuousIntegrationConfig ciConfig) {
		try {
			model.remove(ciConfig);
			deleteAbapPackage(ciConfig);
		} catch (ContinuousIntegrationConfigFileParseException e) {
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
		} catch (ContinuousIntegrationConfigFileParseException e) {
			setStatusMessage("Parsing error when updating project",
					new Color(Display.getCurrent(), new RGB(255, 0, 0)));
		} catch (Exception ex) {
			setStatusMessage(String.format("General error when updating project, errormessage %s", ex.getMessage()),
					new Color(Display.getCurrent(), new RGB(255, 0, 0)));
		}

	}

	private void addOrUpdateAbapPackage(ContinuousIntegrationConfig ciConfig) {
		boolean updated = false;

		AbapPackageTestState packageForCiConfig = new AbapPackageTestState(ciConfig.getProjectName(),
				ciConfig.getPackageName(), "UNDEF", new TestResult(ciConfig.getUtActivated()),
				new TestResult(ciConfig.getAtcActivated()));

		ListIterator<AbapPackageTestState> iterator = abapPackageTestStates.listIterator();
		while (iterator.hasNext()) {
			AbapPackageTestState abapPackageTestState = iterator.next();
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
		ListIterator<AbapPackageTestState> iterator = abapPackageTestStates.listIterator();
		while (iterator.hasNext()) {
			AbapPackageTestState abapPackageTestState = iterator.next();
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
		} catch (ContinuousIntegrationConfigFileParseException e) {
			setStatusMessage("Parsing of xml file failed");
			e.printStackTrace();
		}

	}

	public void loadPackages() {
		try {
			abapPackageTestStates.clear();
			List<ContinuousIntegrationConfig> ciConfigs;
			ciConfigs = model.getAll();
			for (ContinuousIntegrationConfig ciConfig : ciConfigs) {
				abapPackageTestStates
						.add(new AbapPackageTestState(ciConfig.getProjectName(), ciConfig.getPackageName(), "UNDEF",
								new TestResult(ciConfig.getUtActivated()), new TestResult(ciConfig.getAtcActivated())));
			}
		} catch (ContinuousIntegrationConfigFileParseException e) {
			setStatusMessage("Parsing of xml file failed");
		}

	}

	private void supplementAbapPackageTestStatesForProject(IProject project)
			throws ContinuousIntegrationConfigFileParseException {
		List<ContinuousIntegrationConfig> ciConfigs = model.getAllForProjectAndGeneral(project.getName());
		for (ContinuousIntegrationConfig ciConfig : ciConfigs) {
			if (!abapPackageTestStates.stream().anyMatch(item -> item.getPackageName().equals(ciConfig.getPackageName())
					&& item.getProjectName().equals(ciConfig.getProjectName()))) {
				abapPackageTestStates
						.add(new AbapPackageTestState(ciConfig.getProjectName(), ciConfig.getPackageName()));
			}
		}
	}

	public void updatePackageTestStates(List<AbapPackageTestState> updateAbapPackageTestStates) {
		for (AbapPackageTestState packageTestState : abapPackageTestStates) {
			for (AbapPackageTestState updatedPackageTestState : updateAbapPackageTestStates) {
				if (updatedPackageTestState.getProjectName().equals(packageTestState.getProjectName())
						&& updatedPackageTestState.getPackageName().equals(packageTestState.getPackageName())) {
					packageTestState = updatedPackageTestState;
				}
			}
		}

		updateViewsAsync();
	}

	public List<AbapPackageTestState> getAbapPackageTestStatesForCurrentProject() {
		List<AbapPackageTestState> abapPackageTestStatesForCurrentProject = new ArrayList<>();
		for (AbapPackageTestState abapPackageTestState : abapPackageTestStates) {
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
		Runnable task = () -> updateViews();
		Display.getDefault().asyncExec(task);
	}

	private void updateViews() {

		List<AbapPackageTestState> abapPackageTestStatesForCurrentProject = getAbapPackageTestStatesForCurrentProject();

		SourcecodeState currentSourceCodeState = evalSourceCodeTestState();
		if (currentSourceCodeState.equals(SourcecodeState.OK)
				&& (sourceCodeStateInfo.nextPlannedStepIsRefactorStep() || sourceCodeStateInfo
						.refactorStepIsStillSuggested(featureFacade.getTddModeFeature().getMinimumRequiredSeconds()))) {
			currentSourceCodeState = SourcecodeState.ATC_FAIL;
		}
		GlobalTestState globalTestState = new GlobalTestState(currentSourceCodeState);
		String globalTestStateString = globalTestState.getTestStateOutputForDashboard();

		sourceCodeStateInfo.setSourceCodeState(globalTestStateString);

		try {

			ResultVisualizerOutput resultVisualizerOutput = new ResultVisualizerOutput();
			resultVisualizerOutput.setGlobalTestState(globalTestStateString); 
			resultVisualizerOutput.setAbapPackageTestStates(abapPackageTestStatesForCurrentProject); 
			resultVisualizerOutput.setBackgroundColor(globalTestState.getColor()); 
			resultVisualizerOutput.setCurrentProject(currentProject);
			resultVisualizerOutput.setShowAtcInfo(featureFacade.getAtcFeature().isActive());

			IStatusBarWidget statusBarWidget = abapCiPluginHelper.getStatusBarWidget(); 
			
			if (featureFacade.getSourceCodeVisualisationFeature().isShowStatusBarWidgetEnabled()) 
			{
				statusBarWidget.setVisible(true);
				ITestResultVisualizer statusBarWidgetVisualizer = statusBarWidget.getTestResultVisualizer();				
				statusBarWidgetVisualizer.setResultVisualizerOutput(resultVisualizerOutput);
			}
			else 
			{
				statusBarWidget.setVisible(false);
			}
			

			if (abapCiDashboardView != null) {
				ITestResultVisualizer testResultVisualizer = abapCiDashboardView.getTestResultVisualizer();
								
				testResultVisualizer.setResultVisualizerOutput(resultVisualizerOutput);
			}
			

			if (featureFacade.getSourceCodeVisualisationFeature().isChangeStatusBarBackgroundColorEnabled()) {

				statusBarColorChanger = new StatusBarColorChanger(Display.getCurrent().getActiveShell(),
						projectColorFactory.create(globalTestState.getColor()));
				statusBarColorChanger.change();

				Color currentColor = globalTestState.getColor();
				IProjectColorFactory projectColorFactory = new ProjectColorFactory();
				IProjectColor projectColor = projectColorFactory.create(currentColor.getRGB());
				WorkspaceColorProxySingleton.getInstance()
						.addOrUpdate(new ColoredProject(currentProject.getName(), projectColor));
			}

			if (view != null && abapPackageTestStatesForCurrentProject != null) {
				view.setViewerInput(abapPackageTestStatesForCurrentProject);
				view.statusLabel.setText("CI run package summary updated");
			}

		} catch (AbapCiColoredProjectFileParseException e) {
			view.statusLabel.setText("File with project coloring could not be loaded");
		} catch (Exception ex) {
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

		List<AbapPackageTestState> packagesWithFailedTests = getAbapPackageTestStatesForCurrentProject().stream()
				.filter(item -> item.getFirstFailedUnitTest() != null)
				.collect(Collectors.<AbapPackageTestState>toList());

		EditorHandler.openUnit(currentProject, packagesWithFailedTests);
	}

	private void openEditorsForFailedAtc() {

		List<AbapPackageTestState> packagesWithFailedAtc = getAbapPackageTestStatesForCurrentProject().stream()
				.filter(item -> item.getFirstFailedAtc() != null).collect(Collectors.<AbapPackageTestState>toList());

		EditorHandler.openAtc(currentProject, packagesWithFailedAtc);
	}


	private SourcecodeState evalSourceCodeTestState() {
		return sourceCodeStateEvaluator.evaluate(getAbapPackageTestStatesForCurrentProject());
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
		Runnable task = () -> setStatusMessageInternal(message, color);
		Display.getDefault().asyncExec(task);
	}

	private void setStatusMessageInternal(String message, Color color) {

		// TODO call async
		try {
			view.statusLabel.setText(message);
			view.statusLabel.setForeground(color);
		} catch (Exception ex) {
			// if the status message can not be set we will ignore this
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
		return !featureFacade.getUnitFeature().isRunActivatedObjectsOnly()
				&& getAbapPackageTestStatesForCurrentProject().stream()
						.anyMatch(item -> item.getUnitTestState() == TestState.UNDEF
								|| item.getUnitTestState() == TestState.OFFL);
	}

	public void mergeUnitTestResultSummary(TestResultSummary unitTestResultSummary) {

		for (AbapPackageTestState testState : getAbapPackageTestStatesForCurrentProject()) {
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
}
