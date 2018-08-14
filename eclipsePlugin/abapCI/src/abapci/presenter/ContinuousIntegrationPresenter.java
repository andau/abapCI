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
import org.eclipse.ui.forms.widgets.Hyperlink;

import abapci.Exception.ContinuousIntegrationConfigFileParseException;
import abapci.domain.AbapPackageTestState;
import abapci.domain.ContinuousIntegrationConfig;
import abapci.domain.GlobalTestState;
import abapci.domain.SourcecodeState;
import abapci.domain.TestResult;
import abapci.domain.TestState;
import abapci.domain.UnitTestResultSummary;
import abapci.manager.DevelopmentProcessManager;
import abapci.model.IContinuousIntegrationModel;
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
	private SourcecodeState sourcecodeState;
	private DevelopmentProcessManager developmentProcessManager;

	public ContinuousIntegrationPresenter(AbapCiMainView abapCiMainView,
			IContinuousIntegrationModel continuousIntegrationModel, IProject currentProject) {
		this.view = abapCiMainView;
		this.model = continuousIntegrationModel;
		this.currentProject = currentProject;
		this.abapPackageTestStates = new ArrayList<AbapPackageTestState>();
		this.sourcecodeState = SourcecodeState.UNDEF;
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
		updateViewsAsync(this.sourcecodeState);
	}

	public void updateViewsAsync(SourcecodeState sourcecodeState) {
		this.sourcecodeState = sourcecodeState;
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
	}

	private void rebuildHyperlink(Composite container, Hyperlink link) {

		List<AbapPackageTestState> packagesWithFailedTests = getAbapPackageTestStatesForCurrentProject().stream()
				.filter(item -> item.getFirstFailedUnitTest() != null)
				.collect(Collectors.<AbapPackageTestState>toList());

		if (packagesWithFailedTests.isEmpty()) {
			link.setVisible(false);
		} else {
			link.setVisible(true);
			if (packagesWithFailedTests.size() == 1) {
				link.setText(
						InvalidItemUtil.getOutputForUnitTest(packagesWithFailedTests.get(0).getFirstFailedUnitTest())
								+ "     ");
			} else {
				link.setText(String.format("Open first failed tests for  %s packages", packagesWithFailedTests.size()));
			}
		}

	}

	public void openEditorsForFailedTests() {

		List<AbapPackageTestState> packagesWithFailedTests = getAbapPackageTestStatesForCurrentProject().stream()
				.filter(item -> item.getFirstFailedUnitTest() != null)
				.collect(Collectors.<AbapPackageTestState>toList());

		EditorHandler.open(currentProject, packagesWithFailedTests);
	}

	private String buildInfoLine(List<AbapPackageTestState> abapPackageTestStatesForCurrentProject) {
		int overallOk = abapPackageTestStatesForCurrentProject.stream().mapToInt(item -> item.getAUnitNumOk()).sum();
		int overallErrors = abapPackageTestStatesForCurrentProject.stream().mapToInt(item -> item.getAUnitNumErr())
				.sum();
		int overallSuppressed = abapPackageTestStatesForCurrentProject.stream()
				.mapToInt(item -> item.getAUnitNumSuppressed()).sum();

		String unitTestInfoString = String.format("(%s,%s,%s)", overallOk, overallErrors, overallSuppressed);

		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		Date date = new Date();

		return dateFormat.format(date) + ": " + currentProject.getName() + " " + unitTestInfoString;
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

	public void mergeUnitTestResultSummary(UnitTestResultSummary unitTestResultSummary) {

		for (AbapPackageTestState testState : getAbapPackageTestStatesForCurrentProject()) {
			if (testState.getPackageName().equals(unitTestResultSummary.getPackageName())) {
				testState.setUnitTestResult(unitTestResultSummary.getTestResult());
			}
		}

		updateViewsAsync();
	}
}
