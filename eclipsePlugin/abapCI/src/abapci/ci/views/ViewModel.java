package abapci.ci.views;

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
import abapci.domain.SourcecodeState;
import abapci.domain.Suppression;
import abapci.domain.TestState;

@Deprecated
public enum ViewModel {
	INSTANCE;

	Viewer mainViewer;
	Viewer suppressionsViewer;
	Label lblOverallTestState;
	Label lblOverallInfoline;

	// private List<AbapPackageTestState> abapPackageTestStates;
	private List<Suppression> suppressions;

	private TestState overallTestState;
	private String overallInfoline;
	private GlobalTestState globalTestState;

	private ViewModel() {

		overallTestState = TestState.UNDEF;
		globalTestState = new GlobalTestState(SourcecodeState.UNDEF);

		suppressions = new ArrayList<>();
		IEclipsePreferences suppressionPrefs = ConfigurationScope.INSTANCE.getNode("suppressions");

		try {
			for (String key : suppressionPrefs.keys()) {
				suppressions.add(new Suppression(suppressionPrefs.get(key, "default")));
			}
		} catch (BackingStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// TODO Dirty but working for the beginning - observableList should make
	// this obsolete in future
	public void setMainViewer(Viewer viewer) {
		INSTANCE.mainViewer = viewer;
	}

	public void setSuppressViewer(Viewer viewer) {
		INSTANCE.suppressionsViewer = viewer;
	}

	public void setPackageTestStates(List<AbapPackageTestState> abapPackageTestStates) {
		// this.abapPackageTestStates = abapPackageTestStates;
		Runnable runnable = () -> mainViewer.setInput(abapPackageTestStates);
		Display.getDefault().asyncExec(runnable);
	}

	public void updatePackageTestStates() {
		// Runnable runnable = () -> mainViewer.setInput(abapPackageTestStates);
		// Display.getDefault().asyncExec(runnable);
	}

	public void setUnitState(TestState testState) {
		globalTestState.setUnitTeststate(testState);
	}

	public void setAtcState(TestState testState) {
		globalTestState.setAtcTeststate(testState);
	}

	public void setGlobalInfoline(String text) {
		lblOverallInfoline.setText(text);

	}

	public TestState getOverallTestState() {
		return INSTANCE.overallTestState;
	}

	public void setLblOverallTestState(Label lblOverallTestState) {
		INSTANCE.lblOverallTestState = lblOverallTestState;
	}

	public String getOverallInfoline() {
		return INSTANCE.overallInfoline;
	}

	public void setOverallInfoline(String infoline) {
		lblOverallInfoline.setText(infoline);
	}

	public List<Suppression> getSuppressions() {
		return suppressions;
	}

	public void setSuppressions(List<Suppression> suppressions) {
		this.suppressions = suppressions;

		Runnable runnable = () -> suppressionsViewer.setInput(suppressions);
		Display.getDefault().asyncExec(runnable);

	}

	public void setOverallLblInfoline(Label infoline) {
		INSTANCE.lblOverallInfoline = infoline;

	}

}
