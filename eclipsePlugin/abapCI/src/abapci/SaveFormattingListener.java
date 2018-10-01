package abapci;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IExecutionListener;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.Adapters;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.PlatformUI;

import com.sap.adt.tools.core.IAdtObjectReference;
import com.sap.adt.tools.core.ui.editors.IAdtFormEditor;

import abapci.activation.Activation;
import abapci.activation.ActivationDetector;
import abapci.feature.FeatureFacade;

public class SaveFormattingListener implements IExecutionListener {

	private FeatureFacade featureFacade;
	private ActivationDetector activationPool;

	public SaveFormattingListener() {
		featureFacade = new FeatureFacade();
		activationPool = ActivationDetector.getInstance();
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

			for (IEditorReference editorReference : editorReferences) {

				if (editorReference.isDirty()) {
					if (featureFacade.getSourcecodeFormattingFeature().isActive() && sourcecodeFormatHandler
							.isAutoformatEnabled(editorReference.getEditor(true), sourcecodePrefix)) {
						sourcecodeFormatHandler.formatEditor(editorReference.getEditor(true));
						if (featureFacade.getSourcecodeFormattingFeature().isCleanupVariablesEnabled()) {
							sourcecodeFormatHandler.deleteUnusedVariables(editorReference.getEditor(true));
						}
					}

					if ("org.eclipse.ui.file.save".equals(arg0)
							|| "com.sap.adt.activation.ui.command.singleActivation".equals(arg0)
							|| "com.sap.adt.activation.ui.command.multiActivation".equals(arg0)) {

						Activation activation = getCurrentAdtObject(editorReference);

						if (activation != null) {
							activationPool.registerModified(activation);
						}
					}
				}
			}

			if ("com.sap.adt.activation.ui.command.multiActivation".equals(arg0)) {
				activationPool.setActivatedEntireProject(AbapProjectUtil.getCurrentProject().getName());
			}

			if ("com.sap.adt.activation.ui.command.singleActivation".equals(arg0)) {
				IEditorPart activeEditor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
						.getActiveEditor();
				List<IEditorReference> activeEditorReferences = Arrays.asList(editorReferences).stream()
						.filter(item -> item != null && activeEditor.equals(item.getEditor(false)))
						.collect(Collectors.toList());

				if (!activeEditorReferences.isEmpty()) {
					Activation activation = getCurrentAdtObject(activeEditorReferences.get(0));
					activationPool.registerModified(activation);
					activationPool.setActivated(activeEditorReferences.get(0).getName());
				}
			}

		}

	}

	private Activation getCurrentAdtObject(IEditorReference editorReference) {

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
			URI parentUri = adtObject.getParentUri();
			// TODO how to get from function module parent URI to package?
			// needed to run unit tests for affected package only
		}

		if (adtObject != null) {
			String projectName = AbapProjectUtil.getCurrentProject(editorReference.getEditor(false)).getName();

			activation = new Activation(editorReference.getName(), adtObject.getPackageName(), projectName,
					adtObject.getUri(), adtObject.getType());
		}

		return activation;

	}
}
