package abapci.coloredProject.model.projectColor;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

public interface IProjectColorFactory {
	public IProjectColor create(RGB rgb);

	public IProjectColor create(RGB rgb, boolean isSuppressed);

	public IProjectColor create(Color color);

	public IProjectColor create(Color generalColor, boolean suppressed);
}
