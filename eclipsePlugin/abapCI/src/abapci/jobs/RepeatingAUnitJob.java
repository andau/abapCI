package abapci.jobs;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

import abapci.AbapCiPlugin;
import abapci.feature.FeatureDecision;
import abapci.manager.AUnitTestManager;
import abapci.manager.AtcTestManager;
import abapci.manager.JenkinsManager;
import abapci.preferences.PreferenceConstants;

public class RepeatingAUnitJob extends Job {

	// TODO Make thread save
	public static boolean ScheduleNextJob;

	private boolean isRunning = true;
	private AUnitTestManager aUnitTestManager;
	private JenkinsManager jenkinsManager;
	private AtcTestManager atcTestManager;

	FeatureDecision featureDecision; 
	
	public RepeatingAUnitJob() {

		super("Repeating AUnit Job");
		aUnitTestManager = new AUnitTestManager();
		jenkinsManager = new JenkinsManager();
		atcTestManager = new AtcTestManager();
		
		featureDecision = new FeatureDecision(); 

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

			processAllResources();
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

	private void processAllResources() {
		
		if (featureDecision.runUnitTestsOnSaveFeatureEnabled()) {
			boolean allUnitTestsOk = aUnitTestManager.executeAllPackages();
			
			if (featureDecision.changeColorOnFailedTestsFeatureEnabled()) {
				updateTheme(allUnitTestsOk);
			}

			if (allUnitTestsOk && featureDecision.runAtcOnSaveFeatureEnabled()) {
				atcTestManager.executeAllPackages();
			}
		}

		if (featureDecision.runJenkinsOnSaveFeatureEnabled()) {
			jenkinsManager.executeAllPackages();
		}
	}

	private void updateTheme(boolean allTestsOk) {
		if (allTestsOk) {
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					PlatformUI.getWorkbench().getThemeManager().setCurrentTheme("org.eclipse.ui.r30");
				}
			});
		} else {
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					PlatformUI.getWorkbench().getThemeManager().setCurrentTheme("com.abapCi.custom.theme");
				}
			});
		}
	}

}