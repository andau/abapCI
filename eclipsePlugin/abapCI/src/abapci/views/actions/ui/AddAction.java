package abapci.views.actions.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.PlatformUI;
import org.osgi.service.prefs.BackingStoreException;

import abapci.Domain.AbapPackageTestState;
import abapci.views.ModelProvider;

public class AddAction extends Action {
	private TableViewer viewer;

	public AddAction(TableViewer viewer) {
		this.viewer = viewer;
	}

	public void run() {

		List<AbapPackageTestState> viewerAbapPackageTestStates = ModelProvider.INSTANCE.getPersons(); 
		
		InputDialog packageNameDialog = new InputDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
				"Add new package", "Adding a new ABAP package", "", null);

		if (packageNameDialog.open() == Window.OK) {
			viewerAbapPackageTestStates.add(new AbapPackageTestState(packageNameDialog.getValue(), "", ""));
			viewer.setInput(viewerAbapPackageTestStates);

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
