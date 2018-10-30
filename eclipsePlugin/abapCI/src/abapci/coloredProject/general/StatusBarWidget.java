package abapci.coloredProject.general;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.menus.AbstractWorkbenchTrimWidget;

import abapci.AbapCiPlugin;

public class StatusBarWidget extends AbstractWorkbenchTrimWidget implements IStatusBarWidget {

	private Composite composite;
	private Label statusLabel;
	private String statusString = "Status not yet set";
	private Color backgroundColor = getColor(SWT.COLOR_BLACK);

	@Override
	public void init(IWorkbenchWindow workbenchWindow) {
		AbapCiPlugin.getDefault().attachStatusBarWidget(this);
	}
		
	@Override
	public void dispose() {
		if ((composite != null) && !composite.isDisposed()) {
			composite.dispose();
		}
		composite = null;
	}

	@Override
	public void fill(Composite parent, int oldSide, int newSide) {
		composite = new Composite(parent, SWT.NONE);

		RowLayout layout = new RowLayout();
		layout.marginHeight = 4;
		layout.marginWidth = 2;
		layout.center = true;
		composite.setLayout(layout);

		addStatusLabel(newSide);
		composite.layout();
	}

	private void addStatusLabel(int newSide) {
		statusLabel = new Label(composite, SWT.BORDER | SWT.LEFT);
		statusLabel.setLayoutData(getRowData(newSide));
		statusLabel.setForeground(getColor(SWT.COLOR_WHITE));
		statusLabel.setText(statusString);
		statusLabel.setBackground(backgroundColor);
	}

	RowData getRowData(int newSide) {
		RowData rowData = new RowData();
		if ((newSide == SWT.BOTTOM) || (newSide == SWT.TOP)) {
			rowData.width = 400;
		}
		return rowData;
	}

	@Override
	public void setBackgroundColor(final Color color) {
		backgroundColor = color;
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				statusLabel.setBackground(color);
				composite.redraw();
			}
		});
	}

	@Override
	public void setText(final String statusString) {
		this.statusString = statusString;
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				if (!statusLabel.getText().equals(statusString)) {
					statusLabel.setText(statusString);
					composite.redraw();
				}
			}
		});
	}

	@Override
	public void setToolTip(final String tooltip) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				statusLabel.setToolTipText(tooltip);
			}
		});
	}

	@Override
	public void setTextColor(final Color color) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				statusLabel.setForeground((color));
			}
		});
	}

		public Color getColor(int color) {
			return Display.getCurrent().getSystemColor(color);
		}
}
