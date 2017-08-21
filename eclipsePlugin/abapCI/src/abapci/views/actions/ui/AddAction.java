package abapci.views.actions.ui;

import java.util.List;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.PlatformUI;
import org.osgi.service.prefs.BackingStoreException;

import abapci.Domain.AbapPackageTestState;
import abapci.lang.UiTexts;
import abapci.views.ViewModel;

public class AddAction extends Action {
    
	
	public AddAction(String label) {
		this.setText(label);
	}

	public void run() {

		List<AbapPackageTestState> viewerAbapPackageTestStates = ViewModel.INSTANCE.getPackageTestStates();

		InputDialog packageNameDialog = new InputDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
				this.getText(), UiTexts.LABEL_LONG_ACTION_ADD_NEW_PACKAGE, "", null);

		if (packageNameDialog.open() == Window.OK) {
			viewerAbapPackageTestStates.add(new AbapPackageTestState(packageNameDialog.getValue()));

			ViewModel.INSTANCE.setPackageTestStates(viewerAbapPackageTestStates);

			IEclipsePreferences preferences = ConfigurationScope.INSTANCE.getNode("packageNames");

			preferences.put(packageNameDialog.getValue(), packageNameDialog.getValue());

			try {
				preferences.sync();
			} catch (BackingStoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
