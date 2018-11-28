package abapci.handlers;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.MessageDialog;
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
import com.sap.adt.project.IAdtCoreProjectService;
import com.sap.adt.projectexplorer.ui.node.AbapRepositoryBaseNode;
import com.sap.adt.sapgui.ui.editors.AdtSapGuiEditorUtilityFactory;
import com.sap.adt.sapgui.ui.editors.IAdtSapGuiEditorUtility;
import com.sap.adt.sapgui.ui.internal.editors.GuiEditorInput;

import abapci.GeneralProjectUtil;
import abapci.abapgit.AbapGitPackageChanger;
import abapci.feature.FeatureFacade;

public class AbapGitHandler extends AbstractHandler {

	private static final String ABAP_GIT_TRANSACTION_NAME = "ZABAPGIT";
	final boolean PACKAGE_CHANGE_FEATURE_ENABLED = false;

	IAdtCoreProjectService coreProjectService;
	IWorkbench workbench;
	IAdtSapGuiEditorUtility sapGuiEditorUtility;

	public AbapGitHandler() {
		coreProjectService = AdtCoreProjectServiceFactory.createCoreProjectService();
		workbench = PlatformUI.getWorkbench();
		sapGuiEditorUtility = AdtSapGuiEditorUtilityFactory.createSapGuiEditorUtility();
	}

	public AbapGitHandler(boolean raw) {
		if (!raw) {
			new AbapGitHandler();
		}
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		String projectname = null;
		String packagename = "";

		final ISelection selection = HandlerUtil.getCurrentSelection(event);
		if (!selection.isEmpty()) {
			if (selection instanceof TreeSelection) {
				if (!((TreeSelection) selection).isEmpty()
						&& ((TreeSelection) selection).getFirstElement() instanceof AbapRepositoryBaseNode) {
					final AbapRepositoryBaseNode packageNode = (AbapRepositoryBaseNode) ((TreeSelection) selection)
							.getFirstElement();

					projectname = packageNode.getProject().getName();
					packagename = packageNode.getPackageName();

					// abapGit Package change is currently not yet implemented on ABAP side
					final FeatureFacade featureFacade = new FeatureFacade();
					if (featureFacade.getAbapGitPackageChangeFeature().isActive()) {

						final AbapGitPackageChanger packageChanger = new AbapGitPackageChanger();
						packageChanger.changePackage(projectname, packagename);
					}
				} else if (!((TreeSelection) selection).isEmpty()
						&& ((TreeSelection) selection).getFirstElement() instanceof IProject) {
					final IProject project = (IProject) ((TreeSelection) selection).getFirstElement();

					projectname = project.getName();
				}
			}
		}

		if (projectname == null && GeneralProjectUtil.getCurrentProject() != null)

		{
			projectname = GeneralProjectUtil.getCurrentProject().getName();
		}

		execute(projectname, packagename);

		return null;
	}

	public Object execute(String projectname, String packagename) {

		final IProject project = coreProjectService.findProject(projectname);

		if (project == null) {
			showMissingProjectInfo(projectname);
		} else {
			final IEditorReference abapGitEditor = getAbapGitEditorReference(project.getName());

			if (abapGitEditor == null) {
				final Map<String, String> params = new HashMap<>();
				params.put("PACKAGE_NAME", packagename);
				sapGuiEditorUtility.openEditorAndStartTransaction(project, ABAP_GIT_TRANSACTION_NAME, true, params,
						params);
			} else {
				final IWorkbenchWindow activeWorkbenchWindow = workbench.getActiveWorkbenchWindow();
				final IWorkbenchPage activePage = activeWorkbenchWindow.getActivePage();
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

		final IWorkbenchWindow activeWorkbenchWindow = workbench.getActiveWorkbenchWindow();
		final IWorkbenchPage activePage = activeWorkbenchWindow.getActivePage();
		final IEditorReference[] editorReferences = activePage.getEditorReferences();

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
