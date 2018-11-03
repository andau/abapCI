package abapci.activation;

import org.eclipse.core.resources.IProject;
import org.eclipse.ui.IEditorReference;

import abapci.AbapProjectUtil;

public class JavaActivationExtractor implements IActivationExtractor {

	@Override
	public Activation extractFrom(IEditorReference editorReference) {
		IProject project = AbapProjectUtil.getCurrentProject(editorReference.getEditor(false));
		return new Activation(editorReference.getName(), project.getName(), project, null, "JAVA");
	}

}
