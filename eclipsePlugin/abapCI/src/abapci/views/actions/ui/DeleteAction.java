package abapci.views.actions.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Predicate;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.ui.PlatformUI;

public class DeleteAction extends Action {
	private TableViewer viewer;

	public DeleteAction(TableViewer viewer) {
		this.viewer = viewer;
	}

	public void run() {

		ISelection selection = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.getSelection();
		String packageName = ((StructuredSelection) selection).getFirstElement().toString();

		String[] currentPackages = (String[]) viewer.getInput();
		ArrayList<String> currentPackagesList = new ArrayList<String>(Arrays.asList(currentPackages));

		Predicate<String> packageNamePredicate = p -> p.equals(packageName);
		currentPackagesList.removeIf(packageNamePredicate);
		viewer.setInput(currentPackagesList.toArray(new String[0])); 
	}

}
