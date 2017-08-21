package abapci.views.actions.ui;

import java.util.List;
import java.util.function.Predicate;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.PlatformUI;
import org.osgi.service.prefs.BackingStoreException;

import abapci.Domain.AbapPackageTestState;
import abapci.views.ViewModel;

public class DeleteAction extends Action {

	public DeleteAction(String label) {
		this.setText(label);
	}

	public void run() {

		ISelection selection = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.getSelection();
		AbapPackageTestState firstAbapPackageTestState = (AbapPackageTestState) ((StructuredSelection) selection).getFirstElement();

		List<AbapPackageTestState> viewerAbapPackageTestStates = ViewModel.INSTANCE.getPackageTestStates(); 

		Predicate<AbapPackageTestState> packageNamePredicate = p -> p.getPackageName().equals(firstAbapPackageTestState.getPackageName());
		viewerAbapPackageTestStates.removeIf(packageNamePredicate);
		
		ViewModel.INSTANCE.setPackageTestStates(viewerAbapPackageTestStates); 

	
		IEclipsePreferences preferences = ConfigurationScope.INSTANCE.getNode("packageNames");
		
		try 
		{
			preferences.remove(firstAbapPackageTestState.getPackageName());	
			preferences.sync();
		} catch (BackingStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
