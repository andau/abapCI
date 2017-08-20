package abapci.views.actions.ui;

import java.util.List;
import java.util.function.Predicate;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.ui.PlatformUI;

import abapci.Domain.AbapPackageTestState;
import abapci.views.ModelProvider;

public class DeleteAction extends Action {
	private TableViewer viewer;

	public DeleteAction(TableViewer viewer) {
		this.viewer = viewer;
	}

	public void run() {

		ISelection selection = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.getSelection();
		AbapPackageTestState abapPackageTestState = (AbapPackageTestState) ((StructuredSelection) selection).getFirstElement();

		List<AbapPackageTestState> viewerAbapPackageTestStates = ModelProvider.INSTANCE.getPersons(); 

		Predicate<AbapPackageTestState> packageNamePredicate = p -> p.getPackageName().equals(abapPackageTestState.getPackageName());
		viewerAbapPackageTestStates.removeIf(packageNamePredicate);
		viewer.setInput(viewerAbapPackageTestStates); 
	}

}
