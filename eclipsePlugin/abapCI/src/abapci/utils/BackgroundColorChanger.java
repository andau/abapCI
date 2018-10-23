package abapci.utils;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class BackgroundColorChanger {

	public void change(Shell shell, Color color) {

		if (color != null) {
			setBackgroundForChildren(shell, color, "org.eclipse.jface.action.StatusLine", 0);
			// setBackgroundForLabel((Composite) currentShell.getChildren()[0], new
			// Color(Display.getDefault(), rgb),
			// "ZCL_BIG_TRUCK", 0, "ZCL_BIG_TRUCK");

		} else {
			setBackgroundForChildren(shell, Display.getDefault().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND),
					"org.eclipse.jface.action.StatusLine", 0);
		}

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

	private void setBackgroundForLabel(Composite composite, Color color, String classname, int level, String caption) {
		Control[] children = composite.getChildren();
		for (Control child : children) {

			if (child instanceof Label) {
				Label label = (Label) child;
				if (label.getText().contains(caption)) {
					child.getParent().setBackground(color);
				}
			} else {
				if (child instanceof Composite) {
					setBackgroundForLabel((Composite) child, color, classname, ++level, caption);
				}
				level--;
			}
		}
	}

}
