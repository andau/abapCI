package abapci.jobs;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import abapci.feature.FeatureProcessor;
import abapci.views.ViewModel;

public class RepeatingAUnitJob extends Job {

	// TODO Make thread save
	public static boolean triggerProcessing; 
	public static List<String> triggerPackages;
	
	private Date triggerDate; 	
	private boolean immediateProcessing; 
	private boolean shortDelayProcessing; 
	private boolean longDelayProcessing; 
	
	private static final long SHORT_DELAY_PROCESSING_DELAY = 10000; 
	private static final long LONG_DELAY_PROCESSING_DELAY = 60000; 

	private boolean isRunning = true;
	private FeatureProcessor featureProcessor;

	public RepeatingAUnitJob() {
		super("Running abapCI");
		triggerDate = new Date();
		triggerPackages = ViewModel.INSTANCE.getPackageTestStates().stream().map(item -> item.getPackageName())
				.collect(Collectors.<String>toList()); 		
		featureProcessor = new FeatureProcessor(triggerPackages);
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		schedule(1000);

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (triggerProcessing) {
			immediateProcessing = true; 
			shortDelayProcessing = true; 
			longDelayProcessing = true;
			triggerDate = new Date(); 
			
			triggerProcessing = false;
		}

			
		if (evaluateAndResetProcessingFlags()) 
		{
			featureProcessor.setPackages(triggerPackages);
			featureProcessor.processEnabledFeatures();				
		}

		return Status.OK_STATUS;
	}

	private boolean evaluateAndResetProcessingFlags() {
		boolean startProcessor = false; 
		if (immediateProcessing) 
		{
			immediateProcessing = false;
			startProcessor = true; 
		}
		
		long timeSinceLastTrigger = new Date().getTime() - triggerDate.getTime(); 
				
		if (shortDelayProcessing && timeSinceLastTrigger > SHORT_DELAY_PROCESSING_DELAY)
		{
			shortDelayProcessing = false; 
			startProcessor = true; 
		}

		if (longDelayProcessing && timeSinceLastTrigger > LONG_DELAY_PROCESSING_DELAY)
		{
			longDelayProcessing = false; 
			startProcessor = true; 
		}

		return startProcessor; 
	}

	@Override
	public boolean shouldSchedule() {
		return isRunning;
	}

	public void stop() {
		isRunning = false;
	}

}