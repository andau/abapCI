package abapci.connections;

import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.NullProgressMonitor;

import com.sap.adt.activation.AdtActivationPlugin;
import com.sap.adt.activation.IActivationService;
import com.sap.adt.activation.IActivationServiceFactory;
import com.sap.adt.activation.model.inactiveObjects.IInactiveCtsObject;
import com.sap.adt.activation.model.inactiveObjects.IInactiveCtsObjectList;
import com.sap.adt.destinations.logon.AdtLogonServiceFactory;
import com.sap.adt.tools.core.model.adtcore.IAdtObjectReference;

import abapci.Exception.InactivatedObjectEvaluationException;
import abapci.activation.Activation;

public class SapConnection {

	public SapConnection() {
		new ArrayList<>();
	}

	public boolean isConnected(String projectName) {
		return AdtLogonServiceFactory.createLogonService().isLoggedOn(projectName);
	}

	public List<Activation> unprocessedActivatedObjects(IProject project) throws InactivatedObjectEvaluationException {
		return getInactiveObjects(project);
	}

	public List<Activation> getInactiveObjects(IProject project) throws InactivatedObjectEvaluationException {

		try {
			IActivationServiceFactory activationServiceFactory = AdtActivationPlugin.getDefault()
					.getActivationServiceFactory();
			IActivationService activationService = activationServiceFactory.createActivationService(project.getName());
			IInactiveCtsObjectList newInactiveCtsObjectList = activationService
					.getInactiveCtsObjects(new NullProgressMonitor());
			return convertToActivationObjectList(newInactiveCtsObjectList);
		} catch (Exception ex) {
			throw new InactivatedObjectEvaluationException(ex);
		}

	}

	private List<Activation> convertToActivationObjectList(IInactiveCtsObjectList inactiveCtsObjectList) {
		List<Activation> activationObjects = new ArrayList<>();
		for (Iterator<IInactiveCtsObject> iterator = inactiveCtsObjectList.getEntry().iterator(); iterator.hasNext();) {
			IInactiveCtsObject ctsObject = iterator.next();
			if (ctsObject.hasObjectRef()) {
				IAdtObjectReference ref = ctsObject.getObject().getRef();
				if (!ref.getName().equals("Z_BAPI_HE_VARIANT_CALC")) {
					activationObjects.add(new Activation(ref.getName(), ref.getPackageName(), "",
							URI.create(ref.getUri()), ref.getType()));
				}
			}

		}
		return activationObjects;
	}

}
