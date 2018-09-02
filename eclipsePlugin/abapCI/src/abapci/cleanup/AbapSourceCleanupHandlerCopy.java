package abapci.cleanup;

import java.lang.reflect.InvocationTargetException;
import java.net.URI;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DocumentRewriteSession;
import org.eclipse.jface.text.DocumentRewriteSessionType;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentExtension4;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.text.edits.MultiTextEdit;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.progress.IProgressService;
import org.eclipse.ui.statushandlers.StatusManager;

import com.sap.adt.compatibility.exceptions.OutDatedClientException;
import com.sap.adt.destinations.model.IDestinationData;
import com.sap.adt.tools.abapsource.sources.AdtSourceServicesFactory;
import com.sap.adt.tools.abapsource.sources.IAdtSourceServicesFactory;
import com.sap.adt.tools.abapsource.sources.cleanup.IAbapSourceCleanupService;
import com.sap.adt.tools.abapsource.sources.cleanup.TextRangeComparator;
import com.sap.adt.tools.abapsource.ui.internal.sources.Messages;
import com.sap.adt.tools.abapsource.ui.sources.editors.AbstractAdtEditorHandler;
import com.sap.adt.tools.abapsource.ui.sources.editors.IAbapSourcePage;
import com.sap.adt.tools.core.project.IAbapProject;
import com.sap.adt.tools.core.ui.editors.IAdtFormEditor;
import com.sap.adt.tools.core.ui.internal.navigation.ITextNavigationSource;
import com.sap.adt.tools.core.urimapping.AdtUriMappingServiceFactory;
import com.sap.adt.tools.core.urimapping.IAdtUriMappingService;
import com.sap.adt.tools.core.urimapping.IUriMappingContext;
import com.sap.adt.tools.core.urimapping.UriMappingContext;

public class AbapSourceCleanupHandlerCopy extends AbstractAdtEditorHandler {
	private final IAdtSourceServicesFactory cleanUpFactory;
	private final IAdtUriMappingService uriMappingService;
	public static final String COMMAND_ID = "com.sap.adt.tools.abapsource.ui.cleanup.deleteUnusedVariables";

	AbapSourceCleanupHandlerCopy(IAdtSourceServicesFactory cleanUpFactory, IAdtUriMappingService uriMappingService) {
		this.cleanUpFactory = cleanUpFactory;
		this.uriMappingService = uriMappingService;
	}

	public AbapSourceCleanupHandlerCopy() {
		this.cleanUpFactory = AdtSourceServicesFactory.createInstance();
		this.uriMappingService = AdtUriMappingServiceFactory.createUriMappingService();
	}

	protected String getCommandId() {
		return "com.sap.adt.tools.abapsource.ui.cleanup.deleteUnusedVariables";
	}

	public Object execute(ExecutionEvent event) throws ExecutionException {
		try {
			IAbapSourcePage adtSourcePage = AbapSourceCleanupHandlerCopy
					.getAdtSourcePage(HandlerUtil.getActiveEditor((ExecutionEvent) event));
			Command command = event.getCommand();
			Assert.isNotNull((Object) command);
			AbapCleanupCommandInfoCopy cleanupCommandInfo = AbapCleanupCommandInfoCopy
					.findByCommandId((String) command.getId());
			Assert.isNotNull((Object) cleanupCommandInfo);
			String operation = cleanupCommandInfo.getOperation();
			String scope = cleanupCommandInfo.getScope();
			String jobName = command.getName();
			return this.execute(adtSourcePage, operation, scope, jobName);
		} catch (Exception e) {
			String message = e.getLocalizedMessage() != null ? e.getLocalizedMessage() : e.getMessage();
			throw new ExecutionException(message, (Throwable) e);
		}
	}

