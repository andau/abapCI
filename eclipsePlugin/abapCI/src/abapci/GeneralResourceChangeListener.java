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

import abapci.activation.Activation;
import abapci.activation.ActivationPool;
import abapci.connections.SapConnection;
import abapci.domain.ActivationObject;
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
	private ActivationPool activationPool;
	private int currentInactiveObjectsCount;

	public GeneralResourceChangeListener(ContinuousIntegrationPresenter continuousIntegrationPresenter) {
		sapConnection = new SapConnection();
		initialRun = false;
		this.continuousIntegrationPresenter = continuousIntegrationPresenter;
		job = CiJob.getInstance(continuousIntegrationPresenter);
		activationPool = ActivationPool.getInstance();
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

						List<ActivationObject> inactiveObjects = sapConnection
								.getInactiveObjects(currentProject.getName());

						if (currentInactiveObjectsCount > inactiveObjects.size()) {
							inactiveObjects = activationPool.getLastInactiveObjects();
						}

						currentInactiveObjectsCount = inactiveObjects.size();
						activationPool.setLastInactiveObjects(inactiveObjects);

						List<Activation> activations = activationPool.findAllActiveOrIncludedInJob();

						List<String> selectedPackages = new ArrayList<String>();

						if (activations != null && !activations.isEmpty())
							for (ActivationObject inactiveObject : inactiveObjects) {
								if (inactiveObject.packagename != null
										&& !selectedPackages.contains(inactiveObject.packagename)
										&& activations.stream().anyMatch(item -> item.getObjectName().toLowerCase()
												.contains(inactiveObject.getClassname().toLowerCase()))) {
									selectedPackages.add(inactiveObject.packagename);
								}
							}

						showDialogForPackages(currentProject, selectedPackages);

						if (!initialRun || continuousIntegrationPresenter.runNecessary()) {
							initialRun = true;
							continuousIntegrationPresenter.setCurrentProject(currentProject);
							job.setTriggerPackages(continuousIntegrationPresenter.getCurrentProject(),
									continuousIntegrationPresenter.getAbapPackageTestStatesForCurrentProject().stream()
											.map(item -> item.getPackageName()).distinct()
											.collect(Collectors.<String>toList()),
									null);
							job.start();
						} else if (!selectedPackages.isEmpty()) {
							continuousIntegrationPresenter.setCurrentProject(currentProject);
							job.setTriggerPackages(continuousIntegrationPresenter.getCurrentProject(), selectedPackages,
									inactiveObjects);
							job.start();
							activationPool.changeActivedToIncludedInJob();
							activationPool.resetProcessedInactiveObjects();
							currentInactiveObjectsCount = 0;
						} else if (!activations.isEmpty()) {
							continuousIntegrationPresenter.setCurrentProject(currentProject);
							job.setTriggerPackages(continuousIntegrationPresenter.getCurrentProject(),
									continuousIntegrationPresenter.getAbapPackageTestStatesForCurrentProject().stream()
											.map(item -> item.getPackageName()).distinct()
											.collect(Collectors.<String>toList()),
									inactiveObjects);
							job.start();
							activationPool.changeActivedToIncludedInJob();
							activationPool.resetProcessedInactiveObjects();
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
