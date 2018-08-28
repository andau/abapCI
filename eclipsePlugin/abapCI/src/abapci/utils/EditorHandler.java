package abapci.utils;

import java.net.URI;
import java.util.List;

import org.eclipse.core.resources.IProject;

import com.sap.adt.tools.core.model.adtcore.IAdtObjectReference;
import com.sap.adt.tools.core.model.util.AdtObjectReferenceAdapterFactory;
import com.sap.adt.tools.core.ui.navigation.AdtNavigationServiceFactory;

import abapci.domain.AbapPackageTestState;

public class EditorHandler {

	public static void open(IProject project, URI uri) {
		IAdtObjectReference objRef = AdtObjectReferenceAdapterFactory.create(uri.toString());
		AdtNavigationServiceFactory.createNavigationService().navigate(project, objRef, true);
	}

	public static void openUnit(IProject currentProject, List<AbapPackageTestState> packagesWithFailedTests) {
		packagesWithFailedTests.stream()
				.forEach(item -> open(currentProject, item.getFirstFailedUnitTest().getFirstStackEntry().getUri()));
	}

	public static void openAtc(IProject currentProject, List<AbapPackageTestState> packagesWithFailedTests) {
		packagesWithFailedTests.stream()
				.forEach(item -> open(currentProject, URI.create(item.getFirstFailedAtc().getDescription())));
	}

}
