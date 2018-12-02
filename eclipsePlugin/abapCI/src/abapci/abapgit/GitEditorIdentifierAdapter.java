package abapci.abapgit;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;

import com.sap.adt.projectexplorer.ui.node.AbapRepositoryBaseNode;

public class GitEditorIdentifierAdapter {

	public GitEditorIdentifier adapt(ISelection selection) {
		IProject project = null;
		String packagename = "";

		if (selection != null && !selection.isEmpty()) {
			if (selection instanceof TreeSelection) {
				if (((TreeSelection) selection).getFirstElement() instanceof AbapRepositoryBaseNode) {
					final AbapRepositoryBaseNode packageNode = (AbapRepositoryBaseNode) ((TreeSelection) selection)
							.getFirstElement();

					project = packageNode.getProject();
					packagename = packageNode.getPackageName();

				} else if (((TreeSelection) selection).getFirstElement() instanceof IProject) {
					project = (IProject) ((TreeSelection) selection).getFirstElement();
				}
			}
		}

		return new GitEditorIdentifier(project, packagename);
	};

}
