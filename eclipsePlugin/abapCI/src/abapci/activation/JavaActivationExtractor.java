package abapci.activation;

import org.eclipse.core.resources.IProject;
import org.eclipse.ui.IEditorReference;

import abapci.GeneralProjectUtil;

public class JavaActivationExtractor implements IActivationExtractor {

	@Override
	public Activation extractFrom(IEditorReference editorReference) {
		IProject project = GeneralProjectUtil.getProject(editorReference.getEditor(false));
		return new Activation(editorReference.getName(), project.getName(), project, null, "JAVA");
	}

}
