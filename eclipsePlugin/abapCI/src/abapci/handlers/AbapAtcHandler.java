package abapci.handlers;

import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.statushandlers.StatusManager;

import com.sap.adt.atc.AtcBackendServices;
import com.sap.adt.atc.AtcCheckVariantMode;
import com.sap.adt.atc.AtcCustomizingFlavor;
import com.sap.adt.atc.IAtcCheckableItem;
import com.sap.adt.atc.IAtcCustomizing;
import com.sap.adt.atc.IAtcSetting;
import com.sap.adt.atc.IAtcWorklistBackendAccess;
import com.sap.adt.atc.model.atcworklist.IAtcWorklist;
import com.sap.adt.atc.model.atcworklist.IAtcWorklistRun;
//import com.sap.adt.atc.ui.internal.IAtcWorklistService;
import com.sap.adt.tools.core.internal.AbapProjectService;
import com.sap.adt.tools.core.project.IAbapProject;
import com.sap.adt.tools.core.ui.AbapCoreUi;
import com.sap.adt.tools.core.ui.IStatusLineHelper;

import abapci.activation.Activation;
import abapci.feature.AtcProjectSettingStore;
import abapci.feature.FeatureFacade;

public class AbapAtcHandler extends AbstractHandler {

	private FeatureFacade featureFacade;

	public AbapAtcHandler() {
		featureFacade = new FeatureFacade();
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		String packageName = event.getParameter("1");
		return executePackage(null, packageName);
	}

	public IAtcWorklist executePackage(IProject project, String packageName) {
		IAtcWorklistBackendAccess worklistBackendAccess = AtcBackendServices.getWorklistBackendAccess();
		List<IAtcCheckableItem> checkableItems = new ArrayList<>();
		IProgressMonitor progressMonitor = new NullProgressMonitor();
		IAbapProject abapProject = AbapProjectService.getInstance().createFromProjectUnchecked(project);

		String atcVariant = featureFacade.getAtcFeature().getVariant();
		String worklistId = worklistBackendAccess.createWorklist(abapProject, atcVariant, progressMonitor);
		checkableItems.add(new MyAtcCheckableItem(createAtcUri(packageName), packageName, "DEVC/K"));
		IAtcWorklistRun worklistRun = worklistBackendAccess.startAtcRunForWorklist(abapProject, checkableItems,
				worklistId, progressMonitor);
		String objectSetName = packageName;
		boolean forceObjectSet = true;
		boolean includeExemptedFindings = false;

		return worklistBackendAccess.getWorklist(abapProject, worklistRun.getWorklistId(),
				worklistRun.getWorklistTimestamp().toString(), objectSetName, forceObjectSet, includeExemptedFindings,
				progressMonitor);

	}

	public IAtcWorklist executeObjects(IProject project, Collection<Activation> inactiveObjects) {
		IAtcWorklistBackendAccess worklistBackendAccess = AtcBackendServices.getWorklistBackendAccess();
		IAbapProject abapProject = AbapProjectService.getInstance().createFromProjectUnchecked(project);
		List<IAtcCheckableItem> checkableItems = new ArrayList<>();

		IProgressMonitor progressMonitor = new NullProgressMonitor();
		String atcVariant = featureFacade.getAtcFeature().getVariant();
		String worklistId = worklistBackendAccess.createWorklist(abapProject, atcVariant, progressMonitor);
		for (Activation activation : inactiveObjects) {
			checkableItems.add(
					new MyAtcCheckableItem(activation.getUri(), activation.getClass().getName(), activation.getType()));
		}

		IAtcWorklistRun worklistRun = worklistBackendAccess.startAtcRunForWorklist(abapProject, checkableItems,
				worklistId, progressMonitor);

		boolean forceObjectSet = true;
		boolean includeExemptedFindings = false;
		String objectSetName = "TODO";

		/**
		 * try {
		 * 
		 * launchInNewThread(project, getProjectSetting(abapProject), new
		 * HashSet<IAtcCheckableItem>(checkableItems)); } catch
		 * (InvocationTargetException | InterruptedException | CoreException e) { //
		 * TODO Auto-generated catch block e.printStackTrace(); }
		 **/

		return worklistBackendAccess.getWorklist(abapProject, worklistRun.getWorklistId(),
				worklistRun.getWorklistTimestamp().toString(), objectSetName, forceObjectSet, includeExemptedFindings,
				progressMonitor);

	}

	private URI createAtcUri(String packageName) {
		return URI.create("/sap/bc/adt/vit/wb/object_type/devck/object_name/" + packageName);
	}

	protected IAtcSetting getProjectSetting(IAbapProject abapProject)
			throws InvocationTargetException, InterruptedException, CoreException {
		AtcCustomizingFlavor flavor = getFlavor(abapProject);
		AtcProjectSettingStore store = new AtcProjectSettingStore(abapProject.getProject(), flavor);
		return store.readSetting();
	}

