package abapci.handlers;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;

import com.sap.adt.atc.AtcBackendServices;
import com.sap.adt.atc.IAtcCheckableItem;
import com.sap.adt.atc.IAtcWorklistBackendAccess;
import com.sap.adt.atc.model.atcworklist.IAtcWorklist;
import com.sap.adt.atc.model.atcworklist.IAtcWorklistRun;
import com.sap.adt.tools.core.internal.AbapProjectService;
import com.sap.adt.tools.core.project.IAbapProject;

import abapci.activation.Activation;
import abapci.feature.activeFeature.AtcFeature;

public class AbapAtcHandler extends AbstractHandler {

	private final AtcFeature atcFeature;

	public AbapAtcHandler(AtcFeature atcFeature) {
		this.atcFeature = atcFeature;
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

		String atcVariant = atcFeature.getVariant();
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
		String atcVariant = atcFeature.getVariant();
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

}
