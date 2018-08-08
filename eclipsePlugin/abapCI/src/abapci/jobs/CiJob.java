package abapci.jobs;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import abapci.Exception.InactivatedObjectEvaluationException;
import abapci.connections.SapConnection;
import abapci.domain.ActivationObject;
import abapci.feature.FeatureProcessor;
import abapci.presenter.ContinuousIntegrationPresenter;

public class CiJob extends Job {

	private static CiJob instance;

	private List<String> triggerPackages;
	private String projectName;

	private Date triggerDate;
	private boolean immediateProcessing;
	private boolean shortDelayProcessing;
	private boolean longDelayProcessing;

	private static final long SHORT_DELAY_PROCESSING_DELAY = 2000;
	private static final long LONG_DELAY_PROCESSING_DELAY = 5000;

	private FeatureProcessor featureProcessor;

	private CiJob(ContinuousIntegrationPresenter continuousIntegrationPresenter) {
		super("Running abapCI");
		triggerDate = new Date();
		triggerPackages = continuousIntegrationPresenter.getAbapPackageTestStatesForCurrentProject().stream()
				.map(item -> item.getPackageName()).collect(Collectors.<String>toList());
		featureProcessor = new FeatureProcessor(continuousIntegrationPresenter, projectName, triggerPackages);
	}

	public static CiJob getInstance(ContinuousIntegrationPresenter continuousIntegrationPresenter) {
		if (CiJob.instance == null) {
			CiJob.instance = new CiJob(continuousIntegrationPresenter);
		}
		return CiJob.instance;
	}

	public void resetProcessingFlags() {
		immediateProcessing = true;
		shortDelayProcessing = true;
		longDelayProcessing = true;
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {

		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			return Status.CANCEL_STATUS;
		}

		int processingStep = evaluateAndResetProcessingFlags();
		if (processingStep > 0) {
			featureProcessor.setPackages(triggerPackages);
			featureProcessor.processEnabledFeatures();
		}

		if (shortDelayProcessing || longDelayProcessing) {
			schedule();
		}

		return Status.OK_STATUS;
	}

	private List<String> getActivatedPackages() {
		List<String> activatedPackages = new ArrayList<>();

		final SapConnection sapConnection = new SapConnection();

		try {
			List<ActivationObject> activatedObjects = sapConnection.getInactiveObjects(projectName);

			if (activatedObjects != null && activatedObjects.size() > 0) {
				activatedPackages = activatedObjects.stream().map(item -> item.packagename)
						.collect(Collectors.<String>toList());
			}
		} catch (InactivatedObjectEvaluationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return activatedPackages;
	}

	private int evaluateAndResetProcessingFlags() {
		int processingStep = -1;
		if (immediateProcessing) {
			immediateProcessing = false;
			processingStep = 1;
		}

		long timeSinceLastTrigger = new Date().getTime() - triggerDate.getTime();

		if (shortDelayProcessing && timeSinceLastTrigger > SHORT_DELAY_PROCESSING_DELAY) {
			shortDelayProcessing = false;
			processingStep = 2;
		}

		if (longDelayProcessing && timeSinceLastTrigger > LONG_DELAY_PROCESSING_DELAY) {
			longDelayProcessing = false;
			processingStep = 3;
		}

		return processingStep;
	}

	public void stop() {
	}

	public void start() {
		resetProcessingFlags();
		triggerDate = new Date();
		schedule();
	}

	public void setTriggerPackages(IProject project, List<String> triggerPackages) {
		this.projectName = project.getName();
		this.triggerPackages = triggerPackages;
	}

}