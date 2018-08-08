package abapci.utils;

import java.net.URI;

import org.eclipse.core.resources.IProject;

import com.sap.adt.tools.core.model.adtcore.IAdtObjectReference;
import com.sap.adt.tools.core.model.util.AdtObjectReferenceAdapterFactory;
import com.sap.adt.tools.core.ui.navigation.AdtNavigationServiceFactory;

public class EditorHandler {

	public static void Open(IProject project, URI uri) {
		IAdtObjectReference objRef = AdtObjectReferenceAdapterFactory.create(uri.toString());
		AdtNavigationServiceFactory.createNavigationService().navigate(project, objRef, true);
	}

}
