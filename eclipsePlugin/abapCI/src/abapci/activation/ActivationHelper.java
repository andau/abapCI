package abapci.activation;

import java.util.List;
import java.util.stream.Collectors;

public class ActivationHelper {

	public static List<String> getPackages(List<Activation> activatedInactiveObjects) {
		return activatedInactiveObjects.stream().filter(item -> item.getPackageName() != null)
				.map(item -> item.getPackageName()).distinct().collect(Collectors.toList());
	}

}
