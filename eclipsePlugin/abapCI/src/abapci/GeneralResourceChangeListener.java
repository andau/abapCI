package abapci;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import abapci.connections.SapConnection;
import abapci.jobs.RepeatingAUnitJob;
import abapci.views.ViewModel;

public class GeneralResourceChangeListener implements IResourceChangeListener {

	private SapConnection sapConnection;

	public GeneralResourceChangeListener() {
		sapConnection = new SapConnection();
	}

	public void resourceChanged(IResourceChangeEvent event) {

		if (event.getType() == IResourceChangeEvent.POST_CHANGE) {
			IResourceDelta delta = event.getDelta();
			if (delta == null)
				return;

			IResourceDelta[] resourceDeltas = delta.getAffectedChildren(IResourceDelta.CHANGED);

			try {
				if (resourceDeltas.length > 0 && sapConnection.isConnected()
						&& sapConnection.unprocessedActivatedObjects()) {
					RepeatingAUnitJob.scheduleNextJob = true;
				}
				StringBuilder infotextBuilder = new StringBuilder();
				infotextBuilder.append(
						"Last check: " + new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()));
				ViewModel.INSTANCE.setOverallInfoline(infotextBuilder.toString());
			} catch (Exception ex) {
				// TODO excpetion handling
			}

		}
	}
}
