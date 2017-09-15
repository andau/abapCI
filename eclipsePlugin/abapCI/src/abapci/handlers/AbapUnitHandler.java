package abapci.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.preference.IPreferenceStore;

import abapci.AbapCiPlugin;
import abapci.connections.ISapConnection;
import abapci.connections.SapConnection;
import abapci.connections.SapDemoConnection;
import abapci.preferences.PreferenceConstants;

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

		IPreferenceStore prefs = AbapCiPlugin.getDefault().getPreferenceStore(); 

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
