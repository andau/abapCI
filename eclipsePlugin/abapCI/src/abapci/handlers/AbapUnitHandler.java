package abapci.handlers;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;

import com.sap.adt.communication.exceptions.CommunicationException;
import com.sap.adt.tools.abapsource.abapunit.AbapUnitTask;
import com.sap.adt.tools.abapsource.abapunit.IAbapUnitResult;
import com.sap.adt.tools.abapsource.abapunit.IAbapUnitService;
import com.sap.adt.tools.abapsource.abapunit.TestItem;
import com.sap.adt.tools.abapsource.abapunit.TestRunException;
import com.sap.adt.tools.abapsource.abapunit.services.IAdtServicesFactory;

import abapci.activation.Activation;
import abapci.testResult.TestResultSummary;
import abapci.testResult.TestResultSummaryFactory;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 *
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class AbapUnitHandler extends AbstractHandler implements IUnitHandler {

	private static final String ADT_OBJECT_NAME_PREFIX = "/sap/bc/adt/vit/wb/object_type/devck/object_name/";
	private static final String ABAP_CLASS_TYPE = "CLAS/OC";

	private final AdtServicePluginHelper adtServicePluginHelper;

	public AbapUnitHandler() {
		adtServicePluginHelper = new AdtServicePluginHelper();
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		String packageName = event.getParameter("1");
		return executePackage(null, packageName, new HashSet<Activation>());
	}

	@Override
	public TestResultSummary executePackage(IProject project, String packageName, Set<Activation> activations) {

		boolean flag = false;

		IAdtServicesFactory servicesFactory = adtServicePluginHelper.getServiceFactory();
		IAbapUnitService abapUnitService = servicesFactory.createAbapUnitService(project.getName(), flag);
		AbapUnitTask task = new AbapUnitTask(packageName);

		String testobjectUrl = ADT_OBJECT_NAME_PREFIX + packageName;
		TestItem itemObject = new TestItem(testobjectUrl, testobjectUrl);
		task.addTestItem(itemObject);

		/**
		 * Unit test level selection seems currently not supported ( at least with 7.50)
		 * - therefore its deactived for the moment int riskLevels =
		 * evaluateRiskLevels();
		 * task.setRiskLevels(AbapUnitTestRiskLevel.getAsEnum(riskLevels));
		 *
		 * int durations = evaluateDurations();
		 * task.setDurations(AbapUnitTestDuration.getAsEnum(durations));
		 */

		TestResultSummary unitTestResultSummary;

		try {
			IAbapUnitResult abapUnitResult = abapUnitService.executeUnitTests(task, false, packageName);
			unitTestResultSummary = TestResultSummaryFactory.create(project, packageName, abapUnitResult, activations);

			return unitTestResultSummary;

		} catch (TestRunException e) {
			if (e.getCause() instanceof CommunicationException) {
				unitTestResultSummary = TestResultSummaryFactory.createOffline(project, packageName);
			} else {
				unitTestResultSummary = TestResultSummaryFactory.createUndefined(project, packageName);
			}
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CommunicationException e) {
			e.printStackTrace();
			unitTestResultSummary = TestResultSummaryFactory.createUndefined(project, packageName);
		} catch (Exception ex) {
			unitTestResultSummary = TestResultSummaryFactory.createUndefined(project, packageName);
		}

		return unitTestResultSummary;
	}

	@Override
	public TestResultSummary executeObjects(IProject project, String packageName, Set<Activation> activations) {
		boolean flag = false;
		IAdtServicesFactory servicesFactory = adtServicePluginHelper.getServiceFactory();
		IAbapUnitService abapUnitService = servicesFactory.createAbapUnitService(project.getName(), flag);
		AbapUnitTask task = new AbapUnitTask(packageName);

		for (Activation activation : activations) {
			if (activation.getType().equals(ABAP_CLASS_TYPE)) {
				task.addTestItem(new TestItem(activation.getUri().toString(), activation.getUri().toString()));
			}
		}

		TestResultSummary unitTestResultSummary;

		try {
			IAbapUnitResult abapUnitResult = abapUnitService.executeUnitTests(task, false, packageName);
			unitTestResultSummary = TestResultSummaryFactory.create(project, packageName, abapUnitResult, activations);

			return unitTestResultSummary;

		} catch (TestRunException e) {
			if (e.getCause() instanceof CommunicationException) {
				unitTestResultSummary = TestResultSummaryFactory.createOffline(project, packageName);
			} else {
				unitTestResultSummary = TestResultSummaryFactory.createUndefined(project, packageName);
			}
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CommunicationException e) {
			e.printStackTrace();
			unitTestResultSummary = TestResultSummaryFactory.createUndefined(project, packageName);
		} catch (Exception ex) {
			unitTestResultSummary = TestResultSummaryFactory.createUndefined(project, packageName);
		}

		return unitTestResultSummary;
	}

}