	private Object execute(IAbapSourcePage adtSourcePage, String operation, String scope, String jobName)
			throws CoreException {
		URI sourceObjectUri;
		String destination;
		IDocument document;
		block12: {
			IFile file;
			block11: {
				IProject project;
				block10: {
					block9: {
						Assert.isNotNull((Object) adtSourcePage);
						Assert.isNotNull((Object) operation);
						Assert.isNotNull((Object) scope);
						destination = this.getDestination((IEditorPart) adtSourcePage).getId();
						file = ((IFileEditorInput) adtSourcePage.getEditorInput()).getFile();
						project = file.getProject();
						document = adtSourcePage.getDocumentProvider()
								.getDocument((Object) adtSourcePage.getEditorInput());
						ITextNavigationSource textInfo = (ITextNavigationSource) adtSourcePage
								.getAdapter(ITextNavigationSource.class);
						sourceObjectUri = textInfo.getResourceUri(true);
						if (!(adtSourcePage.getSelectionProvider().getSelection() instanceof ITextSelection))
							break block9;
						ITextSelection t = (ITextSelection) adtSourcePage.getSelectionProvider().getSelection();
						if (!scope.equals("selection") || !t.getText().isEmpty())
							break block9;
						MessageDialog.openError((Shell) adtSourcePage.getSite().getShell(),
								(String) Messages.Cleanup_DialogTitle_xmsg,
								(String) Messages.Cleanup_SelectionNotDefined_xmsg);
						return null;
					}
					if (sourceObjectUri != null)
						break block10;
					return null;
				}
				IResource resource = this.uriMappingService
						.getPlatformResource((IUriMappingContext) new UriMappingContext(project), sourceObjectUri);
				if (resource != null)
					break block11;
				return null;
			}
			if (adtSourcePage.validateEdit(file))
				break block12;
			return null;
		}
		try {
			String oldSource = document.get();
			IProgressService progressService = (IProgressService) adtSourcePage.getSite()
					.getService(IProgressService.class);
			String newSource = this.runCleanupWithProgress(destination, oldSource, sourceObjectUri, operation, scope,
					progressService);
			if (newSource != null) {
				this.replaceTextInDocument(document, adtSourcePage, oldSource, newSource);
			}
		} catch (InterruptedException interruptedException) {
		} catch (Exception e) {
			Throwable cause = e;
			if (cause instanceof InvocationTargetException) {
				cause = ((InvocationTargetException) e).getCause();
			}
			IStatus status = cause instanceof CoreException ? ((CoreException) cause).getStatus()
					: (cause instanceof OutDatedClientException
							? new Status(2, "com.sap.adt.tools.abapsource.ui", cause.getLocalizedMessage(), cause)
							: new Status(4, "com.sap.adt.tools.abapsource.ui", cause.getLocalizedMessage(), cause));
			StatusManager.getManager().handle(status, 3);
		}
		return null;
	}

	private void replaceTextInDocument(IDocument document, IAbapSourcePage adtSourcePage, String source,
			String newSource) throws BadLocationException {
		StyledText textControl = adtSourcePage.getViewer().getTextWidget();
		textControl.setRedraw(false);
		DocumentRewriteSession rewriteSession = ((IDocumentExtension4) document)
				.startRewriteSession(DocumentRewriteSessionType.SEQUENTIAL);
		adtSourcePage.uninstallReconciler();
		try {
			MultiTextEdit edit = TextRangeComparator.createTextEdit((String) source, (String) newSource,
					(TextRangeComparator.CompareMode) TextRangeComparator.CompareMode.WORD);
			edit.apply(document, 2);
		} finally {
			textControl.setRedraw(true);
			((IDocumentExtension4) document).stopRewriteSession(rewriteSession);
			adtSourcePage.installReconciler();
		}
	}

	private String runCleanupWithProgress(final String destination, final String source, final URI sourceObjectUri,
			final String operation, final String scope, IProgressService progressService)
			throws InvocationTargetException, InterruptedException {
		final String[] newSource = new String[1];
		progressService.busyCursorWhile(new IRunnableWithProgress() {

			public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
				try {
					try {
						monitor.beginTask(Messages.Cleanup_OperationName_xmsg, -1);
						IAbapSourceCleanupService cleanupService = AbapSourceCleanupHandlerCopy.this.cleanUpFactory
								.createCleanupService(monitor, destination);
						newSource[0] = cleanupService.cleanup(monitor, source, new String[] { operation },
								sourceObjectUri, scope);
						if (monitor.isCanceled()) {
							throw new InterruptedException();
						}
					} catch (OperationCanceledException e) {
						InterruptedException interruptedException = new InterruptedException(e.getMessage());
						interruptedException.initCause((Throwable) e);
						throw interruptedException;
					} catch (InterruptedException e) {
						throw e;
					} catch (Exception e) {
						throw new InvocationTargetException(e);
					}
				} finally {
					monitor.done();
				}
			}
		});
		return newSource[0];
	}

	private IDestinationData getDestination(IEditorPart editor) throws CoreException {
		IFileEditorInput fileInput;
		IFile file;
		IEditorInput input = editor.getEditorInput();
		if (input instanceof IFileEditorInput && (file = (fileInput = (IFileEditorInput) input).getFile()) != null) {
			return ((IAbapProject) file.getProject().getAdapter(IAbapProject.class)).getDestinationData();
		}
		return null;
	}

	public static IAbapSourcePage getAdtSourcePage(IEditorPart editor) {
		if (editor instanceof IAbapSourcePage) {
			return (IAbapSourcePage) editor;
		}
		if (editor instanceof MultiPageEditorPart) {
			MultiPageEditorPart multiPageEditor = (MultiPageEditorPart) editor;
			IEditorPart activePage = (IEditorPart) multiPageEditor.getSelectedPage();
			if (activePage == null && multiPageEditor instanceof IAdtFormEditor) {
				int ap = multiPageEditor.getActivePage();
				IEditorPart ed = ((IAdtFormEditor) multiPageEditor).getEditor(ap);
				return AbapSourceCleanupHandlerCopy.getAdtSourcePage(ed);
			}
			return AbapSourceCleanupHandlerCopy.getAdtSourcePage(activePage);
		}
		return null;
	}

}