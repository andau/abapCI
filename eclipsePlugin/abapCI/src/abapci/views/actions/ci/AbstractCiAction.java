package abapci.views.actions.ci;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.PlatformUI;

import abapci.domain.AbapPackageTestState;
import abapci.presenter.ContinuousIntegrationPresenter;

abstract class AbstractCiAction extends Action {

	protected ContinuousIntegrationPresenter continuousIntegrationPresenter;
	private static final String NOT_YET_CALLED = "not yet called";
	private String lastResult = NOT_YET_CALLED;

	protected List<AbapPackageTestState> getSelectedAbapPackageTestStates() {

		List<AbapPackageTestState> abapPackageTestStates = new ArrayList<AbapPackageTestState>();

		ISelection selection = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getSelection();

		for (Iterator<?> iter = ((IStructuredSelection) selection).iterator(); iter.hasNext();) {

			abapPackageTestStates.add((AbapPackageTestState) iter.next());
		}

		return abapPackageTestStates;
	}

	public String getLastResult() {
		return lastResult;
	}
}
