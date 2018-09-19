package abapci;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

import abapci.Exception.InactivatedObjectEvaluationException;
import abapci.activation.Activation;
import abapci.activation.ActivationDetector;
import abapci.activation.ActivationHelper;
import abapci.connections.SapConnection;
import abapci.domain.ContinuousIntegrationConfig;
import abapci.feature.FeatureFacade;
import abapci.jobs.CiJob;
import abapci.presenter.ContinuousIntegrationPresenter;
import abapci.views.wizard.AddContinuousIntegrationConfigPage;

public class GeneralResourceChangeListener implements IResourceChangeListener {

	private SapConnection sapConnection;
	private boolean initialRun;
	private CiJob job;
	private ContinuousIntegrationPresenter continuousIntegrationPresenter;
	private ActivationDetector activationDetector;
	private int currentInactiveObjectsCount;

	public GeneralResourceChangeListener(ContinuousIntegrationPresenter continuousIntegrationPresenter) {
		sapConnection = new SapConnection();
		initialRun = false;
		this.continuousIntegrationPresenter = continuousIntegrationPresenter;
		job = CiJob.getInstance(continuousIntegrationPresenter);
		activationDetector = ActivationDetector.getInstance();
		new FeatureFacade();
	}

	public void resourceChanged(IResourceChangeEvent event) {

		if (event.getType() == IResourceChangeEvent.POST_CHANGE) {
			IResourceDelta delta = event.getDelta();
			if (delta == null)
				return;

			IResourceDelta[] resourceDeltas = delta.getAffectedChildren(IResourceDelta.CHANGED);

			try {
				if (resourceDeltas.length > 0) {

					IProject currentProject;

					try {
						currentProject = resourceDeltas[0].getResource().getProject();
					} catch (Exception ex) {
						currentProject = continuousIntegrationPresenter.getCurrentProject();
					}

					if (currentProject != null && sapConnection.isConnected(currentProject.getName())) {
						updateInactiveObjects(currentProject);

						List<String> selectedPackages = new ArrayList<>();
						List<Activation> activatedInactiveObjects = new ArrayList<>();

						// TODO merge activation and activatedInactive
						List<Activation> activations = activationDetector.findAllActiveOrIncludedInJob();

						if (activations != null && !activations.isEmpty()) {
							activatedInactiveObjects = activations;
							selectedPackages = ActivationHelper.getPackages(activations);
						}

						if (!initialRun || continuousIntegrationPresenter.runNecessary()) {
							initialRun = true;
							continuousIntegrationPresenter.setCurrentProject(currentProject);
							job.setTriggerPackages(continuousIntegrationPresenter.getCurrentProject(),
									continuousIntegrationPresenter.getAbapPackageTestStatesForCurrentProject().stream()
											.map(item -> item.getPackageName()).distinct()
											.collect(Collectors.<String>toList()),
									null);
							job.start();
						} else if (!activations.isEmpty()) {
							continuousIntegrationPresenter.setCurrentProject(currentProject);
							job.setTriggerPackages(continuousIntegrationPresenter.getCurrentProject(), selectedPackages,
									activatedInactiveObjects);
							job.start();
							if (!activationDetector.findActiveActivationsAssignedToProject().isEmpty()) {
								showDialogForPackages(currentProject, selectedPackages);
							}
							activationDetector.changeActivedToIncludedInJob();

							activationDetector.resetProcessedInactiveObjects();

							currentInactiveObjectsCount = 0;
						}
					}
				}

			} catch (Exception ex) {
				Runnable runnable = () -> continuousIntegrationPresenter
						.setStatusMessage(String.format("CI Run failed, errormessage %s", ex.getMessage()));
				Display.getDefault().asyncExec(runnable);

			}

		}
	}

	private void updateInactiveObjects(IProject currentProject) {
		List<Activation> inactiveObjects;
		try {
			inactiveObjects = sapConnection.getInactiveObjects(currentProject.getName());

			if (currentInactiveObjectsCount > inactiveObjects.size()) {
				inactiveObjects = activationDetector.getLastInactiveObjects();
			}

			currentInactiveObjectsCount = inactiveObjects.size();
			activationDetector.setLastInactiveObjects(inactiveObjects);

		} catch (InactivatedObjectEvaluationException e) {
			// if inactiveObjects could not be set we move on
		}
	}

	private void showDialogForPackages(IProject currentProject, List<String> packages) {
		for (String triggerPackage : packages) {
			if (!continuousIntegrationPresenter.containsPackage(currentProject.getName(), triggerPackage)) {
				ContinuousIntegrationConfig ciConfig = new ContinuousIntegrationConfig(currentProject.getName(),
						triggerPackage, true, true);

				Runnable runnable = () -> handleNewConfig(ciConfig);
				Display.getDefault().asyncExec(runnable);
			}
		}
	}

	private void handleNewConfig(ContinuousIntegrationConfig ciConfig) {

		AddContinuousIntegrationConfigPage addContinuousIntegrationConfigPage = new AddContinuousIntegrationConfigPage(
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), continuousIntegrationPresenter,
				ciConfig, true);
		if (addContinuousIntegrationConfigPage.open() == Window.OK) {
			continuousIntegrationPresenter.loadPackages();
		}

	}
}
