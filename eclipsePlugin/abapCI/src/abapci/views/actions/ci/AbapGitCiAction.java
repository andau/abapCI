package abapci.views.actions.ci;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.preference.IPreferenceStore;

import com.sap.adt.project.AdtCoreProjectServiceFactory;
import com.sap.adt.sapgui.ui.editors.AdtSapGuiEditorUtilityFactory;

import abapci.AbapCiPlugin;
import abapci.preferences.PreferenceConstants;

public class AbapGitCiAction extends AbstractCiAction {	


	public AbapGitCiAction(String label, String tooltip) {
		this.setText(label);
		this.setToolTipText(tooltip);
		this.setImageDescriptor(AbapCiPlugin.getImageDescriptor("icons/git.ico"));
	}

	public void run() {
				
		IPreferenceStore prefs = AbapCiPlugin.getDefault().getPreferenceStore();
        String projectName = prefs.getString(PreferenceConstants.PREF_ABAP_UNIT_DEV_PROJECT);        
        IProject project = AdtCoreProjectServiceFactory.createCoreProjectService().findProject(projectName);      
        
        String transactionName = "ZABAPGIT";
        AdtSapGuiEditorUtilityFactory.createSapGuiEditorUtility()
        .openEditorAndStartTransaction(project, transactionName, true);

	}
}
