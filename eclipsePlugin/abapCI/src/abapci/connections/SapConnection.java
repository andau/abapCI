package abapci.connections;

import java.net.URI;
import java.util.ArrayList;
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

	public List<Activation> getInactiveObjects(IProject project) throws InactivatedObjectEvaluationException {

		try {
			final IActivationServiceFactory activationServiceFactory = AdtActivationPlugin.getDefault()
					.getActivationServiceFactory();
			final IActivationService activationService = activationServiceFactory
					.createActivationService(project.getName());
			final IInactiveCtsObjectList newInactiveCtsObjectList = activationService
					.getInactiveCtsObjects(new NullProgressMonitor());
			return convertToActivationObjectList(newInactiveCtsObjectList);
		} catch (final Exception ex) {
			throw new InactivatedObjectEvaluationException(ex);
		}

	}

	private List<Activation> convertToActivationObjectList(IInactiveCtsObjectList inactiveCtsObjectList) {
		final List<Activation> activationObjects = new ArrayList<>();
		for (final IInactiveCtsObject ctsObject : inactiveCtsObjectList.getEntry()) {
			if (ctsObject.hasObjectRef()) {
				final IAdtObjectReference ref = ctsObject.getObject().getRef();
				if (!ref.getName().equals("Z_BAPI_HE_VARIANT_CALC")) {
					activationObjects.add(new Activation(ref.getName(), ref.getPackageName(), null,
							URI.create(ref.getUri()), ref.getType()));
				}
			}

		}
		return activationObjects;
	}

}
