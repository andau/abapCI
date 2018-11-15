package abapci;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

import com.sap.adt.atc.IAtcCheckableItem;
import com.sap.adt.atc.ui.internal.launch.AtcLaunchShortcut;

import abapci.activation.Activation;
import abapci.activation.ActivationHelper;
import abapci.activation.ActivationPool;
import abapci.ci.presenter.ContinuousIntegrationPresenter;
import abapci.ci.views.AddOrUpdateContinuousIntegrationConfigPage;
import abapci.connections.SapConnection;
import abapci.domain.ContinuousIntegrationConfig;
import abapci.feature.FeatureFacade;
import abapci.feature.activeFeature.AtcFeature;
import abapci.handlers.MyAtcCheckableItem;
import abapci.jobs.CiJob;

public class GeneralResourceChangeListener implements IResourceChangeListener {

	private final SapConnection sapConnection;
	private boolean initialRun;
	private final CiJob job;
	private final ContinuousIntegrationPresenter continuousIntegrationPresenter;
	private final ActivationPool activationPool;
	
	private AtcFeature atcFeature; 

	public GeneralResourceChangeListener(ContinuousIntegrationPresenter continuousIntegrationPresenter) {
		sapConnection = new SapConnection();
		initialRun = false;
		this.continuousIntegrationPresenter = continuousIntegrationPresenter;
		job = CiJob.getInstance(continuousIntegrationPresenter);
		activationPool = ActivationPool.getInstance();
		
		FeatureFacade featureFacade = new FeatureFacade(); 
		atcFeature = featureFacade.getAtcFeature(); 
	}

	@Override
	public void resourceChanged(IResourceChangeEvent event) {

		if (event.getType() == IResourceChangeEvent.POST_CHANGE) {
			IResourceDelta delta = event.getDelta();
			if (delta == null) {
				return;
			}

			IResourceDelta[] resourceDeltas = delta.getAffectedChildren(IResourceDelta.CHANGED);

			try {
				if (resourceDeltas.length > 0) {

					IProject currentProject;

					try {
						currentProject = resourceDeltas[0].getResource().getProject();
					} catch (Exception ex) {
						currentProject = continuousIntegrationPresenter.getCurrentProject();
					}

					if (currentProject != null && (currentProject.hasNature(JavaCore.NATURE_ID)
							|| sapConnection.isConnected(currentProject.getName()))) {

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
							activatedInactiveObjects = (currentProject.hasNature(JavaCore.NATURE_ID))
									? activationPool.findAllActive()
									: null;

							job.setTriggerPackages(continuousIntegrationPresenter.getCurrentProject(),
									continuousIntegrationPresenter.getAbapPackageTestStatesForCurrentProject().stream()
											.map(item -> item.getPackageName()).distinct()
											.collect(Collectors.<String>toList()),
									activatedInactiveObjects);
							job.start(true);
						} else if (!activations.isEmpty() && (activationPool.hasUnprocessedActivationClicks()
								|| currentProject.hasNature(JavaCore.NATURE_ID))) {

							IProject activatedProject = activationPool.getCurrentProject();
							
							if (atcFeature.isAnnotationHandlingEnabled()) 
							{
							   executeAtcShortcut(activatedProject, activatedInactiveObjects);
							}
							if (activatedProject != null) {
								continuousIntegrationPresenter.setCurrentProject(activationPool.getCurrentProject());
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

	// EXPERIMENTAL

	private void executeAtcShortcut(IProject project, Collection<Activation> inactiveObjects) {
		List<IAtcCheckableItem> checkableItems = new ArrayList<>();

		for (Activation activation : inactiveObjects) {
			checkableItems.add(
					new MyAtcCheckableItem(activation.getUri(), activation.getClass().getName(), activation.getType()));
		}

		try {
			runAtcLaunchShortcut(project, checkableItems);
		} catch (Exception e) {
			// currently this is only an experimental function therefore we log the
			// exception and go on
			e.printStackTrace();
		}

	}

	private void runAtcLaunchShortcut(IProject project, List<IAtcCheckableItem> checkableItems)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException,
			SecurityException {

		try {

			Set<IAtcCheckableItem> adtItems = new HashSet<>(checkableItems);

			AtcLaunchShortcut atcLaunchShortcut = new AtcLaunchShortcut();
			Method launchShortcut = AtcLaunchShortcut.class.getDeclaredMethod("runAtcForSelectedItems", Set.class,
					IProject.class);
			launchShortcut.setAccessible(true);
			launchShortcut.invoke(atcLaunchShortcut, adtItems, project);

		} catch (Exception e) {
			// currently this is only an experimental function therefore we log the
			// exception and go on
			e.printStackTrace();
		}
	}

}
