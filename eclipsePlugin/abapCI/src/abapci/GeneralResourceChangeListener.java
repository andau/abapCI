package abapci;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;

import abapci.jobs.RepeatingAUnitJob;

public class GeneralResourceChangeListener implements IResourceChangeListener {

	public GeneralResourceChangeListener() {
	}

	public void resourceChanged(IResourceChangeEvent event) {
		int eventType = event.getType();
		switch (eventType) {

		case IResourceChangeEvent.POST_CHANGE:
			IResourceDelta delta = event.getDelta();
			if (delta == null)
				return;

			IResourceDelta[] resourceDeltas = delta.getAffectedChildren(IResourceDelta.CHANGED);

			if (resourceDeltas.length > 0) {
				RepeatingAUnitJob.ScheduleNextJob = true;
				// TODO add possibility to run tests only for affected packages
				// - resourceDeltas[i]....
			}

			break;
		default:
			return;
		}
	}
}
