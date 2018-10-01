package abapci.jobs;

import java.text.SimpleDateFormat;
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
import abapci.activation.Activation;
import abapci.activation.ActivationDetector;
import abapci.connections.SapConnection;
import abapci.feature.FeatureProcessor;
import abapci.presenter.ContinuousIntegrationPresenter;

public class CiJob extends Job {

	private static CiJob instance;

	private List<String> triggerPackages;
	private List<Activation> inactiveObjects;
	private List<Activation> currentInactiveObjects;
	private IProject project;

	private Date triggerDate;
	private boolean triggerProcessor;

	private static final long DELAYED_PROCESSING_TIMESPAN = 10000;

	private FeatureProcessor featureProcessor;
	private SapConnection sapConnection;

	private CiJob(ContinuousIntegrationPresenter continuousIntegrationPresenter) {
		super("AbapCI active");
		triggerPackages = continuousIntegrationPresenter.getAbapPackageTestStatesForCurrentProject().stream()
				.map(item -> item.getPackageName()).collect(Collectors.<String>toList());
		featureProcessor = new FeatureProcessor(continuousIntegrationPresenter, project, triggerPackages);

		resetProcessing();
	}

	public static CiJob getInstance(ContinuousIntegrationPresenter continuousIntegrationPresenter) {
		if (CiJob.instance == null) {
			CiJob.instance = new CiJob(continuousIntegrationPresenter);
			CiJob.instance.resetProcessing();
		}
		return CiJob.instance;
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {

		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			return Status.CANCEL_STATUS;
		}

		if (triggerProcessor) {
			SimpleDateFormat timeformat = new SimpleDateFormat("HH:mm:ss");
			System.out.println(String.format("Feature processor started at %s", timeformat.format(new Date())));
			featureProcessor.setPackagesAndObjects(triggerPackages, inactiveObjects);
			featureProcessor.processEnabledFeatures();
			triggerProcessor = false;
		}

		if (evaluateRerun()) {
			schedule();
		}

		return Status.OK_STATUS;
	}

	private boolean evaluateRerun() {

		try {
			List<Activation> newInactiveObjects = sapConnection.getInactiveObjects(project);
			if (newInactiveObjects.size() != currentInactiveObjects.size()) {
				triggerProcessor = true;
				currentInactiveObjects = newInactiveObjects;
				System.out.println(String.format("Number of inactive objects %d", currentInactiveObjects.size()));
			}
		} catch (InactivatedObjectEvaluationException e) {
			e.printStackTrace();
		}

		long timeSinceLastTrigger = new Date().getTime() - triggerDate.getTime();

		if (currentInactiveObjects.size() == 0) {
			ActivationDetector.getInstance().unregisterAllIncludedInJob();
		}

		return (timeSinceLastTrigger < DELAYED_PROCESSING_TIMESPAN);
	}

	public void stop() {
	}

	public void start() {
		schedule();
	}

	private void resetProcessing() {
		triggerDate = new Date();
		triggerProcessor = true;
		sapConnection = new SapConnection();
		currentInactiveObjects = new ArrayList<Activation>();
	}

	public void setTriggerPackages(IProject project, List<String> triggerPackages,
			List<Activation> activatedInactiveObjects) {
		this.project = project;
		this.triggerPackages = triggerPackages;
		this.inactiveObjects = activatedInactiveObjects;
	}

}