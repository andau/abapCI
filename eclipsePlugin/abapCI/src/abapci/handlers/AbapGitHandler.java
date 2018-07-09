package abapci.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import com.sap.adt.project.AdtCoreProjectServiceFactory;
import com.sap.adt.projectexplorer.ui.internal.node.AbapRepositoryPackageNode;
import com.sap.adt.sapgui.ui.editors.AdtSapGuiEditorUtilityFactory;

import abapci.AbapCiPlugin;
import abapci.preferences.PreferenceConstants;

public class AbapGitHandler extends AbstractHandler {

	private static final String ABAP_GIT_TRANSACTION_NAME = "ZABAPGIT";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		ISelection selection = HandlerUtil.getCurrentSelection(event);
		if (!selection.isEmpty()) {
			if (selection instanceof TreeSelection) {
				if (!((TreeSelection) selection).isEmpty()
						&& ((TreeSelection)selection).getFirstElement() instanceof AbapRepositoryPackageNode) {
					String packageName = ((TreeSelection) selection).getFirstElement().toString();
					String username = "TBD";
					// call AbapGitPackageChanger
				}
			}
		}
		execute();

		return null;
	}

	public Object execute() {

		IPreferenceStore prefs = AbapCiPlugin.getDefault().getPreferenceStore();
		String projectName = prefs.getString(PreferenceConstants.PREF_DEV_PROJECT);
		IProject project = AdtCoreProjectServiceFactory.createCoreProjectService().findProject(projectName);

		String transactionName = ABAP_GIT_TRANSACTION_NAME;
		IEditorReference abapGitEditor = getAbapGitEditorReference();

		if (abapGitEditor == null) {
			AdtSapGuiEditorUtilityFactory.createSapGuiEditorUtility().openEditorAndStartTransaction(project,
					transactionName, true);
		} else {
			IWorkbench workbench = PlatformUI.getWorkbench();
			IWorkbenchWindow activeWorkbenchWindow = workbench.getActiveWorkbenchWindow();
			IWorkbenchPage activePage = activeWorkbenchWindow.getActivePage();
			activePage.activate(abapGitEditor.getPart(false));
		}

		return null;
	}

	private IEditorReference getAbapGitEditorReference() {

		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow activeWorkbenchWindow = workbench.getActiveWorkbenchWindow();
		IWorkbenchPage activePage = activeWorkbenchWindow.getActivePage();
		IEditorReference[] editorReferences = activePage.getEditorReferences();

		for (IEditorReference editorReference : editorReferences) {
			if (editorReference.getTitle().endsWith(ABAP_GIT_TRANSACTION_NAME))
			{
				return editorReference;
			}
		}
		return null;

	}

}
