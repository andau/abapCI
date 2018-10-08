package abapci;

import java.util.HashMap;

import org.eclipse.core.commands.Category;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.CommandManager;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.expressions.EvaluationContext;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ISources;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.texteditor.ITextEditor;

import com.sap.adt.tools.abapsource.ui.internal.sources.cleanup.AbapSourceCleanupHandler;
import com.sap.adt.tools.abapsource.ui.sources.prettyprinter.PrettyPrintHandler;

import abapci.cleanup.AbapSourceCleanupHandlerCopy;

public class SourcecodeFormatHandler {

	private static final Object NO_FILTER_PREFIX = "<NO_FILTER>";

	public boolean isAutoformatEnabled(IEditorPart editor, String sourcecodePrefix) {
		boolean autoformat = false;

		IEditorPart editorToFormat = editor;

		if (editor instanceof MultiPageEditorPart) {
			editorToFormat = (IEditorPart) ((MultiPageEditorPart) editor).getSelectedPage();
		}

		if (editorToFormat instanceof ITextEditor) {
			ITextEditor ite = (ITextEditor) editorToFormat;
			IDocument doc = ite.getDocumentProvider().getDocument(ite.getEditorInput());

			autoformat = isDocumentStartingWithPrefix(doc, sourcecodePrefix);
		}

		return autoformat;

	}

	private boolean isDocumentStartingWithPrefix(IDocument doc, String sourcecodePrefix) {

		return (NO_FILTER_PREFIX.equals(sourcecodePrefix) || doc.get().startsWith("\"" + sourcecodePrefix)
				|| doc.get().startsWith("*" + sourcecodePrefix) || doc.get().startsWith("//" + sourcecodePrefix));
	}

	public void formatEditor(IEditorPart editorPart) {
		@SuppressWarnings("rawtypes")
		HashMap parametersMap = new HashMap();
		EvaluationContext evaluationContext = new EvaluationContext(null, new Object());
		evaluationContext.addVariable(ISources.ACTIVE_EDITOR_NAME, editorPart);

		CommandManager manager = new CommandManager();
		Category category = manager.getCategory("TBD");
		category.define("name", "description");
		Command command = manager.getCommand("commandId");
		command.define("name", "description", category);

		ExecutionEvent executionEvent = new ExecutionEvent(command, parametersMap, null, evaluationContext);

		try {
			PrettyPrintHandler prettyPrinterHandler = new PrettyPrintHandler();
			prettyPrinterHandler.execute(executionEvent);
		} catch (org.eclipse.core.commands.ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void deleteUnusedVariables(IEditorPart editorPart) {
		HashMap<String, String> parametersMap = new HashMap<>();
		EvaluationContext evaluationContext = new EvaluationContext(null, new Object());
		evaluationContext.addVariable(ISources.ACTIVE_EDITOR_NAME, editorPart);

		CommandManager manager = new CommandManager();
		Category category = manager.getCategory("TBD");
		category.define("name", "description");
		String scope = "all";
		Command command = manager.getCommand(AbapSourceCleanupHandler.COMMAND_ID + "." + scope);
		command.define(AbapSourceCleanupHandler.COMMAND_ID + "." + scope, "description", category);

		ExecutionEvent executionEvent = new ExecutionEvent(command, parametersMap, null, evaluationContext);

		try {
			AbapSourceCleanupHandlerCopy sourceCleanupHandler = new AbapSourceCleanupHandlerCopy();
			sourceCleanupHandler.execute(executionEvent);
		} catch (org.eclipse.core.commands.ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public IEditorPart autoformattableEditor(IEditorPart editor) {
		boolean autoformat = false;

		if (editor instanceof MultiPageEditorPart) {
			editor = (IEditorPart) ((MultiPageEditorPart) editor).getSelectedPage();
		}

		if (editor instanceof ITextEditor) {
			ITextEditor ite = (ITextEditor) editor;
			IDocument doc = ite.getDocumentProvider().getDocument(ite.getEditorInput());
			if (doc.get().startsWith("\"@autoformat")) {
				autoformat = true;
			}
		}

		return (autoformat) ? editor : null;

	}

}
