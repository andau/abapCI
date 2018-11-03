package abapci;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.core.internal.resources.Project;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

import abapci.activation.Activation;
import abapci.activation.ActivationPool;
import abapci.activation.ActivationHelper;
import abapci.connections.SapConnection;
import abapci.domain.ContinuousIntegrationConfig;
import abapci.feature.FeatureFacade;
import abapci.jobs.CiJob;
import abapci.presenter.ContinuousIntegrationPresenter;
import abapci.views.wizard.AddOrUpdateContinuousIntegrationConfigPage;

public class GeneralResourceChangeListener implements IResourceChangeListener {

	private SapConnection sapConnection;
	private boolean initialRun;
	private CiJob job;
	private ContinuousIntegrationPresenter continuousIntegrationPresenter;
	private ActivationPool activationPool;

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

					if (currentProject != null && (currentProject.hasNature(JavaCore.NATURE_ID) || sapConnection.isConnected(currentProject.getName()))) {

						List<String> selectedPackages = new ArrayList<>();
						List<Activation> activatedInactiveObjects = new ArrayList<>();

						// TODO merge activation and activatedInactive
						List<Activation> activations = activationPool.findAllActiveOrIncludedInJob();

						if (activations != null && !activations.isEmpty()) {
							activatedInactiveObjects = activations;
							selectedPackages = ActivationHelper.getPackages(activations);
						}

						if (!initialRun || continuousIntegrationPresenter.runNecessary()) {
							initialRun = true;
							continuousIntegrationPresenter.setCurrentProject(currentProject);
							activatedInactiveObjects = (currentProject.hasNature(JavaCore.NATURE_ID)) ?   
								activationPool.findAllActive() : null;
							
							job.setTriggerPackages(continuousIntegrationPresenter.getCurrentProject(),
									continuousIntegrationPresenter.getAbapPackageTestStatesForCurrentProject().stream()
											.map(item -> item.getPackageName()).distinct()
											.collect(Collectors.<String>toList()),
									activatedInactiveObjects);
							job.start(true);
						} else if (!activations.isEmpty() && ( activationPool.hasUnprocessedActivationClicks() || currentProject.hasNature(JavaCore.NATURE_ID)))
						{
							IProject activatedProject = activationPool.getCurrentProject();
							if (activatedProject != null) {
								continuousIntegrationPresenter
										.setCurrentProject(activationPool.getCurrentProject());
							}
							job.setTriggerPackages(continuousIntegrationPresenter.getCurrentProject(), selectedPackages,
									activatedInactiveObjects);
							job.start(true);
							System.out.println("Job started");
							activationPool.resetUnprocessedActivationClicks();
							if (!activationPool.findActiveActivationsAssignedToProject().isEmpty()) {
								showDialogForPackages(currentProject, selectedPackages);
							}
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

		AddOrUpdateContinuousIntegrationConfigPage addContinuousIntegrationConfigPage = new AddOrUpdateContinuousIntegrationConfigPage(
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), continuousIntegrationPresenter,
				ciConfig, true);
		if (addContinuousIntegrationConfigPage.open() == Window.OK) {
			continuousIntegrationPresenter.setStatusMessage(
					String.format("Configuration for package %s added to CI run", ciConfig.getPackageName()));
		}

	}
}
