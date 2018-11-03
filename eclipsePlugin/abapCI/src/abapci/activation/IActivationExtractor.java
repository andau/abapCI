package abapci.activation;

import org.eclipse.ui.IEditorReference;

public interface IActivationExtractor {

	Activation extractFrom(IEditorReference editorReference);
}
