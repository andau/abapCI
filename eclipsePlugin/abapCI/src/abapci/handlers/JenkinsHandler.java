package abapci.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.preference.IPreferenceStore;
import abapci.AbapCiPlugin;
import abapci.connections.JenkinsConnection;
import abapci.preferences.PreferenceConstants;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * 
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class JenkinsHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		String packageName = event.getParameter("1");

		IPreferenceStore prefs = AbapCiPlugin.getDefault().getPreferenceStore(); 

		String baseurl = prefs.getString(PreferenceConstants.PREF_JENKINS_URL);
		String username = prefs.getString(PreferenceConstants.PREF_JENKINS_USERNAME);
		String password = prefs.getString(PreferenceConstants.PREF_JENKINS_PASSWORD);
		String buildToken = prefs.getString(PreferenceConstants.PREF_JENKINS_BUILD_TOKEN); 

		JenkinsConnection jenkinsConnection = new JenkinsConnection(baseurl, username, password, buildToken);
		jenkinsConnection.runJob(packageName);
		return null;
	}
}
