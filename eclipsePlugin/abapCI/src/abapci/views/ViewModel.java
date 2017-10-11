package abapci.views;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.osgi.service.prefs.BackingStoreException;

import abapci.domain.AbapPackageTestState;
import abapci.domain.GlobalTestState;
import abapci.domain.TestState;

public enum ViewModel {
	INSTANCE;

	Viewer viewer;
	Label lblOverallTestState;

	private List<AbapPackageTestState> abapPackageTestStates;
	private TestState overallTestState;

	private ViewModel() {

		abapPackageTestStates = new ArrayList<>();

		IEclipsePreferences packageNamePrefs = ConfigurationScope.INSTANCE.getNode("packageNames");

		try {
			for (String key : packageNamePrefs.keys()) {
				abapPackageTestStates.add(new AbapPackageTestState(packageNamePrefs.get(key, "default"),
						TestState.UNDEF.toString(), TestState.UNDEF.toString(), TestState.UNDEF.toString()));
			}
		} catch (BackingStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		overallTestState = TestState.UNDEF;

	}

	// TODO Dirty but working for the beginning - observableList should make
	// this obsolete in future
	public void setView(Viewer viewer) {
		INSTANCE.viewer = viewer;
	}

	public List<AbapPackageTestState> getPackageTestStates() {
		return abapPackageTestStates;
	}

	public void setPackageTestStates(List<AbapPackageTestState> abapPackageTestStates) {
		this.abapPackageTestStates = abapPackageTestStates;
		Runnable runnable = () -> viewer.setInput(abapPackageTestStates);
		Display.getDefault().asyncExec(runnable);
	}

	public void updatePackageTestStates() {
		Runnable runnable = () -> viewer.setInput(abapPackageTestStates);
		Display.getDefault().asyncExec(runnable);
	}

	public void setGlobalTestState(GlobalTestState globalTestState) {
		Runnable runnable = () -> {
			lblOverallTestState.setText(globalTestState.getTestStateOutputForDashboard());
			lblOverallTestState.setBackground(globalTestState.getColor());
		};
		Display.getDefault().asyncExec(runnable);
	}

	public TestState getOverallTestState() {
		return INSTANCE.overallTestState;
	}

	public void setLblOverallTestState(Label lblOverallTestState) {
		INSTANCE.lblOverallTestState = lblOverallTestState;
	}

}
