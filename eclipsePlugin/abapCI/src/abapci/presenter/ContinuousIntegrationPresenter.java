package abapci.presenter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.Hyperlink;

import abapci.Exception.ContinuousIntegrationConfigFileParseException;
import abapci.domain.AbapPackageTestState;
import abapci.domain.ContinuousIntegrationConfig;
import abapci.domain.GlobalTestState;
import abapci.domain.InvalidItem;
import abapci.domain.SourcecodeState;
import abapci.domain.TestResult;
import abapci.domain.TestResultSummary;
import abapci.domain.TestState;
import abapci.domain.UiColor;
import abapci.manager.DevelopmentProcessManager;
import abapci.model.IContinuousIntegrationModel;
import abapci.utils.AnnotationRuleColorChanger;
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
	private DevelopmentProcessManager developmentProcessManager;

	public ContinuousIntegrationPresenter(AbapCiMainView abapCiMainView,
			IContinuousIntegrationModel continuousIntegrationModel, IProject currentProject) {
		this.view = abapCiMainView;
		this.model = continuousIntegrationModel;
		this.currentProject = currentProject;
		this.abapPackageTestStates = new ArrayList<AbapPackageTestState>();
		developmentProcessManager = new DevelopmentProcessManager();

		loadPackages();
		setViewerInput();
	}

	public void setView(AbapCiMainView abapCiMainView) {
		this.view = abapCiMainView;
	}

	public void removeContinousIntegrationConfig(ContinuousIntegrationConfig ciConfig) {
		try {
			model.remove(ciConfig);
			loadPackages();
			setViewerInput();
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
			loadPackages();
			setViewerInput();
		} catch (ContinuousIntegrationConfigFileParseException e) {
			setStatusMessage("Parsing error when updating project",
					new Color(Display.getCurrent(), new RGB(255, 0, 0)));
		} catch (Exception ex) {
			setStatusMessage(String.format("General error when updating project, errormessage %s", ex.getMessage()),
					new Color(Display.getCurrent(), new RGB(255, 0, 0)));
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

		if (abapCiDashboardView != null) {

			SourcecodeState currentSourceCodeState = evalSourceCodeTestState();
			GlobalTestState globalTestState = new GlobalTestState(currentSourceCodeState);

			abapCiDashboardView.lblOverallTestState.setText(globalTestState.getTestStateOutputForDashboard());
			abapCiDashboardView.lblOverallTestState.redraw();

			// abapCiDashboardView.projectline.setText(TODO reserved for projectinfo when
			// infoline is used for all infos);

			abapCiDashboardView.infoline.setText(buildInfoLine(abapPackageTestStatesForCurrentProject) + "     ");
			abapCiDashboardView.infoline.setLayoutData(abapCiDashboardView.infoline.getLayoutData());

			rebuildHyperlink(abapCiDashboardView.getEntireContainer(), abapCiDashboardView.openErrorHyperlink);

			abapCiDashboardView.setBackgroundColor(globalTestState.getColor());
		}

		if (view != null && abapPackageTestStatesForCurrentProject != null) {
			view.setViewerInput(abapPackageTestStatesForCurrentProject);
			view.statusLabel.setText("Package information updated");
		}

		// changeColoringForRightAnnotationRuler();

	}

	private void changeColoringForRightAnnotationRuler() {
		IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		IEditorPart activeEditor = activePage.getActiveEditor();

		if (true) // check preferences
		{
			UiColor currentColor;
			SourcecodeState currentSourceCodeState = evalSourceCodeTestState();
			if (currentSourceCodeState == SourcecodeState.UT_FAIL) {
				currentColor = UiColor.RED;
			} else {
				currentColor = UiColor.WHITE;
			}
			AnnotationRuleColorChanger annotationRuleColorChanger = new AnnotationRuleColorChanger();
			annotationRuleColorChanger.change(activeEditor, currentColor, false, true);
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
			if (packagesWithFailedTests.size() == 1) {
				link.setText(
						InvalidItemUtil.getOutputForUnitTest(packagesWithFailedTests.get(0).getFirstFailedUnitTest())
								+ "     ");
			} else {
				link.setText(String.format("Open first failed tests for  %s packages", packagesWithFailedTests.size()));
			}
		} else {
			if (packagesWithFailedAtc.size() > 0) {
				link.setVisible(true);
				if (packagesWithFailedAtc.size() == 1) {
					link.setText(InvalidItemUtil.getOutputForAtcTest(packagesWithFailedAtc.get(0).getFirstFailedAtc())
							+ "     ");
				} else {
					link.setText(String.format("Open first failed atc finding for  %s packages",
							packagesWithFailedAtc.size()));
				}

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
		int overallOk = abapPackageTestStatesForCurrentProject.stream().mapToInt(item -> item.getAUnitNumOk()).sum();
		int overallErrors = abapPackageTestStatesForCurrentProject.stream().mapToInt(item -> item.getAUnitNumErr())
				.sum();
		int overallSuppressed = abapPackageTestStatesForCurrentProject.stream()
				.mapToInt(item -> item.getAUnitNumSuppressed()).sum();

		int overallAtcErr = abapPackageTestStatesForCurrentProject.stream().mapToInt(item -> item.getAtcNumErr()).sum();
		int overallAtcSuppressed = abapPackageTestStatesForCurrentProject.stream()
				.mapToInt(item -> item.getAtcNumSuppressed()).sum();

		String unitTestInfoString = String.format("[%s,%s,%s]", overallOk, overallErrors, overallSuppressed);
		String atcInfoString = String.format("[%s,%s]", overallAtcErr, overallAtcSuppressed);

		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		Date date = new Date();

		return dateFormat.format(date) + ": " + currentProject.getName() + " " + unitTestInfoString + " "
				+ atcInfoString;
	}

	private SourcecodeState evalSourceCodeTestState() {

		TestState currentUnitTestState = TestState.UNDEF;

		for (AbapPackageTestState testState : getAbapPackageTestStatesForCurrentProject()) {
			switch (currentUnitTestState) {
			case OFFL:
			case NOK:
				// no change as this is the highest state
				break;
			case UNDEF:
			case OK:
			case DEACT:
				currentUnitTestState = testState.getUnitTestState() != TestState.DEACT ? testState.getUnitTestState()
						: currentUnitTestState;
				break;
			}
		}

		developmentProcessManager.setUnitTeststate(currentUnitTestState);

		TestState currentAtcState = TestState.UNDEF;

		for (AbapPackageTestState testState : getAbapPackageTestStatesForCurrentProject()) {
			switch (currentAtcState) {
			case OFFL:
			case NOK:
				// no change as this is the highest state
				break;
			case UNDEF:
			case OK:
			case DEACT:
				currentAtcState = testState.getAtcTestState() != TestState.DEACT ? testState.getAtcTestState()
						: currentAtcState;
				break;
			}
		}

		developmentProcessManager.setAtcTeststate(currentAtcState);

		return developmentProcessManager.getSourcecodeState();
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
		return getAbapPackageTestStatesForCurrentProject().stream().anyMatch(
				item -> item.getUnitTestState() == TestState.UNDEF || item.getUnitTestState() == TestState.OFFL);
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
		for (AbapPackageTestState testState : getAbapPackageTestStatesForCurrentProject()) {
			if (testState.getPackageName().equals(atcTestResultSummary.getPackageName())) {
				TestResult newTestResult = mergeIntoExistingTestResult(testState.getAtcTestResult(),
						atcTestResultSummary.getTestResult());
				testState.setAtcTestResult(newTestResult);
			}
		}

		updateViewsAsync();
	}

	private TestResult mergeIntoExistingTestResult(TestResult currentTestResult, TestResult newTestResult) {

		List<InvalidItem> newInvalidItems = new ArrayList<InvalidItem>();

		for (InvalidItem invalidItem : currentTestResult.getActiveErrors()) {
			if (!newTestResult.getActivatedObjects().stream()
					.anyMatch(item -> item.getClassname().equals(invalidItem.getClassName()))) {
				newInvalidItems.add(invalidItem);
			}
		}

		newInvalidItems.addAll(newTestResult.getActiveErrors());

		return new TestResult(true, 0, newInvalidItems, null);
	}
}
