package abapci.jobs;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.preference.IPreferenceStore;

import com.sap.adt.destinations.logon.AdtLogonServiceFactory;
import abapci.AbapCiPlugin;
import abapci.feature.FeatureProcessor;
import abapci.preferences.PreferenceConstants;

public class RepeatingAUnitJob extends Job {

	// TODO Make thread save
	public static boolean ScheduleNextJob;

	private boolean isRunning = true;
	private FeatureProcessor featureProcessor;

	public RepeatingAUnitJob() {

		super("Running abapCI");

	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		schedule(1000);
		if (ScheduleNextJob) {
			ScheduleNextJob = false;

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			IPreferenceStore prefs = AbapCiPlugin.getDefault().getPreferenceStore();
			String projectName = prefs.getString(PreferenceConstants.PREF_DEV_PROJECT);
			boolean loggedOn = AdtLogonServiceFactory.createLogonService().isLoggedOn(projectName);
			if (loggedOn) {
				featureProcessor = new FeatureProcessor();
				featureProcessor.processEnabledFeatures();
			}
		}

		return Status.OK_STATUS;
	}

	@Override
	public boolean shouldSchedule() {
		return isRunning;
	}

	public void stop() {
		isRunning = false;
	}

}