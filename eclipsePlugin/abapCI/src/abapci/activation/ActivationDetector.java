package abapci.activation;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ActivationDetector {

	private static ActivationDetector instance;
	private List<Activation> activations;

	private ActivationDetector() {
		activations = new ArrayList<Activation>();
	}

	public static ActivationDetector getInstance() {
		if (instance == null) {
			instance = new ActivationDetector();
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

	public void setActivatedEntireProject(String projectname) {
		for (Activation activation : activations) {
			if (activation.getProjectName().equals(projectname)) {
				activation.setActivated();
			}
		}
	}

	@Deprecated
	public List<Activation> findActivationsAssignedToProject() {

		return findAllActiveOrIncludedInJob().stream()
				.filter(item -> item.getPackageName() != null || !item.getPackageName().isEmpty())
				.collect(Collectors.toList());
	}

	public List<Activation> findActiveActivationsAssignedToProject() {

		return findAllActive().stream()
				.filter(item -> item.getPackageName() != null && !item.getPackageName().isEmpty())
				.collect(Collectors.toList());
	}

	public List<Activation> findAllActiveOrIncludedInJob() {
		return activations.stream().filter(item -> item.isActivated() || item.isIncludedInJob())
				.collect(Collectors.<Activation>toList());
	}

	public List<Activation> findAllActive() {
		return activations.stream().filter(item -> item.isActivated()).collect(Collectors.<Activation>toList());
	}

	public void unregisterAllIncludedInJob() {
		activations.removeIf(item -> item == null || item.isIncludedInJob());
	}

	public void changeActivedToIncludedInJob() {
		for (Activation activation : activations) {
			if (activation.isActivated()) {
				activation.setIncludedInJob();
			}
		}

	}

}
