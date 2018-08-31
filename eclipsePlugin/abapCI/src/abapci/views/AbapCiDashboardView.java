package abapci.views;

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

import abapci.AbapCiPlugin;
import abapci.presenter.ContinuousIntegrationPresenter;

public class AbapCiDashboardView extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "abapci.views.AbapCiDashboardView";

	private ContinuousIntegrationPresenter presenter;

	public Label projectline;
	public Label lblOverallTestState;
	public Label infoline;
	public Hyperlink openErrorHyperlink;

	private Composite parent;
	private Composite entireContainer;

	public AbapCiDashboardView() {
		ViewModel.INSTANCE.getOverallTestState();
		ViewModel.INSTANCE.getOverallInfoline();
	}

	public void setBackgroundColor(Color color) {
		parent.setBackground(color);
	}

	public void createPartControl(Composite parent) {

		this.parent = parent;
		presenter = AbapCiPlugin.getDefault().continuousIntegrationPresenter;

		entireContainer = new Composite(parent, SWT.NONE);
		entireContainer.setLayout(new GridLayout(1, false));

		lblOverallTestState = new Label(entireContainer, SWT.CENTER);
		FontData[] fontData = lblOverallTestState.getFont().getFontData();
		fontData[0].setHeight(16);
		lblOverallTestState.setText("Status not yet defined");

		lblOverallTestState.setFont(new Font(Display.getCurrent(), fontData[0]));
		lblOverallTestState.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true, true));

		infoline = new Label(entireContainer, SWT.FILL);
		infoline.setText(
				"Initialized                                                                                        ");
		infoline.setLayoutData(new GridData(SWT.LEFT, SWT.WRAP, true, true));

		openErrorHyperlink = new Hyperlink(entireContainer, SWT.FILL);
		openErrorHyperlink.setText(
				"                                                                                                                ");
		openErrorHyperlink.setLayoutData(new GridData(SWT.LEFT, SWT.WRAP, true, true));
		openErrorHyperlink.addHyperlinkListener(new HyperlinkAdapter() {
			public void linkActivated(HyperlinkEvent e) {
				presenter.openEditorsForFailedItems();
			}

		});

		parent.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
		parent.redraw();

		if (presenter != null) {
			presenter.registerDashboardView(this);
		} else {
			lblOverallTestState.setText("Unit testrun deactivated");
			infoline.setText("Activate 'Run Unit tests after an ABAP object' and restart");
		}

	}

	public Composite getEntireContainer() {
		return entireContainer;
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
	}

}
