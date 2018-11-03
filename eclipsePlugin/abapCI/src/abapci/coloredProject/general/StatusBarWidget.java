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
import abapci.testResult.visualizer.ITestResultVisualizer;
import abapci.testResult.visualizer.ResultVisualizerOutput;
import abapci.testResult.visualizer.StatusBarWidgetTestVisualizer;

public class StatusBarWidget extends AbstractWorkbenchTrimWidget implements IStatusBarWidget, ITestResultVisualizer {

	private Composite composite;
	private Label statusLabel;
	private String statusString = "Teststatus not yet initialized";
	private ITestResultVisualizer testResultVisualizer; 
	

	@Override
	public void setResultVisualizerOutput(ResultVisualizerOutput resultVisualizerOutput) {
		setText(resultVisualizerOutput.getGlobalTestState()); 		
		setBackgroundColor(resultVisualizerOutput.getBackgroundColor());
	}


	@Override
	public ITestResultVisualizer getTestResultVisualizer() {
		return testResultVisualizer; 
	}

	
	@Override
	public void init(IWorkbenchWindow workbenchWindow) {
		testResultVisualizer = new StatusBarWidgetTestVisualizer(this); 
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
		statusLabel.setForeground(getColor(SWT.COLOR_BLACK));
		statusLabel.setText(statusString);
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

		@Override
		public void setVisible(boolean visible) {
			//FeatureFacade featureFacade = new FeatureFacade(); 
			//ColoredProjectFeature projectColorFeature = featureFacade.getColoredProjectFeature(); 
			//composite.setVisible(projectColorFeature.isStatusBarWidgetActive()); 
			composite.setVisible(visible);
		}



}
