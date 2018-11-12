package abapci.activation;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.ui.IEditorReference;

import abapci.GeneralProjectUtil;

public class ProgLanguageFactorySelector {

	public ILanguageFactory determine(String uiAction, IEditorReference[] editorReferences) {

		ILanguageFactory languageFactory = new DefaultLanguageFactory();

		if (ActivationAction.ACTION_ABAP_SINGLE_ACTIVATION.equals(uiAction)
				|| ActivationAction.ACTION_ABAP_MULTI_ACTIVATION.equals(uiAction)) {
			languageFactory = new AbapLanguageFactory();
		} else {
			for (IEditorReference editorReference : editorReferences) {
				if (editorReference.isDirty()) {
					IProject project = GeneralProjectUtil.getProject(editorReference.getEditor(false));
					languageFactory = determine(project);
				}
			}
		}

		return languageFactory;
	}

	public ILanguageFactory determine(IProject project) {

		ILanguageFactory languageFactory = new DefaultLanguageFactory();

		try {
			if (project.hasNature(JavaCore.NATURE_ID)) {
				languageFactory = new JavaLanguageFactory();
			} else if (project.hasNature("com.sap.adt.abapnature")) {
				languageFactory = new AbapLanguageFactory();
			} else {
				languageFactory = new DefaultLanguageFactory();
			}
		} catch (CoreException e) {
			// in case the language could not be determined we stay with the default
			// language
		}

		return languageFactory;
	}

}
