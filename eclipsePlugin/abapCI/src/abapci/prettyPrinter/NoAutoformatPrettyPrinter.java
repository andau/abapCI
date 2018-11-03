package abapci.prettyPrinter;

import org.eclipse.ui.IEditorPart;

public class NoAutoformatPrettyPrinter implements IPrettyPrinter {

	@Override
	public boolean isAutoformatEnabled(IEditorPart editor, String sourcecodePrefix) {
		return false;
	}

	@Override
	public void formatEditor(IEditorPart editor) {
		// default pretty printer has nothing todo  		
	}

	@Override
	public void deleteUnusedVariables(IEditorPart editor) {
		// default pretty printer has nothing todo  		
	}

}