	private static AtcCustomizingFlavor getFlavor(IAbapProject abapProject)
			throws InvocationTargetException, InterruptedException, CoreException {
		// IAtcCustomizing customizing = new
		// AtcCustomizingAccess().tryToGetAssynchronous(abapProject);
		// if (customizing == null) {
		// return AtcCustomizingFlavor.UNKNOWN;
		// }
		// return customizing.getFlavor();
		return AtcCustomizingFlavor.CHECKMAN;
	}

	protected void launchInNewThread(final IProject projectToExamine, final IAtcSetting projectSetting,
			final Set<IAtcCheckableItem> adtItems) {

		IAtcLauncher launcher = getLauncher();
		try {
			launcher.launch(projectToExamine.getName(), projectSetting, adtItems);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/**
		 * Job job = new Job("atcJob") {
		 * 
		 * protected IStatus run(IProgressMonitor monitor) { IAtcLauncher launcher =
		 * getLauncher(); try { launcher.launch(projectToExamine.getName(),
		 * projectSetting, adtItems); } catch (CoreException e) { return new Status(4,
		 * "com.sap.adt.atc.ui", e.getMessage(), (Throwable) e); } return
		 * Status.OK_STATUS; }
		 * 
		 * public boolean belongsTo(Object family) { return "AtcRun".equals(family); }
		 * };
		 **/
		// if (Display.getCurrent() != null) {
		// ProgressMonitorToUpdateUi listener = new
		// ProgressMonitorToUpdateUi(Display.getCurrent(), projectToExamine);
		// job.addJobChangeListener((IJobChangeListener) listener);
		// IProgressService progressService =
		// PlatformUI.getWorkbench().getProgressService();
		// progressService.showInDialog(null, job);
		// }

		// job.schedule();
	}

	protected IAtcLauncher getLauncher() {
		return new AtcLauncher(DebugPlugin.getDefault().getLaunchManager());
	}

	final class ProgressMonitorToUpdateUi extends JobChangeAdapter {
		private final Display display;
		private final IProject project;

		public ProgressMonitorToUpdateUi(Display display, IProject project) {
			this.display = display;
			this.project = project;
		}

		protected IStatusLineHelper getStatusLineHelper() {
			return AbapCoreUi.getStatusLineHelper();
		}

		public void done(IJobChangeEvent event) {
			Runnable updateUI = new Runnable() {

				@Override
				public void run() {
					IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
					if (window == null) {
						return;
					}
					IWorkbenchPage page = window.getActivePage();
					if (page == null) {
						return;
					}
					IWorkbenchPart part = page.getActivePart();
					if (part == null) {
						return;
					}
					if (ProgressMonitorToUpdateUi.this.hasFindingsOrFailures()) {
						this.bringProblemsViewToTop(page);
					} else {
					}
				}

				private void bringProblemsViewToTop(IWorkbenchPage page) {
					try {
						IViewPart problemView = page.showView("org.eclipse.ui.views.ProblemView", null, 2);
						page.bringToTop((IWorkbenchPart) problemView);
					} catch (PartInitException partInitException) {
						return;
					}
				}
			};
			this.display.asyncExec(updateUI);
		}

		private boolean hasFindingsOrFailures() {
			IMarker[] findMarkers;
			try {
				findMarkers = this.project.findMarkers("com.sap.adt.tools.atc.finding", true, 2);
			} catch (CoreException coreException) {
				return false;
			}
			if (findMarkers != null && findMarkers.length > 0) {
				return true;
			}
			return false;
		}

	}

	protected String getCheckVariant(IAbapProject abapProject, IAtcSetting projectSetting) {
		String variantName;
		if (projectSetting.getVariantMode().equals((Object) AtcCheckVariantMode.USER_SPECIFIC)) {
			variantName = projectSetting.getVariantName();
		} else {
			IAtcCustomizing customizing;
			try {
				customizing = new AtcCustomizingAccess().tryToGetAssynchronous(abapProject);
			} catch (InvocationTargetException e) {
				StatusManager.getManager()
						.handle((IStatus) new Status(4, "com.sap.adt.atc.ui", e.getMessage(), (Throwable) e));
				return null;
			} catch (InterruptedException e) {
				StatusManager.getManager()
						.handle((IStatus) new Status(4, "com.sap.adt.atc.ui", e.getMessage(), (Throwable) e));
				return null;
			} catch (CoreException e) {
				StatusManager.getManager()
						.handle((IStatus) new Status(4, "com.sap.adt.atc.ui", e.getMessage(), (Throwable) e));
				return null;
			}
			variantName = customizing.getSystemDefaultCheckVariant();
		}
		return variantName;
	}

}
