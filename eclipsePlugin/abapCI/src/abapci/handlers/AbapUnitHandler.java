package abapci.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.preference.IPreferenceStore;

import com.sap.adt.tools.abapsource.abapunit.AbapUnitTask;
import com.sap.adt.tools.abapsource.abapunit.IAbapUnitResult;
import com.sap.adt.tools.abapsource.abapunit.IAbapUnitService;
import com.sap.adt.tools.abapsource.abapunit.TestItem;
import com.sap.adt.tools.abapsource.abapunit.TestRunException;
import com.sap.adt.tools.abapsource.abapunit.services.AdtServicesPlugin;
import com.sap.adt.tools.abapsource.abapunit.services.IAdtServicesFactory;

import abapci.AbapCiPlugin;
import abapci.Domain.TestResultSummary;
import abapci.connections.ISapConnection;
import abapci.connections.SapConnection;
import abapci.connections.SapDemoConnection;
import abapci.preferences.PreferenceConstants;
import abapci.result.TestResultSummaryFactory;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * 
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class AbapUnitHandler extends AbstractHandler {

	private static final String TESTOBJECT_PREFIX = "/sap/bc/adt/vit/wb/object_type/devck/object_name/";

	@Override
	public TestResultSummary execute(ExecutionEvent event) throws ExecutionException {
		
		String packageName = event.getParameter("1");
		IPreferenceStore prefs = AbapCiPlugin.getDefault().getPreferenceStore(); 

		
		if (prefs.getBoolean(PreferenceConstants.PREF_ABAP_UNIT_USE_ODATA)) 
		{			
			return (TestResultSummary) executeWithOdataService(packageName, prefs);
		}
		else 
		{
	        return executeWithAdtTools(packageName, prefs);	
		}
	}

	private TestResultSummary executeWithAdtTools(String packageName, IPreferenceStore prefs) {
		
		String projectName = prefs.getString(PreferenceConstants.PREF_ABAP_UNIT_DEV_PROJECT);
		boolean enableDebugging = false;
		
		IAdtServicesFactory servicesFactory = AdtServicesPlugin.getDefault().getFactory();
		IAbapUnitService abapUnitService = servicesFactory.createAbapUnitService(projectName, enableDebugging);
		AbapUnitTask unitTask = new AbapUnitTask(packageName);
		String testobjectPath = TESTOBJECT_PREFIX + packageName;
		unitTask.addTestItem(new TestItem(testobjectPath, testobjectPath));
		
		try {
			boolean withCoverage = false; 
		    IAbapUnitResult abapUnitResult = abapUnitService.executeUnitTests(unitTask, withCoverage, packageName);
		    
		    return TestResultSummaryFactory.create(packageName, abapUnitResult); 
		    
		} catch (TestRunException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
		return TestResultSummaryFactory.createUndefined(packageName); 
	}

	private Object executeWithOdataService(String packageName, IPreferenceStore prefs) {
		String baseurl = prefs.getString(PreferenceConstants.PREF_ABAP_UNIT_URL);
		String username = prefs.getString(PreferenceConstants.PREF_ABAP_UNIT_USERNAME);
		String password = prefs.getString(PreferenceConstants.PREF_ABAP_UNIT_PASSWORD);

		String integrateAbapUnit = prefs.getString(PreferenceConstants.PREF_ABAP_UNIT_SIMULATE); 
		
		
		ISapConnection sapConnection; 
		if (integrateAbapUnit == "true")
		{
			sapConnection = new SapDemoConnection();
		}
		else 
		{			
			sapConnection = new SapConnection(baseurl, username, password);
		}
		
		return sapConnection.executeTests(packageName);
	}
	
}
