package abapci.abapgit.handlers;

import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

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

}
