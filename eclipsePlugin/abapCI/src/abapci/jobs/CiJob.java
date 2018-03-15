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

public class CiJob extends Job {

	private static CiJob instance;
	
	private List<String> triggerPackages;
	
	private Date triggerDate; 	
	private boolean immediateProcessing; 
	private boolean shortDelayProcessing; 
	private boolean longDelayProcessing; 
	
	private static final long SHORT_DELAY_PROCESSING_DELAY = 5000; 
	private static final long LONG_DELAY_PROCESSING_DELAY = 20000;


	private FeatureProcessor featureProcessor;

	private CiJob() {
		super("Running abapCI");
		triggerDate = new Date();		
		triggerPackages = ViewModel.INSTANCE.getPackageTestStates().stream().map(item -> item.getPackageName())
				.collect(Collectors.<String>toList()); 		
		featureProcessor = new FeatureProcessor(triggerPackages);
	}
	
	public static CiJob getInstance () {
	    if (CiJob.instance == null) {
	    	CiJob.instance = new CiJob ();
	    }
	    return CiJob.instance;
	  }

	@Override
	protected IStatus run(IProgressMonitor monitor) {

		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt(); 
			return Status.CANCEL_STATUS;
		}

		immediateProcessing = true; 
		shortDelayProcessing = true; 
		longDelayProcessing = true;
				
		if (evaluateAndResetProcessingFlags()) 
		{
			featureProcessor.setPackages(triggerPackages);
			featureProcessor.processEnabledFeatures();				
		}
		
		if (shortDelayProcessing || longDelayProcessing ) 
		{
			schedule(); 
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

	public void stop() {
	}
	
    public void start() 
    {
    	triggerDate = new Date();
    	schedule(); 
    }


	public void setTriggerPackages(List<String> triggerPackages) {
		this.triggerPackages = triggerPackages; 
	}

}