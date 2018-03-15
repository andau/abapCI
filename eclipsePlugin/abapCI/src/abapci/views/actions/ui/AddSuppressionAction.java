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

import abapci.domain.Suppression;
import abapci.lang.UiTexts;
import abapci.views.ViewModel;

public class AddSuppressionAction extends Action {
    
	
	public AddSuppressionAction(String label) {
		this.setText(label);
		this.setImageDescriptor(
				PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJ_ADD));

	}

	@Override
	public void run() {

		List<Suppression> suppressions = ViewModel.INSTANCE.getSuppressions(); 

		InputDialog suppressionDialog = new InputDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
				this.getText(), UiTexts.LABEL_LONG_ACTION_ADD_NEW_SUPPRESSION, "", null);

		if (suppressionDialog.open() == Window.OK) {
			suppressions.add(new Suppression(suppressionDialog.getValue()));
			ViewModel.INSTANCE.setSuppressions(suppressions); 
			
			IEclipsePreferences preferences = ConfigurationScope.INSTANCE.getNode("suppressions");
			preferences.put(suppressionDialog.getValue(), suppressionDialog.getValue());

			try {
				preferences.flush();
			} catch (BackingStoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
