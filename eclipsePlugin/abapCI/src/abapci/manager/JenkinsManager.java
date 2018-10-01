package abapci.manager;

import java.util.List;

import org.eclipse.core.resources.IProject;

import abapci.presenter.ContinuousIntegrationPresenter;

public class JenkinsManager {

	public JenkinsManager(ContinuousIntegrationPresenter presenter, IProject project, List<String> packages) {
	}

	public void executeAllPackages() {

		/**
		 * for (AbapPackageTestState packageTestState : packageTestStates) { Map<String,
		 * String> packageNames = new HashMap<String, String>(); packageNames.put("1",
		 * packageTestState.getPackageName());
		 * 
		 * try { new JenkinsHandler().execute(new ExecutionEvent(null, packageNames,
		 * null, null)); } catch (ExecutionException e) { // TODO Auto-generated catch
		 * block e.printStackTrace(); }
		 * 
		 * String currentTime = new
		 * SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime());
		 * 
		 * packageTestState.setJenkinsInfo("last started: " + currentTime);
		 * 
		 * }
		 **/
	}
}
