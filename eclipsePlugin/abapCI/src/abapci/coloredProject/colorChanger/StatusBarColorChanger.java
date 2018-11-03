package abapci.coloredProject.colorChanger;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import abapci.coloredProject.model.projectColor.IProjectColor;

public class StatusBarColorChanger extends ColorChanger {

	private Shell shell;

	public StatusBarColorChanger(Shell shell, IProjectColor projectColor) {
		this.shell = shell;
		this.projectColor = projectColor; 
	}

	public void change() {

		setBackgroundForChildren(shell, StatusBarColorHelper.getColor(projectColor),
				"org.eclipse.jface.action.StatusLine", 0);

	}

	private void setBackgroundForChildren(Composite composite, Color color, String classname, int level) {
		if (level < 3) {
			if (composite != null) {
				Control[] children = composite.getChildren();
				for (Control child : children) {

					if (child.getClass().getName().equals(classname)) {
						child.getParent().setBackground(color);
					} else {
						if (child instanceof Composite) {
							setBackgroundForChildren((Composite) child, color, classname, ++level);
						}
						level--;
					}
				}
			}
		}
	}
}
