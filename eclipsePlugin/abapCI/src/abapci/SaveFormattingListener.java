package abapci;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IExecutionListener;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.PlatformUI;

import abapci.feature.FeatureFacade;

public class SaveFormattingListener implements IExecutionListener {

	private FeatureFacade featureFacade;

	public SaveFormattingListener() {
		featureFacade = new FeatureFacade();
	}

	@Override
	public void notHandled(String arg0, NotHandledException arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void postExecuteFailure(String arg0, ExecutionException arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void postExecuteSuccess(String arg0, Object arg1) {
		// TODO Auto-generated method stub
	}

	@Override
	public void preExecute(String arg0, ExecutionEvent arg1) {

		if (("org.eclipse.ui.file.save".equals(arg0)
				|| "com.sap.adt.activation.ui.command.singleActivation".equals(arg0)
				|| "com.sap.adt.activation.ui.command.multiActivation".equals(arg0))
				&& featureFacade.getSourcecodeFormattingFeature().isActive()) {
			SourcecodeFormatHandler sourcecodeFormatHandler = new SourcecodeFormatHandler();

			IEditorReference[] editorReferences = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
					.getEditorReferences();

			String sourcecodePrefix = featureFacade.getSourcecodeFormattingFeature().getPrefix();
			for (IEditorReference editorReference : editorReferences) {

				if (sourcecodeFormatHandler.isAutoformatEnabled(editorReference.getEditor(true), sourcecodePrefix)) {
					sourcecodeFormatHandler.formatEditor(editorReference.getEditor(true));
				}
			}
		}
	}
}
