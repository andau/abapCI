package abapci.abapgit;

import org.eclipse.core.resources.IProject;

public class GitEditorIdentifier {

	private final IProject project;
	private final String packageName;

	public GitEditorIdentifier(IProject project, String packageName) {
		this.project = project;
		this.packageName = packageName;
	}

	public IProject getProject() {
		return project;
	}

	public String getPackageName() {
		return packageName;
	}

}
