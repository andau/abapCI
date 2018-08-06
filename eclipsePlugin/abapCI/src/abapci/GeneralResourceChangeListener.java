package abapci;

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
	private FeatureFacade featureFacade;

	public GeneralResourceChangeListener(ContinuousIntegrationPresenter continuousIntegrationPresenter) {
		sapConnection = new SapConnection();
		initialRun = false;
		this.continuousIntegrationPresenter = continuousIntegrationPresenter;
		job = CiJob.getInstance(continuousIntegrationPresenter);
		activationPool = ActivationPool.getInstance();
		featureFacade = new FeatureFacade();
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

						// String uriString =
						// unitTestResultSummary.getTestResult().getFirstInvalidItem().getFirstStackEntry().getUri()
						// .toString();
						// String uriString =
						// "/sap/bc/adt/oo/classes/zcl_autoformatted_class/includes/testclasses#start=15,0";
						// IAdtObjectReference objRef =
						// AdtObjectReferenceAdapterFactory.create(uriString);
						// AdtNavigationServiceFactory.createNavigationService().navigate(currentProject,
						// objRef, true);

						List<ActivationObject> activatedObjects = sapConnection
								.unprocessedActivatedObjects(currentProject.getName());

						List<Activation> activations = activationPool.getActiveActivations();
						continuousIntegrationPresenter.setCurrentProject(currentProject);

						if (!initialRun) {

							initialRun = true;
							continuousIntegrationPresenter.setCurrentProject(currentProject);
							// continuousIntegrationPresenter.updateViewsAsync();
							job.start();

						} else if (!activatedObjects.isEmpty()) {

							List<String> packages = activatedObjects.stream().map(item -> item.getPackagename())
									.filter(item -> item != null).distinct().collect(Collectors.<String>toList());
							if (!packages.isEmpty()) {
								List<String> triggerPackages = continuousIntegrationPresenter
										.getAbapPackageTestStatesForCurrentProject().stream()
										.filter(item -> item.getPackageName() != null)
										.map(item -> item.getPackageName()).collect(Collectors.<String>toList());

								for (String triggerPackage : packages) {
									if (!continuousIntegrationPresenter.containsPackage(currentProject.getName(),
											triggerPackage)) {
										ContinuousIntegrationConfig ciConfig = new ContinuousIntegrationConfig(
												currentProject.getName(), triggerPackage, true, true);

										if (featureFacade.getShowDialogNewPackageForCiRun().isActive()) {
											Runnable runnable = () -> handleNewConfig(ciConfig);
											Display.getDefault().asyncExec(runnable);
										}
									}
								}
								job.setTriggerPackages(continuousIntegrationPresenter.getCurrentProject(),
										triggerPackages);
							} else {
								job.setTriggerPackages(continuousIntegrationPresenter.getCurrentProject(),
										activatedObjects.stream().map(item -> item.getPackagename()).distinct()
												.collect(Collectors.<String>toList()));
							}
							job.start();
						} else if (!activations.isEmpty()) {
							job.setTriggerPackages(continuousIntegrationPresenter.getCurrentProject(),
									continuousIntegrationPresenter.getAbapPackageTestStatesForCurrentProject().stream()
											.map(item -> item.getPackageName()).distinct()
											.collect(Collectors.<String>toList()));
							job.start();
							activationPool.unregisterAllActivated();
						}
					}
				}

			} catch (InactivatedObjectEvaluationException ex) {
				Runnable runnable = () -> continuousIntegrationPresenter.setStatusMessage(
						String.format("Evaluation of inactiveObjects failed, errormessage %s", ex.getMessage()));
				Display.getDefault().asyncExec(runnable);
			} catch (Exception ex) {
				Runnable runnable = () -> continuousIntegrationPresenter
						.setStatusMessage(String.format("CI Run failed, errormessage %s", ex.getMessage()));
				Display.getDefault().asyncExec(runnable);

			}

		}
	}

	private void handleNewConfig(ContinuousIntegrationConfig ciConfig) {

		AddContinuousIntegrationConfigPage addContinuousIntegrationConfigPage = new AddContinuousIntegrationConfigPage(
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), continuousIntegrationPresenter,
				ciConfig);
		if (addContinuousIntegrationConfigPage.open() == Window.OK) {
			// TODO
		}

	}
}
