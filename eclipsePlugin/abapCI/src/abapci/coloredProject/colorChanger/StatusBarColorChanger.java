package abapci.coloredProject.colorChanger;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import abapci.coloredProject.model.projectColor.IProjectColor;
import abapci.utils.ColorChooser;

public class StatusBarColorChanger extends ColorChanger {

	private final Shell shell;

	ColorChooser colorChooser;

	public StatusBarColorChanger(Shell shell, IProjectColor projectColor) {
		this.shell = shell;
		this.projectColor = projectColor;
		colorChooser = new ColorChooser();
	}

	@Override
	public void change() {

		Color backgroundColor = StatusBarColorHelper.getColor(projectColor);
		Color foregroundColor = colorChooser.getContrastColor(backgroundColor);
		setBackgroundForChildren(shell, backgroundColor, foregroundColor, "org.eclipse.jface.action.StatusLine", 0);

	}

	private void setBackgroundForChildren(Composite composite, Color backgroundColor, Color foregroundColor,
			String classname, int level) {
		if (level < 3) {
			if (composite != null) {
				Control[] children = composite.getChildren();
				for (Control child : children) {

					if (child.getClass().getName().equals(classname)) {
						child.getParent().setBackground(backgroundColor);
						child.getParent().setForeground(foregroundColor);
					} else {
						if (child instanceof Composite) {
							setBackgroundForChildren((Composite) child, backgroundColor, foregroundColor, classname,
									++level);
						}
						level--;
					}
				}
			}
		}
	}
}
