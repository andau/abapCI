package abapci.views.actions.ci;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.PlatformUI;

import abapci.domain.AbapPackageInfo;
import abapci.domain.AbapPackageTestState;
import abapci.domain.TestResult;
import abapci.views.ViewModel;

abstract class AbstractCiAction extends Action {
	private static final String NOT_YET_CALLED = "not yet called";

	private String lastResult = NOT_YET_CALLED;

	protected void updateViewerInput(AbapPackageInfo abapPackageInfo, AbapCiActionEnum ciActionType) {
		List<AbapPackageTestState> abapPackageTestStates = ViewModel.INSTANCE.getPackageTestStates();

		for (AbapPackageTestState abapPackageTestState : abapPackageTestStates) {
			if (abapPackageTestState.getPackageName() == abapPackageInfo.getPackageName()) {
				switch (ciActionType) {
				case JENKINS:
					abapPackageTestState.setJenkinsInfo("Jenkins executed");
					break;
				case ABAP_UNIT:
					abapPackageTestState.setAUnitInfo(new TestResult());
					break;
				case ABAP_ATC:
					abapPackageTestState.setAtcInfo(new TestResult());

					break;
				default:
					throw new UnsupportedOperationException("Not yet implemented");
				}
			}
		}

		ViewModel.INSTANCE.setPackageTestStates(abapPackageTestStates);

	}

	protected Map<String, String> getSelectedPackages() {

		ISelection selection = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getSelection();

		Map<String, String> packageNames = new HashMap<>();

		for (Iterator<?> iter = ((IStructuredSelection) selection).iterator(); iter.hasNext();) {

			String abapPackageName = ((AbapPackageTestState)iter.next()).getPackageName();

			packageNames.put(abapPackageName, abapPackageName);
		}

		return packageNames;
	}

	public String getLastResult() {
		return lastResult;
	}
}
