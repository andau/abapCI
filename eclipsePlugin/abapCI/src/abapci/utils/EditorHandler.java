package abapci.utils;

import java.net.URI;
import java.util.List;

import org.eclipse.core.resources.IProject;

import com.sap.adt.tools.core.model.adtcore.IAdtObjectReference;
import com.sap.adt.tools.core.model.util.AdtObjectReferenceAdapterFactory;
import com.sap.adt.tools.core.ui.navigation.AdtNavigationServiceFactory;

import abapci.domain.InvalidItem;

public class EditorHandler {

	public static void open(IProject project, URI uri) {
		final IAdtObjectReference objRef = AdtObjectReferenceAdapterFactory.create(uri.toString());
		AdtNavigationServiceFactory.createNavigationService().navigate(project, objRef, true);
	}

	public static void openInvalidItem(IProject currentProject, List<InvalidItem> invalidItems) {
		invalidItems.stream().forEach(item -> open(currentProject, item.getUriToError()));
	}

}
