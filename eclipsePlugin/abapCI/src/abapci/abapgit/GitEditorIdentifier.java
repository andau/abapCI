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

	@Override
	public boolean equals(Object other) {
		final GitEditorIdentifier otherIdentifier = (GitEditorIdentifier) other;

		return otherIdentifier == null ? false
				: equalProjects(otherIdentifier.getProject()) && equalPackageNames(otherIdentifier.getPackageName());
	}

	@Override
	public int hashCode() {

		int projectHashCode = 37;
		int packageHashCode = 39;

		if (project != null) {
			projectHashCode = project.hashCode();
		}

		if (packageName != null) {
			packageHashCode = packageName.hashCode();
		}

		return projectHashCode | packageHashCode;
	}

	private boolean equalPackageNames(String otherPackageName) {

		if (packageName == null) {
			return otherPackageName == null;
		} else {
			return packageName.equals(otherPackageName);
		}

	}

	private boolean equalProjects(IProject otherProject) {

		if (project == null && otherProject == null) {
			return true;
		} else if (project == null || otherProject == null) {
			return false;
		} else {
			return project.getName().equals(otherProject.getName());
		}
	}
}
