package abapci.abapgit.handlers;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Adapters;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.sap.adt.tools.core.IAdtObjectReference;
import com.sap.adt.tools.core.ui.editors.IAdtFormEditor;

import abapci.AbapCiPluginHelper;
import abapci.GeneralProjectUtil;
import abapci.abapgit.GitEditorIdentifier;
import abapci.abapgit.GitEditorIdentifierAdapter;
import abapci.abapgit.exception.NoAbapProjectException;
import abapci.abapgit.exception.ProjectIsNullException;
import abapci.utils.StringUtils;

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

		final ISelection selection = abapGitHandlerHelper.getCurrentSelection(event);

		GitEditorIdentifier identifier = getGitIdentifierFromSelection(selection);

		setToCurrentProjectIfProjectIsNull(identifier);
		if (identifier.getProject() == null || identifier.getPackageName() == null) {
			identifier = getGitIdentifierFromCurrentActiveEditor();
		}

		execute(identifier);

		return null;
	}

	private GitEditorIdentifier getGitIdentifierFromCurrentActiveEditor() {
		final IEditorPart activeEditor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.getActiveEditor();

		String packageName = StringUtils.EMPTY;
		if (activeEditor instanceof IAdtFormEditor) {
			final IAdtFormEditor adtEditor = (IAdtFormEditor) activeEditor;
			final IFile file = adtEditor.getModelFile();
			final IAdtObjectReference adtObject = Adapters.adapt((Object) file, IAdtObjectReference.class);
			packageName = adtObject.getPackageName();
		}

		final IProject currentProject = GeneralProjectUtil.getProject(activeEditor);
		return new GitEditorIdentifier(currentProject, packageName);

	}

	private void setToCurrentProjectIfProjectIsNull(GitEditorIdentifier identifier) {
		if (identifier.getProject() == null && GeneralProjectUtil.getCurrentProject() != null) {
			identifier = new GitEditorIdentifier(GeneralProjectUtil.getCurrentProject(), identifier.getPackageName());
		}
	}

	private GitEditorIdentifier getGitIdentifierFromSelection(final ISelection selection) {

		final GitEditorIdentifierAdapter gitEditorIdentifierAdapter = new GitEditorIdentifierAdapter();
		return gitEditorIdentifierAdapter.adapt(selection);
	}

	public Object execute(GitEditorIdentifier identifier) {

		try {

			checkValidAbapProject(identifier.getProject());

			final IEditorPart activeEditor = findActiveGitEditor(identifier);

			boolean editorReactivated = false;

			if (activeEditor != null) {
				editorReactivated = reactivateEditor(activeEditor);
			}
			if (!editorReactivated) {
				openNewEditor(identifier);
			}

		} catch (final ProjectIsNullException pne) {
			showProjectNullInfo();
		} catch (final NoAbapProjectException nae) {
			showMissingProjectInfo(identifier.getProject());
		}

		return null;

	}

	private boolean checkValidAbapProject(IProject project) throws ProjectIsNullException, NoAbapProjectException {

		if (project == null) {
			throw new ProjectIsNullException();
		}

		if (!abapGitHandlerHelper.getCoreProjectService().isAdtCoreProject(project)) {
			throw new NoAbapProjectException();
		}

		return true;
	}

	private IEditorPart findActiveGitEditor(final GitEditorIdentifier identifier) {
		return abapCiPluginHelper.getParticularOrGeneralGitEditor(identifier);
	}

	private void openNewEditor(final GitEditorIdentifier identifier) {

		final Map<String, String> params = new HashMap<>();
		params.put(PARAMETER_PACKAGE_NAME_KEY, identifier.getPackageName());

		final IEditorPart newEditor = abapGitHandlerHelper.getSapGuiEditorUtility().openEditorAndStartTransaction(
				identifier.getProject(), ABAP_GIT_TRANSACTION_NAME, true, params, params);

		abapCiPluginHelper.addGitEditor(identifier, newEditor);

	}

	private boolean reactivateEditor(IEditorPart activeEditor) {

		final IWorkbenchPage activePage = getActivePage();
		activePage.activate(activeEditor);
		return abapGitHandlerHelper.getActiveEditor().equals(activeEditor);
	}

	private IWorkbenchPage getActivePage() {
		final IWorkbenchWindow activeWorkbenchWindow = abapGitHandlerHelper.getWorkbench().getActiveWorkbenchWindow();
		return activeWorkbenchWindow.getActivePage();
	}

	private void showProjectNullInfo() {
		abapGitHandlerHelper.showMessage("For calling abapGit select an ABAP package in the package explorer");
	}

	private void showMissingProjectInfo(IProject project) {

		abapGitHandlerHelper
				.showMessage(String.format("Project %s could not be determined as valid ABAP project", project));

	}
}
