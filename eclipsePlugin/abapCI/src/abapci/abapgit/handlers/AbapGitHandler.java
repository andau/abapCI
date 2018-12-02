package abapci.abapgit.handlers;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import com.sap.adt.projectexplorer.ui.node.AbapRepositoryBaseNode;
import com.sap.adt.sapgui.ui.internal.editors.GuiEditorInput;

import abapci.AbapCiPluginHelper;
import abapci.GeneralProjectUtil;
import abapci.abapgit.GitEditorIdentifier;

public class AbapGitHandler extends AbstractHandler {

	private static final String ABAP_GIT_TRANSACTION_NAME = "ZABAPGIT";
	private static final String PARAMETER_PACKAGE_NAME_KEY = "p_package_name";
	final boolean PACKAGE_CHANGE_FEATURE_ENABLED = false;

	AbapCiPluginHelper abapCiPluginHelper;
	AbapGitHandlerHelper abapGitHandlerHelper;

	public AbapGitHandler() {
		abapCiPluginHelper = new AbapCiPluginHelper();
		abapGitHandlerHelper = new AbapGitHandlerHelper();
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		IProject project = null;
		String packagename = "";

		final ISelection selection = HandlerUtil.getCurrentSelection(event);
		if (!selection.isEmpty()) {
			if (selection instanceof TreeSelection) {
				if (!((TreeSelection) selection).isEmpty()
						&& ((TreeSelection) selection).getFirstElement() instanceof AbapRepositoryBaseNode) {
					final AbapRepositoryBaseNode packageNode = (AbapRepositoryBaseNode) ((TreeSelection) selection)
							.getFirstElement();

					project = packageNode.getProject();
					packagename = packageNode.getPackageName();

				} else if (!((TreeSelection) selection).isEmpty()
						&& ((TreeSelection) selection).getFirstElement() instanceof IProject) {
					project = (IProject) ((TreeSelection) selection).getFirstElement();
				}
			}
		}

		if (project == null && GeneralProjectUtil.getCurrentProject() != null)

		{
			project = GeneralProjectUtil.getCurrentProject();
		}

		execute(project, packagename);

		return null;
	}

	public Object execute(IProject project, String packagename) {

		final GitEditorIdentifier identifier = new GitEditorIdentifier(project, packagename);

		if (project == null) {
			showMissingProjectInfo(project);
		} else {

			final IEditorPart activeEditor = findActiveGitEditor(project, identifier);

			if (activeEditor != null) {
				reactivateEditor(activeEditor);
			} else {
				openNewEditor(identifier);
			}
		}

		return null;
	}

	private IEditorPart findActiveGitEditor(IProject project, final GitEditorIdentifier identifier) {
		IEditorPart activeEditor;
		if (abapGitHandlerHelper.getAbapGitFeature().isOnlyOneAbapGitTransactionActive()) {

			final IEditorReference abapGitEditor = getAbapGitEditorReference(project.getName());
			activeEditor = abapGitEditor != null ? abapGitEditor.getEditor(false) : null;

		} else {
			activeEditor = abapCiPluginHelper.getParticularOrGeneralGitEditor(identifier);
		}
		return activeEditor;
	}

	private void openNewEditor(final GitEditorIdentifier identifier) {

		final Map<String, String> params = new HashMap<>();
		params.put(PARAMETER_PACKAGE_NAME_KEY, identifier.getPackageName());

		final IEditorPart newEditor = abapGitHandlerHelper.getSapGuiEditorUtility().openEditorAndStartTransaction(
				identifier.getProject(), ABAP_GIT_TRANSACTION_NAME, true, params, params);

		abapCiPluginHelper.addGitEditor(identifier, newEditor);
	}

	private void reactivateEditor(IEditorPart activeEditor) {

		final IWorkbenchPage activePage = getActivePage();
		activePage.activate(activeEditor);
	}

	private IWorkbenchPage getActivePage() {
		final IWorkbenchWindow activeWorkbenchWindow = abapGitHandlerHelper.getWorkbench().getActiveWorkbenchWindow();
		return activeWorkbenchWindow.getActivePage();
	}

	private void showMissingProjectInfo(IProject project) {
		if (project == null) {
			MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Info",
					"For calling abapGit select an ABAP package in the package explorer");
		} else {
			MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Info",
					String.format("Project %s could not be determined as valid ABAP project", project));
		}

	}

	private IEditorReference getAbapGitEditorReference(String referenceProjectname) {

		final IEditorReference[] editorReferences = getActivePage().getEditorReferences();

		for (final IEditorReference editorReference : editorReferences) {
			if (editorReference.getTitle().endsWith(ABAP_GIT_TRANSACTION_NAME)) {
				try {
					final GuiEditorInput editorInput = (GuiEditorInput) editorReference.getEditorInput();
					final IProject project = editorInput.getProject();
					if (project.getName().equals(referenceProjectname)) {
						return editorReference;
					}

				} catch (final PartInitException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return null;

	}

}
