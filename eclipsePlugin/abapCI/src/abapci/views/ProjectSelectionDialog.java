package abapci.views;

import java.util.ArrayList;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;

import com.sap.adt.project.AdtCoreProjectServiceFactory;
import com.sap.adt.tools.core.internal.AbapProjectService;

import abapci.AbapCiPlugin;
import abapci.preferences.PreferenceConstants;



class ProjectSelectionDialog extends ElementListSelectionDialog {

	private static final String DIALOG_HEADER = "ABAP development system selection";
	private static final String DIALOG_MESSAGE_LINE1 = "Please select your ABAP development system.";
	private static final String DIALOG_MESSAGE_LINE2 = "The current setting <%s> seems not be a valid ABAP project.";
	
	ProjectSelectionDialog(Shell parent, LabelProvider labelProvider) {
		super(parent, labelProvider);
		init(); 
	}
	private void init() {

		IPreferenceStore prefs  = AbapCiPlugin.getDefault().getPreferenceStore(); 
		String actualDevProject = prefs.getString(PreferenceConstants.PREF_DEV_PROJECT);
		this.setTitle(DIALOG_HEADER);
		StringBuilder messageBuilder = new StringBuilder(); 
		messageBuilder.append(DIALOG_MESSAGE_LINE1 + "\n"); 
		messageBuilder.append(String.format(DIALOG_MESSAGE_LINE2, actualDevProject));
		this.setMessage(messageBuilder.toString());
		
	    IProject[] availableProjects = AdtCoreProjectServiceFactory.createCoreProjectService().getAvailableAdtCoreProjects();         
	    ArrayList<String> abapProjectNames = new ArrayList<>(); 
	    
	    for (IProject project : availableProjects)
        {
        	if (AbapProjectService.getInstance().isAbapProject(project))  
            {
        		abapProjectNames.add(project.getName()); 
        	}
        } 
	    
	    if (abapProjectNames.isEmpty()) 
	    {
	    	abapProjectNames.add("No ABAP project found"); 
	    }
        
		this.setElements(abapProjectNames.toArray());
		
		
	}

}