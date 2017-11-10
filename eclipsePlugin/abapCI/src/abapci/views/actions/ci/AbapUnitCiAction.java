package abapci.views.actions.ci;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import abapci.AbapCiPlugin;
import abapci.domain.AbapPackageTestState;
import abapci.domain.UnitTestResultSummary;
import abapci.handlers.AbapUnitHandler;
import abapci.result.TestResultSummaryFactory;
import abapci.views.ViewModel;

public class AbapUnitCiAction extends AbstractCiAction {

	public AbapUnitCiAction(String label, String tooltip) {
		this.setText(label);
		this.setToolTipText(tooltip);
		this.setImageDescriptor(AbapCiPlugin.getImageDescriptor("icons/aunit.png"));
	}

	@Override
	public void run() {

		UnitTestResultSummary unitTestResultSummary;
		List<AbapPackageTestState> packageTestStates = ViewModel.INSTANCE.getPackageTestStates();

		for (Iterator<Entry<String, String>> iter = getSelectedPackages().entrySet().iterator(); iter.hasNext();) {

			String packageName = iter.next().getValue();

			try {
				unitTestResultSummary = new AbapUnitHandler().executePackage(packageName);

			} catch (Exception ex) {
				unitTestResultSummary = TestResultSummaryFactory.createOffline(packageName);
			}


			for (AbapPackageTestState packageTestState : packageTestStates) {
				if (packageName == packageTestState.getPackageName()) {
					packageTestState.setAUnitInfo(unitTestResultSummary.getTestState().toString());

					String currentTime = new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime());
					packageTestState.setLastRun(currentTime);
				}
			}
		}

		ViewModel.INSTANCE.updatePackageTestStates();
	}
}
