package abapci.preferences;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

import abapci.utils.StringUtils;

public class UiItemsTestHelper {
	public static boolean findDuplicates(Control[] children) {
		boolean duplicateDetected = false;
		Set<String> uniqueControls = new HashSet<>();
		for (Control child : children) {
			if (child instanceof Label) {
				if (!addLabelIfNotNull(uniqueControls, child)) {
					duplicateDetected = true;
				}
			} else if (child instanceof Button) {
				if (!addButtonIfNotNull(uniqueControls, child)) {
					duplicateDetected = true;
				}
				;
			} else {
				// ignore the other controls
			}
		}

		return duplicateDetected;
	}

	private static boolean addLabelIfNotNull(Set<String> uniqueControls, Control child) {
		Label label = (Label) child;
		return label.getText().trim().equals(StringUtils.EMPTY) ? true : uniqueControls.add(label.getText());
	}

	private static boolean addButtonIfNotNull(Set<String> uniqueControls, Control child) {
		Button button = (Button) child;
		return button.getText().trim().equals(StringUtils.EMPTY) ? true : uniqueControls.add(button.getText());
	}

}
