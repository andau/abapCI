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
import com.sap.adt.atc.model.atcworklist.IAtcWorklist;
import com.sap.adt.atc.model.atcworklist.IAtcWorklistRun;
import com.sap.adt.project.AdtCoreProjectServiceFactory;
import com.sap.adt.tools.core.internal.AbapProjectService;
import com.sap.adt.tools.core.project.IAbapProject;

import abapci.feature.FeatureFacade;

public class AbapAtcHandler extends AbstractHandler {
    
	private FeatureFacade featureFacade; 
	
	public AbapAtcHandler() 
	{
		featureFacade = new FeatureFacade(); 
	}
	
	@Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
    	String packageName = event.getParameter("1");
    	return executePackage(packageName); 
    }

    public IAtcWorklist executePackage(String packageName)
    {
        IAtcWorklistBackendAccess worklistBackendAccess = AtcBackendServices.getWorklistBackendAccess();
        List<IAtcCheckableItem> checkableItems = new ArrayList<>();
        IProgressMonitor progressMonitor = new NullProgressMonitor();
        String projectName = featureFacade.getGeneralFeature().getDevelopmentProject(); 
        IProject project = AdtCoreProjectServiceFactory.createCoreProjectService().findProject(projectName);
        IAbapProject abapProject =  AbapProjectService.getInstance().createFromProjectUnchecked(project);
        
        //TODO Make check variant variable 
                
        String atcVariant = featureFacade.getAtcFeature().getVariant(); 
        String worklistId = worklistBackendAccess.createWorklist(abapProject, atcVariant, progressMonitor);       
        checkableItems.add(new MyAtcCheckableItem(createAtcUri(packageName), packageName, "DEVC/K"));
        IAtcWorklistRun worklistRun = worklistBackendAccess.startAtcRunForWorklist(abapProject, checkableItems, worklistId, progressMonitor);
        String objectSetName = packageName;
        boolean forceObjectSet = true;
        boolean includeExemptedFindings = false;
                
        return worklistBackendAccess.getWorklist(abapProject, worklistRun.getWorklistId(), worklistRun.getWorklistTimestamp().toString(), 
        		objectSetName, forceObjectSet, includeExemptedFindings, progressMonitor);
        
    }
    
    private URI createAtcUri(String packageName)
    {
    	return URI.create("/sap/bc/adt/vit/wb/object_type/devck/object_name/" + packageName); 
    }
    
}
 
