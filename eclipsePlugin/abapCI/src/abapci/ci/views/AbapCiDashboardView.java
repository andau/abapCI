package abapci.ci.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.eclipse.ui.part.ViewPart;

import abapci.AbapCiPluginHelper;
import abapci.ci.presenter.ContinuousIntegrationPresenter;
import abapci.testResult.visualizer.DashboardTestResultVisualizer;
import abapci.testResult.visualizer.ITestResultVisualizer;

public class AbapCiDashboardView extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "abapci.ci.views.AbapCiDashboardView";

	private ContinuousIntegrationPresenter presenter;

	private Label lblOverallTestState;
	private Label infoline;
	private Hyperlink openErrorHyperlink;

	private Composite parent;
	private Composite entireContainer;

	private final ITestResultVisualizer testResultVisualizer;

	public AbapCiDashboardView() {
		testResultVisualizer = new DashboardTestResultVisualizer(this);
	}

	public void setBackgroundColor(Color color) {
		parent.setBackground(color);
	}

	public void setForegroundColor(Color foregroundColor) {
		lblOverallTestState.setForeground(foregroundColor);
		infoline.setForeground(foregroundColor);
		openErrorHyperlink.setForeground(foregroundColor);
	}

	@Override
	public void createPartControl(Composite parent) {

		this.parent = parent;
		final AbapCiPluginHelper abapCiPluginHelper = new AbapCiPluginHelper();
		presenter = abapCiPluginHelper.getContinousIntegrationPresenter();

		entireContainer = new Composite(parent, SWT.NONE);
		entireContainer.setLayout(new GridLayout(1, false));

		lblOverallTestState = new Label(entireContainer, SWT.CENTER);
		final FontData[] fontData = lblOverallTestState.getFont().getFontData();
		fontData[0].setHeight(16);
		lblOverallTestState.setText("Status not yet defined");

		lblOverallTestState.setFont(new Font(Display.getCurrent(), fontData[0]));
		lblOverallTestState.setLayoutData(new GridData(SWT.CENTER, GridData.FILL_HORIZONTAL, true, true));
		lblOverallTestState.setBounds(entireContainer.getBounds());

		infoline = new Label(entireContainer, SWT.FILL);
		infoline.setText("Initialized");
		infoline.setLayoutData(new GridData(SWT.LEFT, GridData.FILL_HORIZONTAL, true, true));

		openErrorHyperlink = new Hyperlink(entireContainer, SWT.FILL);
		openErrorHyperlink.setText(
				"                                                                                                                ");
		openErrorHyperlink.setLayoutData(new GridData(SWT.LEFT, SWT.WRAP, true, true));
		openErrorHyperlink.addHyperlinkListener(new HyperlinkAdapter() {
			@Override
			public void linkActivated(HyperlinkEvent e) {
				presenter.openEditorsForFailedItems();
			}

		});

		parent.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
		parent.redraw();

		if (presenter != null) {
			presenter.registerDashboardView(this);
		} else {
			lblOverallTestState.setText("No active test run found");
			infoline.setText("Activate Unit testrun or ATC testrun and restart");
		}

	}

	public Composite getEntireContainer() {
		return entireContainer;
	}

	public ITestResultVisualizer getTestResultVisualizer() {
		return testResultVisualizer;
	}

	public void setLabelOverallTestStateText(String text) {
		lblOverallTestState.setText(text);
	}

	public void setInfolineText(String infolineText) {
		infoline.setText(infolineText);

	}

	public Hyperlink getOpenErrorHyperlink() {
		return openErrorHyperlink;
	}

	@Override
	public void setFocus() {
	}

}
