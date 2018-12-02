package abapci.abapgit.handlers;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import com.sap.adt.project.AdtCoreProjectServiceFactory;
import com.sap.adt.project.IAdtCoreProjectService;
import com.sap.adt.sapgui.ui.editors.AdtSapGuiEditorUtilityFactory;
import com.sap.adt.sapgui.ui.editors.IAdtSapGuiEditorUtility;

import abapci.feature.FeatureFacade;
import abapci.feature.activeFeature.AbapGitFeature;

public class AbapGitHandlerHelper {

	public IAdtSapGuiEditorUtility getSapGuiEditorUtility() {
		return AdtSapGuiEditorUtilityFactory.createSapGuiEditorUtility();
	}

	public IWorkbench getWorkbench() {
		return PlatformUI.getWorkbench();
	}

	public AbapGitFeature getAbapGitFeature() {
		final FeatureFacade featureFacade = new FeatureFacade();
		return featureFacade.getAbapGitFeature();
	}

	public ISelection getCurrentSelection(ExecutionEvent event) {
		return HandlerUtil.getCurrentSelection(event);
	}

	public IAdtCoreProjectService getCoreProjectService() {
		return AdtCoreProjectServiceFactory.createCoreProjectService();
	}

	public void showMessage(String message) {
		MessageDialog.openInformation(getWorkbench().getActiveWorkbenchWindow().getShell(), "Info", message);

	}

}
