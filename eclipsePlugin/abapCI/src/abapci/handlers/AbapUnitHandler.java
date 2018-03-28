package abapci.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.preference.IPreferenceStore;

import com.sap.adt.communication.exceptions.CommunicationException;
import com.sap.adt.tools.abapsource.abapunit.AbapUnitTask;
import com.sap.adt.tools.abapsource.abapunit.IAbapUnitResult;
import com.sap.adt.tools.abapsource.abapunit.IAbapUnitService;
import com.sap.adt.tools.abapsource.abapunit.TestItem;
import com.sap.adt.tools.abapsource.abapunit.TestRunException;
import com.sap.adt.tools.abapsource.abapunit.services.AdtServicesPlugin;
import com.sap.adt.tools.abapsource.abapunit.services.IAdtServicesFactory;

import abapci.AbapCiPlugin;
import abapci.domain.UnitTestResultSummary;
import abapci.preferences.PreferenceConstants;
import abapci.result.TestResultSummaryFactory;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 *
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class AbapUnitHandler extends AbstractHandler {
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		String packageName = event.getParameter("1");
		return executePackage(packageName);
	}

	public UnitTestResultSummary executePackage(String packageName) {
		
		IPreferenceStore prefs = AbapCiPlugin.getDefault().getPreferenceStore();
		String destinationId = prefs.getString(PreferenceConstants.PREF_DEV_PROJECT);
		boolean flag = false;
		IAdtServicesFactory servicesFactory = AdtServicesPlugin.getDefault().getFactory();
		IAbapUnitService abapUnitService = servicesFactory.createAbapUnitService(destinationId, flag);
		AbapUnitTask task = new AbapUnitTask(packageName);
		String testobjectUrl = "/sap/bc/adt/vit/wb/object_type/devck/object_name/" + packageName;
		TestItem itemObject = new TestItem(testobjectUrl, testobjectUrl);
		task.addTestItem(itemObject);

		UnitTestResultSummary unitTestResultSummary;

		try {
			IAbapUnitResult abapUnitResult = abapUnitService.executeUnitTests(task, false, packageName);
			return TestResultSummaryFactory.create(packageName, abapUnitResult);
		} catch (TestRunException e) {
			if (e.getCause() instanceof CommunicationException) 
			{
				unitTestResultSummary = TestResultSummaryFactory.createOffline(packageName);
			}
			else 
			{
				unitTestResultSummary = TestResultSummaryFactory.createUndefined(packageName);				
			}
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CommunicationException e) {
			e.printStackTrace();
			unitTestResultSummary = TestResultSummaryFactory.createUndefined(packageName);							
		} catch (Exception ex) 
		{
			unitTestResultSummary = TestResultSummaryFactory.createUndefined(packageName);										
		}
		
		

		return unitTestResultSummary;
	}
}
