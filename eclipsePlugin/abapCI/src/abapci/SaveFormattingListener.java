package abapci;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IExecutionListener;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import abapci.activation.Activation;
import abapci.activation.ActivationPool;
import abapci.feature.FeatureFacade;

public class SaveFormattingListener implements IExecutionListener {

	private FeatureFacade featureFacade;
	private ActivationPool activationPool;

	public SaveFormattingListener() {
		featureFacade = new FeatureFacade();
		activationPool = ActivationPool.getInstance();
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
				|| "com.sap.adt.activation.ui.command.multiActivation".equals(arg0))) {
			SourcecodeFormatHandler sourcecodeFormatHandler = new SourcecodeFormatHandler();

			IEditorReference[] editorReferences = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
					.getEditorReferences();

			String sourcecodePrefix = featureFacade.getSourcecodeFormattingFeature().getPrefix();

			activationPool.unregisterAllIncludedInJob();

			for (IEditorReference editorReference : editorReferences) {

				if (editorReference.isDirty()) {
					if (featureFacade.getSourcecodeFormattingFeature().isActive() && sourcecodeFormatHandler
							.isAutoformatEnabled(editorReference.getEditor(true), sourcecodePrefix)) {
						sourcecodeFormatHandler.formatEditor(editorReference.getEditor(true));
					}

					String projectName = AbapProjectUtil.getCurrentProject(editorReference.getEditor(false)).getName();
					String packageName = ""; // would be awesome to get the package info here too, e.g. with ADT

					if ("org.eclipse.ui.file.save".equals(arg0)
							|| "com.sap.adt.activation.ui.command.singleActivation".equals(arg0)
							|| "com.sap.adt.activation.ui.command.multiActivation".equals(arg0)) {

						Activation activation = new Activation(editorReference.getName(), packageName, projectName);
						activationPool.registerModified(activation);
					}
				}

				if ("com.sap.adt.activation.ui.command.multiActivation".equals(arg0)) {
					activationPool.setActivatedEntireProject(AbapProjectUtil.getCurrentProject().getName());
				}
			}

			if ("com.sap.adt.activation.ui.command.singleActivation".equals(arg0)) {
				IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				IEditorPart activeEditor = activePage.getActiveEditor();
				Activation activation = new Activation(activeEditor.getTitle(), "",
						AbapProjectUtil.getCurrentProject().getName());
				activationPool.registerModified(activation);
				activationPool.setActivated(activeEditor.getTitle());

			}

		}
	}
}
