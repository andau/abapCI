package abapci.prettyPrinter;

import org.eclipse.ui.IEditorPart;

public interface IPrettyPrinter {
	public boolean isAutoformatEnabled(IEditorPart editor, String sourcecodePrefix);

	public void formatEditor(IEditorPart editor);

	public void deleteUnusedVariables(IEditorPart editor); 
}
