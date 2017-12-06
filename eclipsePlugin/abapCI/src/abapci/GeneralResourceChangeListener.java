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
import abapci.jobs.RepeatingAUnitJob;
import abapci.views.ViewModel;

public class GeneralResourceChangeListener implements IResourceChangeListener {

	private SapConnection sapConnection;
	private boolean initialRun; 

	public GeneralResourceChangeListener() {
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

					if (!initialRun) 
					{
						RepeatingAUnitJob.triggerProcessing = true;
						initialRun = true; 						
					}
					else if (!activatedObjects.isEmpty()) {
						RepeatingAUnitJob.triggerProcessing = true;
						RepeatingAUnitJob.triggerPackages = activatedObjects.stream().map(item -> item.getPackagename())
								.distinct().collect(Collectors.<String>toList());
					}
				}
				StringBuilder infotextBuilder = new StringBuilder();
				infotextBuilder.append(
						"Last check: " + new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()));
				infotextBuilder.append("; " + RepeatingAUnitJob.triggerProcessing);
				ViewModel.INSTANCE.setOverallInfoline(infotextBuilder.toString());
			} catch (Exception ex) {
				// TODO exception handling
			}

		}
	}
}
