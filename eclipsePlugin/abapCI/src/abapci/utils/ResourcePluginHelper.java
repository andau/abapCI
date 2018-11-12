package abapci.utils;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;

public class ResourcePluginHelper {
	public IProject[] getProjects() {
		return ResourcesPlugin.getWorkspace().getRoot().getProjects();
	}
}
