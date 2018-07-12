package abapci.abapgit;

import java.net.URI;

import org.eclipse.core.runtime.NullProgressMonitor;

import com.sap.adt.communication.destinations.AdtDestinationRegistryFactory;
import com.sap.adt.communication.destinations.IDestinationRegistry;
import com.sap.adt.communication.message.IMessageBody;
import com.sap.adt.communication.resources.AdtRestResourceFactory;
import com.sap.adt.communication.resources.IQueryParameter;
import com.sap.adt.communication.resources.IRestResource;
import com.sap.adt.communication.resources.IRestResourceFactory;
import com.sap.adt.communication.resources.QueryParameter;
import com.sap.adt.compatibility.discovery.AdtDiscoveryFactory;
import com.sap.adt.compatibility.discovery.IAdtDiscovery;
import com.sap.adt.compatibility.discovery.IAdtDiscoveryCollectionMember;

import abapci.Exception.AdtDiscoveryNotFoundException;
import abapci.views.actions.ci.IAdtAbapCiConstants;

public class AbapGitPackageChanger {

	public void changePackage(String projectname, String packagename) {

		String destination = projectname;
		IDestinationRegistry destRegistry = AdtDestinationRegistryFactory.getDestinationRegistry();
		String username = destRegistry.getEffectiveDestinationData(projectname).getUser();

		try {

			IRestResource resource = createRestResource(getResourceUri(destination), destination);

			IQueryParameter requestParamPackage = new QueryParameter("packagename", packagename);
			IQueryParameter requestParamUsername = new QueryParameter("username", username);

			IMessageBody messageBody = (IMessageBody) resource.post(null, IMessageBody.class, requestParamPackage,
					requestParamUsername);
		} catch (AdtDiscoveryNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private IRestResource createRestResource(URI resourceUri, String destination) {

		IRestResourceFactory resourceFactory = AdtRestResourceFactory.createRestResourceFactory();
		return resourceFactory.createResourceWithStatelessSession(resourceUri, destination);
	}

	private URI getResourceUri(String destination) throws AdtDiscoveryNotFoundException {
		IAdtDiscovery discovery = AdtDiscoveryFactory.createDiscovery(destination,
				URI.create(IAdtAbapCiConstants.DISCOVERY_URI));
		IAdtDiscoveryCollectionMember collectionMember = discovery.getCollectionMember(
				IAdtAbapCiConstants.TOC_RESOURCE_SCHEME, IAdtAbapCiConstants.TOC_TERM, new NullProgressMonitor());
		if (collectionMember == null) {
			throw new AdtDiscoveryNotFoundException();
		} else {
			return collectionMember.getUri();
		}
	}
}
