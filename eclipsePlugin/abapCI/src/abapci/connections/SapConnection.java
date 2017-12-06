package abapci.connections;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.preference.IPreferenceStore;

import com.sap.adt.activation.AdtActivationPlugin;
import com.sap.adt.activation.IActivationService;
import com.sap.adt.activation.IActivationServiceFactory;
import com.sap.adt.activation.model.inactiveObjects.IInactiveCtsObject;
import com.sap.adt.activation.model.inactiveObjects.IInactiveCtsObjectList;
import com.sap.adt.destinations.logon.AdtLogonServiceFactory;
import com.sap.adt.tools.core.model.adtcore.IAdtObjectReference;
import abapci.AbapCiPlugin;
import abapci.domain.ActivationObject;
import abapci.preferences.PreferenceConstants;

public class SapConnection {
	
	private List<ActivationObject> currentInactiveObjects;

	public SapConnection() 
	{
		currentInactiveObjects = new ArrayList<>(); 
	}

	public boolean isConnected() 
	{
		IPreferenceStore prefs = AbapCiPlugin.getDefault().getPreferenceStore();
		String projectName = prefs.getString(PreferenceConstants.PREF_DEV_PROJECT);
		return AdtLogonServiceFactory.createLogonService().isLoggedOn(projectName);
	}
	
	public List<ActivationObject> unprocessedActivatedObjects() 
	{
		List<ActivationObject> activatedObjects = new ArrayList<>(); 
		
        IActivationServiceFactory activationServiceFactory = AdtActivationPlugin.getDefault()
                .getActivationServiceFactory();
        IPreferenceStore prefs = AbapCiPlugin.getDefault().getPreferenceStore();
        String destinationId = prefs.getString(PreferenceConstants.PREF_DEV_PROJECT);
        IActivationService activationService = activationServiceFactory.createActivationService(destinationId);
        IInactiveCtsObjectList newInactiveCtsObjectList = activationService
                .getInactiveCtsObjects(new NullProgressMonitor());
        List<ActivationObject> newInactiveObjects = convertToStringList(newInactiveCtsObjectList);

        if (checkForNewInactiveItems(newInactiveObjects, currentInactiveObjects)) 
        {
        	activatedObjects = currentInactiveObjects; 
        }
    	currentInactiveObjects = newInactiveObjects; 
    	
    	return activatedObjects; 

	}
	
    private boolean checkForNewInactiveItems(List<ActivationObject> newInactiveObjects,
			List<ActivationObject> currentInactiveObjects) {
		return newInactiveObjects.size() < currentInactiveObjects.size();
	}

	private List<ActivationObject> convertToStringList(IInactiveCtsObjectList newInactiveCtsObjectList) {
        List<ActivationObject> newObjects = new ArrayList<>();
        for (Iterator<IInactiveCtsObject> iterator = newInactiveCtsObjectList.getEntry().iterator(); iterator.hasNext();) {
            IInactiveCtsObject ctsObject =  iterator.next();
            if (ctsObject.hasObjectRef())
            {
            	IAdtObjectReference ref = ctsObject.getObject().getRef();
				String packageName = ref.getPackageName(); 
            	if (packageName != null) 
            	{            		
                    newObjects.add(new ActivationObject(ref.getPackageName(), ref.getName()));                
            	}
            }
            
        }
        return newObjects;
    }

}
