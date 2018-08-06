package abapci.presenter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;

import abapci.Exception.ContinuousIntegrationConfigFileParseException;
import abapci.domain.AbapPackageTestState;
import abapci.domain.ContinuousIntegrationConfig;
import abapci.domain.GlobalTestState;
import abapci.domain.SourcecodeState;
import abapci.domain.TestResult;
import abapci.model.IContinuousIntegrationModel;
import abapci.views.AbapCiDashboardView;
import abapci.views.AbapCiMainView;

public class ContinuousIntegrationPresenter {

	private AbapCiMainView view;
	private IContinuousIntegrationModel model;
	private IProject currentProject;
	private List<AbapPackageTestState> abapPackageTestStates;
	private AbapCiDashboardView abapCiDashboardView;
	private SourcecodeState sourcecodeState;

	public ContinuousIntegrationPresenter(AbapCiMainView abapCiMainView,
			IContinuousIntegrationModel continuousIntegrationModel, IProject currentProject) {
		this.view = abapCiMainView;
		this.model = continuousIntegrationModel;
		this.currentProject = currentProject;
		this.abapPackageTestStates = new ArrayList<AbapPackageTestState>();
		this.sourcecodeState = SourcecodeState.UNDEF;

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
		model.add(ciConfig);
		loadPackages();
		setViewerInput();
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

	private void loadPackages() {
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
		if (currentProject != null) {
			return abapPackageTestStates.stream().filter(
					item -> item.getProjectName() != null && item.getProjectName().equals(currentProject.getName()))
					.collect(Collectors.<AbapPackageTestState>toList());
		} else
			return new ArrayList<AbapPackageTestState>();
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
		if (abapCiDashboardView != null) {

			GlobalTestState globalTestState = new GlobalTestState(sourcecodeState);

			abapCiDashboardView.lblOverallTestState.setText(globalTestState.getTestStateOutputForDashboard());
			abapCiDashboardView.lblOverallTestState.redraw();

			DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
			Date date = new Date();
			abapCiDashboardView.infoline.setText("Last test run at:" + dateFormat.format(date));
			abapCiDashboardView.infoline.redraw();

			abapCiDashboardView.setBackgroundColor(globalTestState.getColor());
		}

		if (view != null && getAbapPackageTestStatesForCurrentProject() != null) {
			view.setViewerInput(getAbapPackageTestStatesForCurrentProject());
			view.statusLabel.setText("Package information updated");
		}
	}

	public IProject getCurrentProject() {
		return currentProject;
	}

	public void setCurrentProject(IProject project) {
		currentProject = project;
	}

	public void setStatusMessage(String message) {
		Runnable task = () -> setStatusMessageInternal(message);
		Display.getDefault().asyncExec(task);
	}

	private void setStatusMessageInternal(String message) {

		// TODO call async
		try {
			view.statusLabel.setText(message);
			org.eclipse.swt.graphics.Color red = Display.getCurrent().getSystemColor(SWT.COLOR_RED);
			view.statusLabel.setForeground(red);
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

}
