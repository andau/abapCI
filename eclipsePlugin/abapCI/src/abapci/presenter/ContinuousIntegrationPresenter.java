package abapci.presenter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.forms.widgets.Hyperlink;

import abapci.Exception.ContinuousIntegrationConfigFileParseException;
import abapci.domain.AbapPackageTestState;
import abapci.domain.ContinuousIntegrationConfig;
import abapci.domain.GlobalTestState;
import abapci.domain.InvalidItem;
import abapci.domain.SourcecodeState;
import abapci.domain.TestState;
import abapci.feature.FeatureFacade;
import abapci.model.IContinuousIntegrationModel;
import abapci.result.SourceCodeStateEvaluator;
import abapci.result.SourceCodeStateInfo;
import abapci.result.TestResult;
import abapci.result.TestResultConsolidator;
import abapci.result.TestResultSummary;
import abapci.result.TestResultType;
import abapci.utils.BackgroundColorChanger;
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
	private BackgroundColorChanger backgroundColorChanger;

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
		backgroundColorChanger = new BackgroundColorChanger();

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
		if (currentSourceCodeState.equals(SourcecodeState.OK) && (sourceCodeStateInfo.nextPlannedStepIsRefactorStep()
				|| sourceCodeStateInfo.refactorStepIsStillSuggested())) {
			currentSourceCodeState = SourcecodeState.ATC_FAIL;
		}
		GlobalTestState globalTestState = new GlobalTestState(currentSourceCodeState);
		String globalTestStateString = globalTestState.getTestStateOutputForDashboard();

		sourceCodeStateInfo.setSourceCodeState(globalTestStateString);

		if (abapCiDashboardView != null) {

			abapCiDashboardView.lblOverallTestState.setText(globalTestStateString);

			// abapCiDashboardView.projectline.setText(TODO reserved for projectinfo when
			// infoline is used for all infos);

			abapCiDashboardView.infoline.setText(buildInfoLine(abapPackageTestStatesForCurrentProject) + "        ");
			abapCiDashboardView.infoline.setLayoutData(abapCiDashboardView.infoline.getLayoutData());

			rebuildHyperlink(abapCiDashboardView.getEntireContainer(), abapCiDashboardView.openErrorHyperlink);

			abapCiDashboardView.setBackgroundColor(globalTestState.getColor());
			// abapCiDashboardView.lblOverallTestState.setForeground(globalTestState.getColor());
		}

		if (featureFacade.getSourceCodeVisualisationFeature().isChangeStatusBarBackgroundColorEnabled()) {
			backgroundColorChanger.change(Display.getCurrent().getActiveShell(), globalTestState.getColor().getRGB());
		}

		if (view != null && abapPackageTestStatesForCurrentProject != null) {
			view.setViewerInput(abapPackageTestStatesForCurrentProject);
			view.statusLabel.setText("CI run package summary updated");
		}
	}

	private void rebuildHyperlink(Composite container, Hyperlink link) {

		List<AbapPackageTestState> packagesWithFailedTests = getAbapPackageTestStatesForCurrentProject().stream()
				.filter(item -> item.getFirstFailedUnitTest() != null)
				.collect(Collectors.<AbapPackageTestState>toList());

		List<AbapPackageTestState> packagesWithFailedAtc = getAbapPackageTestStatesForCurrentProject().stream()
				.filter(item -> item.getFirstFailedAtc() != null).collect(Collectors.<AbapPackageTestState>toList());

		if (packagesWithFailedTests.size() > 0) {
			link.setVisible(true);
			List<InvalidItem> firstFailedTests = packagesWithFailedTests.stream()
					.map(item -> item.getFirstFailedUnitTest()).collect(Collectors.toList());
			link.setText(InvalidItemUtil.getOutputForUnitTest(firstFailedTests));
		} else {
			if (packagesWithFailedAtc.size() > 0) {
				link.setVisible(true);
				List<InvalidItem> firstFailedTests = packagesWithFailedAtc.stream()
						.map(item -> item.getFirstFailedAtc()).collect(Collectors.toList());
				link.setText(InvalidItemUtil.getOutputForAtcTest(firstFailedTests));
			} else {
				link.setVisible(false);
			}

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

	private String buildInfoLine(List<AbapPackageTestState> abapPackageTestStatesForCurrentProject) {
		int overallTests = abapPackageTestStatesForCurrentProject.stream().mapToInt(item -> item.getNumTests()).sum();
		int overallErrors = abapPackageTestStatesForCurrentProject.stream().mapToInt(item -> item.getAUnitNumErr())
				.sum();
		int overallSuppressed = abapPackageTestStatesForCurrentProject.stream()
				.mapToInt(item -> item.getAUnitNumSuppressed()).sum();

		int overallAtcNum = abapPackageTestStatesForCurrentProject.stream().mapToInt(item -> item.getAtcNumFiles())
				.sum();
		int overallAtcErr = abapPackageTestStatesForCurrentProject.stream().mapToInt(item -> item.getAtcNumErr()).sum();
		int overallAtcWarnings = abapPackageTestStatesForCurrentProject.stream().mapToInt(item -> item.getAtcNumWarn())
				.sum();
		int overallAtcInfos = abapPackageTestStatesForCurrentProject.stream().mapToInt(item -> item.getAtcNumInfo())
				.sum();
		int overallAtcSuppressed = abapPackageTestStatesForCurrentProject.stream()
				.mapToInt(item -> item.getAtcNumSuppressed()).sum();

		String unitTestInfoString = String.format("[%s / %s,%s]", overallTests, overallErrors, overallSuppressed);

		String atcInfoString = featureFacade.getAtcFeature().isActive()
				? String.format(" [%s / %s,%s,%s,%s]", overallAtcNum, overallAtcErr, overallAtcWarnings,
						overallAtcInfos, overallAtcSuppressed)
				: "";

		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		Date date = new Date();

		return dateFormat.format(date) + ": " + currentProject.getName() + " " + unitTestInfoString + " "
				+ atcInfoString;
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
