package abapci;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.ViewReference;

import com.sap.adt.project.AdtCoreProjectServiceFactory;

public class AbapProjectUtil {

	private AbapProjectUtil() {
	}

	public static IProject getCurrentProject() {

		IProject currentProject = null;

		try {

			IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			IEditorPart activeEditor = activePage.getActiveEditor();
			IWorkbenchPartReference partReference = activePage.getActivePartReference();
			if (!partReference.getClass().equals(ViewReference.class) && (activeEditor != null)) {
				currentProject = getCurrentProject(activeEditor);
			}

		} catch (Exception ex) {
			// return null when project could not be determined
		}

		return currentProject;
	}

	public static IProject getCurrentProject(IEditorPart editorPart) {
		IProject currentProject = null;

		if (editorPart != null) {
			IEditorInput input = editorPart.getEditorInput();
			currentProject = input.getAdapter(IProject.class);
			if (currentProject == null) {
				IResource resource = input.getAdapter(IResource.class);
				if (resource != null) {
					currentProject = resource.getProject();
				}
			}
		}

		return currentProject;

	}

	public static IProject getAbapProjectByProjectName(String projectname) {

		IProject[] availableProjects = AdtCoreProjectServiceFactory.createCoreProjectService()
				.getAvailableAdtCoreProjects();

		for (IProject project : availableProjects) {
			if (project.getName().equals(projectname)) {
				return project;
			}

		}

		return null;

	}

}