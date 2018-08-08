package abapci.connections;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.NullProgressMonitor;

import com.sap.adt.activation.AdtActivationPlugin;
import com.sap.adt.activation.IActivationService;
import com.sap.adt.activation.IActivationServiceFactory;
import com.sap.adt.activation.model.inactiveObjects.IInactiveCtsObject;
import com.sap.adt.activation.model.inactiveObjects.IInactiveCtsObjectList;
import com.sap.adt.destinations.logon.AdtLogonServiceFactory;
import com.sap.adt.tools.core.model.adtcore.IAdtObjectReference;

import abapci.Exception.InactivatedObjectEvaluationException;
import abapci.domain.ActivationObject;

public class SapConnection {

	private List<ActivationObject> currentInactiveObjects;

	public SapConnection() {
		currentInactiveObjects = new ArrayList<>();
	}

	public boolean isConnected(String projectName) {
		return AdtLogonServiceFactory.createLogonService().isLoggedOn(projectName);
	}

	public List<ActivationObject> unprocessedActivatedObjects(String projectName)
			throws InactivatedObjectEvaluationException {
		return getInactiveObjects(projectName);

		/**
		 * if (checkForNewInactiveItems(newInactiveObjects, currentInactiveObjects)) {
		 * activatedObjects = currentInactiveObjects; } currentInactiveObjects =
		 * newInactiveObjects;
		 * 
		 * return activatedObjects;
		 **/
	}

	private boolean checkForNewInactiveItems(List<ActivationObject> newInactiveObjects,
			List<ActivationObject> currentInactiveObjects) {
		return newInactiveObjects.size() < currentInactiveObjects.size();
	}

	public List<ActivationObject> getInactiveObjects(String projectName) throws InactivatedObjectEvaluationException {

		try {
			List<ActivationObject> activatedObjects = new ArrayList<>();

			IActivationServiceFactory activationServiceFactory = AdtActivationPlugin.getDefault()
					.getActivationServiceFactory();
			IActivationService activationService = activationServiceFactory.createActivationService(projectName);
			IInactiveCtsObjectList newInactiveCtsObjectList = activationService
					.getInactiveCtsObjects(new NullProgressMonitor());
			return convertToStringList(newInactiveCtsObjectList);
		} catch (Exception ex) {
			throw new InactivatedObjectEvaluationException(ex);
		}

	}

	private List<ActivationObject> convertToStringList(IInactiveCtsObjectList inactiveCtsObjectList) {
		List<ActivationObject> activationObjects = new ArrayList<>();
		for (Iterator<IInactiveCtsObject> iterator = inactiveCtsObjectList.getEntry().iterator(); iterator.hasNext();) {
			IInactiveCtsObject ctsObject = iterator.next();
			if (ctsObject.hasObjectRef()) {
				IAdtObjectReference ref = ctsObject.getObject().getRef();
				if (!ref.getName().equals("Z_BAPI_HE_VARIANT_CALC")) {
					activationObjects.add(new ActivationObject(ref.getPackageName(), ref.getName()));
				}
			}

		}
		return activationObjects;
	}

}
