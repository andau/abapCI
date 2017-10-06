package abapci.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import com.sap.adt.atc.model.atcfinding.IAtcFinding;
import com.sap.adt.atc.model.atcfinding.IAtcFindingList;
import com.sap.adt.atc.model.atcobject.IAtcObject;
import com.sap.adt.atc.model.atcworklist.IAtcWorklist;

import abapci.domain.AbapPackageTestState;
import abapci.domain.TestState;
import abapci.handlers.AbapAtcHandler;
import abapci.views.ViewModel;

public class AtcTestManager {

	public TestState executeAllPackages() {
		TestState testState = TestState.UNDEF;

		List<AbapPackageTestState> packageTestStates = ViewModel.INSTANCE.getPackageTestStates();

		IAtcWorklist atcWorklist = null;

		for (AbapPackageTestState packageTestState : packageTestStates) {
			Map<String, String> packageNames = new HashMap<>();
			packageNames.put("1", packageTestState.getPackageName());

			try {
				atcWorklist = (IAtcWorklist) new AbapAtcHandler()
						.execute(new ExecutionEvent(null, packageNames, null, null));
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			int numErrors = 0;
			if (atcWorklist == null) {
				testState = TestState.UNDEF;

			} else {
				numErrors = countErrorsInWorklist(atcWorklist);
				testState = numErrors == 0 ? TestState.OK : TestState.NOK;
			}

			String atcUiOutputLabel = testState == TestState.NOK ? "Num Errors: " + numErrors : testState.toString();
			packageTestState.setAtcState(atcUiOutputLabel);

		}

		ViewModel.INSTANCE.setPackageTestStates(packageTestStates);

		return testState;

	}

	private int countErrorsInWorklist(IAtcWorklist atcWorklist) {
		int numErrors = 0;

		for (IAtcObject object : atcWorklist.getObjects().getObject()) {
			IAtcFindingList findingList = object.getFindings();
			for (IAtcFinding finding : findingList.getFinding()) {
				if (finding.getPriority() == 1) {
					numErrors = numErrors + 1;
				}
			}
		}
		return numErrors;
	}
}
