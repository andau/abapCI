package abapci.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import abapci.Activator;
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

		IEclipsePreferences prefs = InstanceScope.INSTANCE.getNode(Activator.PLUGIN_ID);

		String baseurl = prefs.get(PreferenceConstants.PREF_JENKINS_URL, "");
		String username = prefs.get(PreferenceConstants.PREF_JENKINS_USERNAME, "");
		String password = prefs.get(PreferenceConstants.PREF_JENKINS_PASSWORD, "");

		JenkinsConnection jenkinsConnection = new JenkinsConnection(baseurl, username, password);
		jenkinsConnection.runJob(packageName);
		return null;
	}
}
