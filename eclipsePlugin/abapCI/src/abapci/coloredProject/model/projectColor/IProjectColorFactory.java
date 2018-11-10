package abapci.coloredProject.model.projectColor;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

public interface IProjectColorFactory {
	IProjectColor create(RGB rgb);

	IProjectColor create(RGB rgb, boolean isSuppressed);

	IProjectColor create(Color color);

	IProjectColor create(Color generalColor, boolean suppressed);

	IProjectColor createStandardColor();
}
