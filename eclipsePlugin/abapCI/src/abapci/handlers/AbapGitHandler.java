package abapci.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.IEditorPart;
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

		String projectname = null;

		ISelection selection = HandlerUtil.getCurrentSelection(event);
		if (!selection.isEmpty()) {
			if (selection instanceof TreeSelection) {
				if (!((TreeSelection) selection).isEmpty()
						&& ((TreeSelection) selection).getFirstElement() instanceof AbapRepositoryPackageNode) {
					AbapRepositoryPackageNode packageNode = (AbapRepositoryPackageNode) ((TreeSelection) selection)
							.getFirstElement();
					String packageName = packageNode.getPackageName();
					projectname = packageNode.getProject().getName();
					String username = "TBD";
					// call AbapGitPackageChanger
				}
			}
		}
		execute(projectname);

		return null;
	}

	public Object execute(String projectname) {

		if (projectname == null) {
			IPreferenceStore prefs = AbapCiPlugin.getDefault().getPreferenceStore();
			projectname = prefs.getString(PreferenceConstants.PREF_DEV_PROJECT);
		}

		IProject project = AdtCoreProjectServiceFactory.createCoreProjectService().findProject(projectname);

		if (project == null) {
			showMissingProjectInfo(projectname);
		} else {
			String transactionName = ABAP_GIT_TRANSACTION_NAME;
			IEditorReference abapGitEditor = getAbapGitEditorReference();

			if (abapGitEditor == null) {
				AdtSapGuiEditorUtilityFactory.createSapGuiEditorUtility()
						.openEditorAndStartTransaction(project, transactionName, true);
			} else {
				IWorkbench workbench = PlatformUI.getWorkbench();
				IWorkbenchWindow activeWorkbenchWindow = workbench.getActiveWorkbenchWindow();
				IWorkbenchPage activePage = activeWorkbenchWindow.getActivePage();
				activePage.activate(abapGitEditor.getPart(false));
			}
		}

		return null;
	}

	private void showMissingProjectInfo(String projectname) {
		if (projectname == null || projectname.trim().isEmpty()) {
			MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Info",
					"For calling abapGit select an ABAP package or set the ABAP development package in the ABAP CI settings");
		} else {
			MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Info",
					String.format("Project %s could not be determined as valid ABAP project", projectname));
		}

	}

	private IEditorReference getAbapGitEditorReference() {

		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow activeWorkbenchWindow = workbench.getActiveWorkbenchWindow();
		IWorkbenchPage activePage = activeWorkbenchWindow.getActivePage();
		IEditorReference[] editorReferences = activePage.getEditorReferences();

		for (IEditorReference editorReference : editorReferences) {
			if (editorReference.getTitle().endsWith(ABAP_GIT_TRANSACTION_NAME)) {
				return editorReference;
			}
		}
		return null;

	}

}
