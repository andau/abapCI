package abapci.views.actions.ci;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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

	protected Map<String, String> getSelectedPackages() {

		ISelection selection = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getSelection();

		Map<String, String> packageNames = new HashMap<>();

		for (Iterator<?> iter = ((IStructuredSelection) selection).iterator(); iter.hasNext();) {

			String abapPackageName = ((AbapPackageTestState) iter.next()).getPackageName();

			packageNames.put(abapPackageName, abapPackageName);
		}

		return packageNames;
	}

	public String getLastResult() {
		return lastResult;
	}
}
