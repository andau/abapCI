package abapci.activation;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.core.resources.IProject;

public class ActivationPool {

	private static ActivationPool instance;
	private final List<Activation> activations;
	private boolean unprocessedActivationClick;

	private ActivationPool() {
		activations = new ArrayList<>();
	}

	public static ActivationPool getInstance() {
		if (instance == null) {
			instance = new ActivationPool();
		}
		return instance;
	}

	public void registerModified(Activation activation) {
		activations.add(activation);
	};

	public void setActivated(String objectName) {
		for (Activation activation : activations) {
			if (activation != null && activation.getObjectName().equals(objectName)) {
				activation.setActivated();
			}
		}
	}

	public void setActivatedEntireProject(IProject project) {
		for (Activation activation : activations) {
			if (activation.getProject().equals(project)) {
				activation.setActivated();
			}
		}
	}

	public List<Activation> findActiveActivationsAssignedToProject() {

		return findAllActive().stream()
				.filter(item -> item.getPackageName() != null && !item.getPackageName().isEmpty())
				.collect(Collectors.toList());
	}

	public List<Activation> findAllActiveOrIncludedInJob() {
		return activations.stream()
				.filter(item -> item.getPackageName() != null && item.isActivated() || item.isIncludedInJob())
				.collect(Collectors.<Activation>toList());
	}

	public List<Activation> findAllActive() {
		return activations.stream().filter(item -> item.isActivated()).collect(Collectors.<Activation>toList());
	}

	public void unregisterAllActivated() {
		activations.removeIf(item -> item == null || item.isActivated());
	}

	public void changeActivedToIncludedInJob() {
		for (Activation activation : activations) {
			if (activation.isActivated()) {
				activation.setIncludedInJob();
			}
		}
	}

	public void activationClickDetected() {
		unprocessedActivationClick = true;
	}

	public boolean hasUnprocessedActivationClicks() {
		return unprocessedActivationClick;
	}

	public void resetUnprocessedActivationClicks() {
		unprocessedActivationClick = false;
	}

	public IProject getCurrentProject() {
		return findAllActiveOrIncludedInJob().size() > 0 ? findAllActiveOrIncludedInJob().get(0).getProject() : null;
	}

}
