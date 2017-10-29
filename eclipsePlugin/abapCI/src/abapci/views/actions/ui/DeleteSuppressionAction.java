package abapci.views.actions.ui;

import java.util.List;
import java.util.function.Predicate;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.osgi.service.prefs.BackingStoreException;

import abapci.domain.Suppression;
import abapci.views.ViewModel;

public class DeleteSuppressionAction extends Action {

	public DeleteSuppressionAction(String label) {
		this.setText(label);
		this.setImageDescriptor(
				PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_ELCL_REMOVE));

	}

	@Override
	public void run() {

		ISelection selection = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.getSelection();
		Suppression firstSuppression = (Suppression) ((StructuredSelection) selection).getFirstElement();

		List<Suppression> suppressions = ViewModel.INSTANCE.getSuppressions(); 

		Predicate<Suppression> suppressionPredicate = s -> s.getClassName().equals(firstSuppression.getClassName());
		suppressions.removeIf(suppressionPredicate);
		
		ViewModel.INSTANCE.setSuppressions(suppressions); 

	
		IEclipsePreferences preferences = ConfigurationScope.INSTANCE.getNode("suppressions");
		
		try 
		{
		    preferences.remove(firstSuppression.getClassName());	
		    preferences.sync();
		} catch (BackingStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
