package abapci;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IExecutionListener;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.PlatformUI;

import abapci.activation.Activation;
import abapci.activation.ActivationAction;
import abapci.activation.ActivationPool;
import abapci.activation.IActivationExtractor;
import abapci.activation.ILanguageFactory;
import abapci.activation.JavaActivationExtractor;
import abapci.activation.ProgLanguageFactorySelector;
import abapci.feature.FeatureFacade;
import abapci.prettyPrinter.IPrettyPrinter;

public class ActivationExecutionListener implements IExecutionListener {

	private FeatureFacade featureFacade;
	private ActivationPool activationPool;

	public ActivationExecutionListener() {
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

		if ((ActivationAction.ACTION_FILE_SAVE.equals(arg0)
				|| ActivationAction.ACTION_ABAP_SINGLE_ACTIVATION.equals(arg0)
				|| ActivationAction.ACTION_ABAP_MULTI_ACTIVATION.equals(arg0))) {

			IEditorReference[] editorReferences = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
					.getEditorReferences();
			
			ILanguageFactory languageFactory = getProgLanguageFactory(arg0, editorReferences); 
			
			IPrettyPrinter prettyPrinter = languageFactory.createPrettyPrinter();
			String sourcecodePrefix = featureFacade.getSourcecodeFormattingFeature().getPrefix();

			for (IEditorReference editorReference : editorReferences) {

				if (editorReference.isDirty()) {
					if (featureFacade.getSourcecodeFormattingFeature().isActive() && prettyPrinter
							.isAutoformatEnabled(editorReference.getEditor(true), sourcecodePrefix)) {
						prettyPrinter.formatEditor(editorReference.getEditor(true));
						if (featureFacade.getSourcecodeFormattingFeature().isCleanupVariablesEnabled()) {
							prettyPrinter.deleteUnusedVariables(editorReference.getEditor(true));
						}
					}

					if (ActivationAction.ACTION_FILE_SAVE.equals(arg0)
							|| ActivationAction.ACTION_ABAP_SINGLE_ACTIVATION.equals(arg0)
							|| ActivationAction.ACTION_ABAP_MULTI_ACTIVATION.equals(arg0)) {

						IActivationExtractor activationExtractor = languageFactory.createActivationExtractor(); 						
						Activation activation = activationExtractor.extractFrom(editorReference);

						if (activation != null) {
							activationPool.registerModified(activation);
							if (activationExtractor instanceof JavaActivationExtractor) 
							{
								activationPool.setActivated(activation.getObjectName());								
							}
						}
					}
				}
			}

			if (ActivationAction.ACTION_ABAP_MULTI_ACTIVATION.equals(arg0)) {
				activationPool.setActivatedEntireProject(AbapProjectUtil.getCurrentProject());
				activationPool.activationClickDetected();
			}

			if (ActivationAction.ACTION_ABAP_SINGLE_ACTIVATION.equals(arg0)) {
				IEditorPart activeEditor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
						.getActiveEditor();
				List<IEditorReference> activeEditorReferences = Arrays.asList(editorReferences).stream()
						.filter(item -> item != null && activeEditor.equals(item.getEditor(false)))
						.collect(Collectors.toList());

				if (!activeEditorReferences.isEmpty()) {
					IActivationExtractor activationExtractor = languageFactory.createActivationExtractor(); 						
					Activation activation = activationExtractor.extractFrom(activeEditorReferences.get(0));
					activationPool.registerModified(activation);
					activationPool.setActivated(activeEditorReferences.get(0).getName());
				}
				activationPool.activationClickDetected();
			}

		}

	}

	
	private ILanguageFactory getProgLanguageFactory(String uiAction, IEditorReference[] editorReferences) {
		ProgLanguageFactorySelector progLanguageFactorySelector = new ProgLanguageFactorySelector(); 
		return progLanguageFactorySelector.determine(uiAction, editorReferences);
	}

}
