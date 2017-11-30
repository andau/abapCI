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

import abapci.AbapCiPlugin;
import abapci.preferences.PreferenceConstants;

public class SapConnection {
	
	private ArrayList<String> currentInactiveObjects;

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
	
	public boolean unprocessedActivatedObjects() 
	{
        IActivationServiceFactory activationServiceFactory = AdtActivationPlugin.getDefault()
                .getActivationServiceFactory();
        IPreferenceStore prefs = AbapCiPlugin.getDefault().getPreferenceStore();
        String destinationId = prefs.getString(PreferenceConstants.PREF_DEV_PROJECT);
        IActivationService activationService = activationServiceFactory.createActivationService(destinationId);
        IInactiveCtsObjectList newInactiveCtsObjectList = activationService
                .getInactiveCtsObjects(new NullProgressMonitor());
        List<String> newInactiveObjects = convertToStringList(newInactiveCtsObjectList);

        return checkForNewInactiveItems(newInactiveObjects, currentInactiveObjects); 
	}
	
    private boolean checkForNewInactiveItems(List<String> newInactiveObjects,
			ArrayList<String> currentInactiveObjects) {
		return newInactiveObjects.size() < currentInactiveObjects.size();
	}

	private List<String> convertToStringList(IInactiveCtsObjectList newInactiveCtsObjectList) {
        List<String> newObjects = new ArrayList<>();
        for (Iterator<IInactiveCtsObject> iterator = newInactiveCtsObjectList.getEntry().iterator(); iterator.hasNext();) {
            IInactiveCtsObject ctsObject =  iterator.next();
            if (ctsObject.hasObjectRef())
            {
                newObjects.add(ctsObject.getObject().getClass().getName());                
            }
            
        }
        return newObjects;
    }

}
