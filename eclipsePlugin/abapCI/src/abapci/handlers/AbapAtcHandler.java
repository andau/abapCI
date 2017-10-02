package abapci.handlers;

import java.net.URI;
import java.util.ArrayList;
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
import com.sap.adt.atc.model.atcworklist.IAtcWorklistRun;
import com.sap.adt.project.AdtCoreProjectServiceFactory;
import com.sap.adt.tools.core.internal.AbapProjectService;
import com.sap.adt.tools.core.project.IAbapProject;


public class AbapAtcHandler extends AbstractHandler {
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		String packageName = event.getParameter("1");
		IAtcWorklistBackendAccess worklistBackendAccess = AtcBackendServices.getWorklistBackendAccess();
        List<IAtcCheckableItem> checkableItems = new ArrayList<IAtcCheckableItem>();
        IProgressMonitor progressMonitor = new NullProgressMonitor();
        IProject project = AdtCoreProjectServiceFactory.createCoreProjectService().findProject(packageName);
        IAbapProject abapProject =  AbapProjectService.getInstance().createFromProjectUnchecked(project);
        
		
		String worklistId = worklistBackendAccess.createWorklist(abapProject, "ZVAR_GAUTSCH", progressMonitor);       
        checkableItems.add(new MyAtcCheckableItem(URI.create("/sap/bc/adt/vit/wb/object_type/devck/object_name/" + packageName), packageName, "DEVC/K"));
        IAtcWorklistRun worklistRun = worklistBackendAccess.startAtcRunForWorklist(abapProject, checkableItems, worklistId, progressMonitor);
        String objectSetName = packageName;
        boolean forceObjectSet = false;
        boolean includeExemptedFindings = false;
        
        
        return worklistBackendAccess.getWorklist(abapProject, worklistRun.getWorklistId(), worklistRun.getWorklistTimestamp().toString(), objectSetName, forceObjectSet, includeExemptedFindings , progressMonitor);
		
	}
}
