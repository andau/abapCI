package abapci.activation;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ActivationPool {

	private static ActivationPool instance;
	private List<Activation> activations;

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

	public List<Activation> getActiveActivations() {
		return activations.stream().filter(item -> item.isActivated()).collect(Collectors.<Activation>toList());
	}

	public void unregisterAllActivated() {
		activations.removeIf(item -> item.isActivated());
	}

}
