package abapci.activation;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Adapters;
import org.eclipse.ui.IEditorReference;

import com.sap.adt.tools.core.IAdtObjectReference;
import com.sap.adt.tools.core.ui.editors.IAdtFormEditor;

import abapci.GeneralProjectUtil;

public class AbapActivationExtractor implements IActivationExtractor {

	@Override
	public Activation extractFrom(IEditorReference editorReference) {
		Activation activation = null;

		if (!(editorReference.getEditor(false) instanceof IAdtFormEditor)) {
			return null;
		}
		IAdtFormEditor adtEditor = (IAdtFormEditor) editorReference.getEditor(false);
		IFile file = adtEditor.getModelFile();
		IAdtObjectReference adtObject = (IAdtObjectReference) Adapters.adapt((Object) file, IAdtObjectReference.class);
		if (adtObject == null) {
			return null;
		}

		if (adtObject.getPackageName() == null && adtObject.getType().equals("FUGR/FF")) {
			//URI parentUri = adtObject.getParentUri();
			// TODO how to get from function module parent URI to package?
			// needed to run unit tests for affected package only
		}

		if (adtObject != null) {
			IProject project = GeneralProjectUtil.getProject(editorReference.getEditor(false));

			activation = new Activation(editorReference.getName(), adtObject.getPackageName(), project,
					adtObject.getUri(), adtObject.getType());
		}

		return activation;
	}

}
