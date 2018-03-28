package abapci;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;

import abapci.connections.SapConnection;
import abapci.domain.ActivationObject;
import abapci.jobs.CiJob;
import abapci.views.ViewModel;

public class GeneralResourceChangeListener implements IResourceChangeListener {

	private SapConnection sapConnection;
	private boolean initialRun;
	private CiJob job = CiJob.getInstance();

	public GeneralResourceChangeListener() 
	{
		sapConnection = new SapConnection();
		initialRun = false;
	}

	public void resourceChanged(IResourceChangeEvent event) {

		if (event.getType() == IResourceChangeEvent.POST_CHANGE) {
			IResourceDelta delta = event.getDelta();
			if (delta == null)
				return;

			IResourceDelta[] resourceDeltas = delta.getAffectedChildren(IResourceDelta.CHANGED);

			try {
				if (resourceDeltas.length > 0 && sapConnection.isConnected()) {

					List<ActivationObject> activatedObjects = sapConnection.unprocessedActivatedObjects();

					if (!initialRun) {
					    initialRun = true;
					    
						job.start();
					} else if (!activatedObjects.isEmpty()) {

						if (activatedObjects.stream().map(item -> item.getPackagename()).anyMatch(item -> item == null)) {
							job.setTriggerPackages(ViewModel.INSTANCE.getPackageTestStates().stream()
									.map(item -> item.getPackageName()).collect(Collectors.<String>toList()));
						}
						else 
						{
							job.setTriggerPackages(activatedObjects.stream().map(item -> item.getPackagename())
									.distinct().collect(Collectors.<String>toList()));							
						}
						job.start();
					}
				}

				StringBuilder infotextBuilder = new StringBuilder();
				infotextBuilder.append(
						"Last CI Check: " + new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()));
				ViewModel.INSTANCE.setOverallInfoline(infotextBuilder.toString());

			} catch (Exception ex) {
				// TODO exception handling
			}

		}
	}
}
