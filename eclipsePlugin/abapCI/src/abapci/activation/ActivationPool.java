package abapci.activation;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import abapci.domain.ActivationObject;

public class ActivationPool {

	private static ActivationPool instance;
	private List<Activation> activations;
	private List<ActivationObject> inactiveObjects;

	private ActivationPool() {
		activations = new ArrayList<Activation>();
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
			if (activation.getObjectName().equals(objectName)) {
				activation.setActivated();
			}
		}
	}

	public void setActivatedEntireProject(String projectname) {
		for (Activation activation : activations) {
			if (activation.getProjectName().equals(projectname)) {
				activation.setActivated();
			}
		}
	}

	public List<Activation> findAllActiveOrIncludedInJob() {
		return activations.stream().filter(item -> item.isActivated() || item.isIncludedInJob())
				.collect(Collectors.<Activation>toList());
	}

	public void unregisterAllIncludedInJob() {
		activations.removeIf(item -> item.isIncludedInJob());
	}

	public void changeActivedToIncludedInJob() {
		for (Activation activation : activations) {
			if (activation.isActivated()) {
				activation.setIncludedInJob();
			}
		}

	}

	public void setLastInactiveObjects(List<ActivationObject> inactiveObjects) {
		this.inactiveObjects = inactiveObjects;
	}

	public List<ActivationObject> getLastInactiveObjects() {
		return inactiveObjects;
	}

}
