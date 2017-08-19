package abapci.views.actions.ui;

import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.PlatformUI;
import org.osgi.service.prefs.BackingStoreException;

public class AddAction extends Action {
	private TableViewer viewer;

	public AddAction(TableViewer viewer) {
		this.viewer = viewer;
	}

	public void run() {

		String[] currentPackages = (String[]) viewer.getInput();
		ArrayList<String> currentPackagesList;

		if (currentPackages != null) {
			currentPackagesList = new ArrayList<String>(Arrays.asList(currentPackages));
		} else {
			currentPackagesList = new ArrayList<String>();
		}

		InputDialog packageNameDialog = new InputDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
				"Add new package", "Adding a new ABAP package", "", null);

		if (packageNameDialog.open() == Window.OK) {
			currentPackagesList.add(packageNameDialog.getValue());
			viewer.setInput(currentPackagesList.toArray(new String[1]));

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
