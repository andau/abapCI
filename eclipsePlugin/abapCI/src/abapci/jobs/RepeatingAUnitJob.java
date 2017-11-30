package abapci.jobs;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import abapci.feature.FeatureProcessor;

public class RepeatingAUnitJob extends Job {

	// TODO Make thread save
	public static boolean scheduleNextJob;

	private boolean isRunning = true;
	private FeatureProcessor featureProcessor;

	public RepeatingAUnitJob() {
		super("Running abapCI");
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		schedule(1000);
		if (scheduleNextJob) {
			scheduleNextJob = false;

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			featureProcessor = new FeatureProcessor();
			featureProcessor.processEnabledFeatures();

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