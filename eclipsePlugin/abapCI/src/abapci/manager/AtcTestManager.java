package abapci.manager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import com.sap.adt.atc.model.atcworklist.IAtcWorklist;

import abapci.domain.AbapPackageTestState;
import abapci.domain.GlobalTestState;
import abapci.domain.TestResultSummary;
import abapci.domain.TestState;
import abapci.handlers.AbapAtcHandler;
import abapci.handlers.AbapUnitHandler;
import abapci.views.ViewModel;

public class AtcTestManager {

	public boolean executeAllPackages() {
		List<AbapPackageTestState> packageTestStates = ViewModel.INSTANCE.getPackageTestStates();

		TestState overallTestState = TestState.UNDEF;

		IAtcWorklist atcWorklist = null;

		for (AbapPackageTestState packageTestState : packageTestStates) {
			Map<String, String> packageNames = new HashMap<String, String>();
			packageNames.put("1", packageTestState.getPackageName());

			try {
				atcWorklist = (IAtcWorklist) new AbapAtcHandler()
						.execute(new ExecutionEvent(null, packageNames, null, null));
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			String atcErrors = "Errors:" + atcWorklist.getObjects().getObject().size();

			packageTestState.setAtcState(atcErrors);

		}

		ViewModel.INSTANCE.setPackageTestStates(packageTestStates);

		return true;
	}
}
