package abapci.views.actions.ui;

import java.util.List;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.osgi.service.prefs.BackingStoreException;

import abapci.domain.ColoredProject;
import abapci.domain.UiColor;
import abapci.lang.UiTexts;
import abapci.views.ViewModel;

public class AddColoredProjectAction extends Action {
    
	
	public AddColoredProjectAction(String label) {
		this.setText(label);
		this.setImageDescriptor(
				PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJ_ADD));

	}

	@Override
	public void run() {

		//TODO 
		List<ColoredProject> coloredProjects = ViewModel.INSTANCE.getColoredProjects();  

		InputDialog coloredProjectDialog = new InputDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
				this.getText(), UiTexts.LABEL_LONG_ACTION_ADD_NEW_SUPPRESSION, "", null);

		if (coloredProjectDialog.open() == Window.OK) {
			coloredProjects.add(new ColoredProject(coloredProjectDialog.getValue(), UiColor.BLUE));
			ViewModel.INSTANCE.setColoredProjects(coloredProjects); 
			
			IEclipsePreferences preferences = ConfigurationScope.INSTANCE.getNode("coloredProjects");
			preferences.put(coloredProjectDialog.getValue(), UiColor.BLUE.toString());

			try {
				preferences.flush();
			} catch (BackingStoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
