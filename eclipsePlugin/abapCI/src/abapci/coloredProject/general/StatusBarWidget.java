package abapci.coloredProject.general;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.menus.AbstractWorkbenchTrimWidget;

import abapci.AbapCiPlugin;
import abapci.AbapCiPluginHelper;
import abapci.feature.FeatureFacade;
import abapci.testResult.visualizer.ITestResultVisualizer;
import abapci.testResult.visualizer.ResultVisualizerOutput;
import abapci.testResult.visualizer.StatusBarWidgetTestVisualizer;

public class StatusBarWidget extends AbstractWorkbenchTrimWidget implements IStatusBarWidget, ITestResultVisualizer {

	private Composite composite;
	private String statusString = "Teststatus not yet initialized";
	private ITestResultVisualizer testResultVisualizer;
	private Button statusButton;

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

		final RowLayout layout = new RowLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.center = true;
		composite.setLayout(layout);
		addButton(newSide);
		composite.layout();

		final FeatureFacade featureFacade = new FeatureFacade();
		this.setVisible(featureFacade.getColoredProjectFeature().isStatusBarWidgetActive()
				|| featureFacade.getSourceCodeVisualisationFeature().isShowStatusBarWidgetEnabled());
	}

	private void addButton(int newSide) {
		statusButton = new Button(composite, SWT.BORDER | SWT.LEFT);
		statusButton.setLayoutData(getRowData(newSide));
		statusButton.setForeground(getColor(SWT.COLOR_BLACK));
		statusButton.setText(statusString);

		statusButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				final AbapCiPluginHelper abapCiHelper = new AbapCiPluginHelper();
				abapCiHelper.getContinousIntegrationPresenter().openEditorsForFailedItems();
			}
		});

		final Menu popupMenu = new Menu(statusButton);

		final MenuItem resetItem = new MenuItem(popupMenu, SWT.CASCADE);
		resetItem.setText("Reset error information");
		resetItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				final AbapCiPluginHelper abapCiHelper = new AbapCiPluginHelper();
				abapCiHelper.getContinousIntegrationPresenter().resetPackageTestStates();
			}
		});

		final MenuItem openItem = new MenuItem(popupMenu, SWT.CASCADE);
		openItem.setText("Open active errors");
		openItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				final AbapCiPluginHelper abapCiHelper = new AbapCiPluginHelper();
				abapCiHelper.getContinousIntegrationPresenter().openEditorsForFailedItems();
			}
		});

		statusButton.setMenu(popupMenu);
	}

	/**
	 * private void addStatusLabel(int newSide) { statusLabel = new Label(composite,
	 * SWT.BORDER | SWT.LEFT); statusLabel.setLayoutData(getRowData(newSide));
	 * statusLabel.setForeground(getColor(SWT.COLOR_BLACK));
	 * statusLabel.setText(statusString); }
	 **/

	RowData getRowData(int newSide) {
		final RowData rowData = new RowData();
		if ((newSide == SWT.BOTTOM) || (newSide == SWT.TOP)) {
			rowData.width = 400;
		}
		return rowData;
	}

	@Override
	public void setBackgroundColor(final Color color) {
		Display.getDefault().asyncExec(() -> {
			statusButton.setBackground(color);
			composite.redraw();
		});
	}

	@Override
	public void setText(final String statusString) {
		this.statusString = statusString;
		Display.getDefault().asyncExec(() -> {
			if (!statusButton.getText().equals(statusString)) {
				statusButton.setText(statusString);
				composite.redraw();
			}
		});
	}

	@Override
	public void setToolTip(final String tooltip) {
		if (statusButton != null) {
			Display.getDefault().asyncExec(() -> statusButton.setToolTipText(tooltip));
		}
	}

	@Override
	public void setTextColor(final Color color) {
		if (statusButton != null) {
			Display.getDefault().asyncExec(() -> statusButton.setForeground((color)));
		}
	}

	public Color getColor(int color) {
		return Display.getCurrent().getSystemColor(color);
	}

	@Override
	public void setVisible(boolean visible) {
		if (composite != null) {
			composite.setVisible(visible);
		}
	}

	@Override
	public boolean isVisible() {
		return composite != null && composite.isVisible();
	}

}
