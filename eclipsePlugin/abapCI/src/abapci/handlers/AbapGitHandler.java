package abapci.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import com.sap.adt.project.AdtCoreProjectServiceFactory;
import com.sap.adt.projectexplorer.ui.node.AbapRepositoryBaseNode;
import com.sap.adt.sapgui.ui.editors.AdtSapGuiEditorUtilityFactory;
import com.sap.adt.sapgui.ui.internal.editors.GuiEditorInput;

import abapci.AbapCiPlugin;
import abapci.AbapProjectUtil;
import abapci.abapgit.AbapGitPackageChanger;
import abapci.feature.FeatureFacade;

public class AbapGitHandler extends AbstractHandler {

	private static final String ABAP_GIT_TRANSACTION_NAME = "ZABAPGIT";
	final boolean PACKAGE_CHANGE_FEATURE_ENABLED = false;

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		String projectname = null;

		ISelection selection = HandlerUtil.getCurrentSelection(event);
		if (!selection.isEmpty()) {
			if (selection instanceof TreeSelection) {
				if (!((TreeSelection) selection).isEmpty()
						&& ((TreeSelection) selection).getFirstElement() instanceof AbapRepositoryBaseNode) {
					AbapRepositoryBaseNode packageNode = (AbapRepositoryBaseNode) ((TreeSelection) selection)
							.getFirstElement();

					projectname = packageNode.getProject().getName();

					// abapGit Package change is currently not yet implemented on ABAP side
					FeatureFacade featureFacade = new FeatureFacade();
					if (featureFacade.getAbapGitPackageChangeFeature().isActive()) {

						String packagename = packageNode.getPackageName();

						AbapGitPackageChanger packageChanger = new AbapGitPackageChanger();
						packageChanger.changePackage(projectname, packagename);
					}
				} else if (!((TreeSelection) selection).isEmpty()
						&& ((TreeSelection) selection).getFirstElement() instanceof IProject) {
					IProject project = (IProject) ((TreeSelection) selection).getFirstElement();

					projectname = project.getName();
				}
			}
		}

		if (projectname == null && AbapProjectUtil.getCurrentProject() != null)

		{
			projectname = AbapProjectUtil.getCurrentProject().getName();
		}

		execute(projectname);

		return null;
	}

	public Object execute(String projectname) {

		if (projectname == null || projectname.equals("")) {
			IPreferenceStore prefs = AbapCiPlugin.getDefault().getPreferenceStore();
		}

		IProject project = AdtCoreProjectServiceFactory.createCoreProjectService().findProject(projectname);

		if (project == null) {
			showMissingProjectInfo(projectname);
		} else {
			String transactionName = ABAP_GIT_TRANSACTION_NAME;
			IEditorReference abapGitEditor = getAbapGitEditorReference(project.getName());

			if (abapGitEditor == null) {
				AdtSapGuiEditorUtilityFactory.createSapGuiEditorUtility().openEditorAndStartTransaction(project,
						transactionName, true);
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
					"For calling abapGit select an ABAP package in the package explorer");
		} else {
			MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Info",
					String.format("Project %s could not be determined as valid ABAP project", projectname));
		}

	}

	private IEditorReference getAbapGitEditorReference(String referenceProjectname) {

		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow activeWorkbenchWindow = workbench.getActiveWorkbenchWindow();
		IWorkbenchPage activePage = activeWorkbenchWindow.getActivePage();
		IEditorReference[] editorReferences = activePage.getEditorReferences();

		for (IEditorReference editorReference : editorReferences) {
			if (editorReference.getTitle().endsWith(ABAP_GIT_TRANSACTION_NAME)) {
				try {
					GuiEditorInput editorInput = (GuiEditorInput) editorReference.getEditorInput();
					IProject project = editorInput.getProject();
					if (project.getName().equals(referenceProjectname)) {
						return editorReference;
					}

				} catch (PartInitException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return null;

	}

}
