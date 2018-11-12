package abapci.ci.views;

import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.PreferencesUtil;

import abapci.ci.presenter.ContinuousIntegrationPresenter;
import abapci.domain.ContinuousIntegrationConfig;
import abapci.feature.FeatureFacade;

public class AddOrUpdateContinuousIntegrationConfigPage extends Dialog {

	final static Color HEADER_COLOR = new Color(Display.getCurrent(), 0, 0, 255);

	ContinuousIntegrationPresenter presenter;
	Combo cbProjectContent;
	Text packageText;
	ContinuousIntegrationConfig ciConfig;

	Button activated;
	Button showPopUpYes;
	boolean showPopUpButton;

	public AddOrUpdateContinuousIntegrationConfigPage(Shell parentShell, ContinuousIntegrationPresenter presenter,
			ContinuousIntegrationConfig ciConfig, boolean showPopUpButton) {
		super(parentShell);
		this.presenter = presenter;
		this.ciConfig = ciConfig;
		this.showPopUpButton = showPopUpButton;

	}

	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText("ABAP package configuration for CI Job");
	}

	@Override
	protected Control createDialogArea(Composite parent) {

		Composite container = (Composite) super.createDialogArea(parent);

		addProjectComboBox(container);

		addPackageTextfield(container);

		addActivatedCheckbox(container);

		addShowAgainCheckbox(container);

		addDescriptionPart(parent, container);
		addEmptyLine(container);

		return container;
	}

	private void addDescriptionPart(Composite parent, Composite container) {
		Label descriptionLabel = new Label(container, SWT.NONE);

		descriptionLabel.setText("Description");
		descriptionLabel.setForeground(HEADER_COLOR);

		Text description = new Text(container, SWT.WRAP | SWT.MULTI | SWT.BORDER);

		GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL);
		gridData.horizontalSpan = 3;
		gridData.grabExcessVerticalSpace = true;

		StringBuilder descriptionTextBuilder = new StringBuilder();
		descriptionTextBuilder.append("An ABAP package was detected which is not already configured for the CI run");
		descriptionTextBuilder.append(System.lineSeparator() + System.lineSeparator());
		descriptionTextBuilder
				.append("By activating the option Unit test, unit tests are performed after each activation");
		descriptionTextBuilder.append(System.lineSeparator() + System.lineSeparator());
		descriptionTextBuilder.append(
				"By activation the checkbox ATC checks, after each Activation the activated ABAP classes are checked for findings.");
		descriptionTextBuilder.append(System.lineSeparator());
		descriptionTextBuilder.append(
				"! Please be aware that this  functionality generates some data in the table TBD. Please reorganise this table with the ABAP program TBD continuously !");
		descriptionTextBuilder.append(System.lineSeparator());
		descriptionTextBuilder
				.append("General configuration of this feature can be found in the <a>Eclipse preferences</a>");
		descriptionTextBuilder.append(System.lineSeparator());

		description.setLayoutData(gridData);
		description.setText(descriptionTextBuilder.toString());
		description.setEnabled(false);

		addEmptyLine(container);

		Link link = new Link(container, SWT.NONE);
		link.setText("The details can be configured in the <a>Eclipse preferences</a>");

		link.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				final PreferenceDialog dialog = PreferencesUtil.createPreferenceDialogOn(parent.getShell(),
						"abapci.preferences.abapCiPreferences", null, null);
				dialog.open();
			}
		});
	}

	private void addEmptyLine(Composite container) {
		new Label(container, SWT.READ_ONLY);
	}

	private void addShowAgainCheckbox(Composite container) {
		Label lblShowPopUpAgain = new Label(container, SWT.READ_ONLY);
		lblShowPopUpAgain.setText("Show this popup again, when the next new package is detected?");

		Composite compShowPopUp = new Composite(container, SWT.NULL);
		compShowPopUp.setLayout(new RowLayout());
		showPopUpYes = new Button(compShowPopUp, SWT.RADIO);
		showPopUpYes.setSelection(true);
		showPopUpYes.setText("Yes");
		Button showPopUpNo = new Button(compShowPopUp, SWT.RADIO);
		showPopUpNo.setSelection(false);
		showPopUpNo.setText("No");

		lblShowPopUpAgain.setVisible(showPopUpButton);
		compShowPopUp.setVisible(showPopUpButton);
	}

	private void addActivatedCheckbox(Composite container) {
		Label lblActivated = new Label(container, SWT.READ_ONLY);
		lblActivated.setText("Include the Package in the CI Run?");
		lblActivated.setForeground(HEADER_COLOR);

		boolean isActivated = ciConfig == null ? true : ciConfig.getUtActivated();
		Composite compActivated = new Composite(container, SWT.NULL);
		compActivated.setLayout(new RowLayout());
		activated = new Button(compActivated, SWT.RADIO);
		activated.setSelection(isActivated);
		activated.setText("Yes");
		Button deactivated = new Button(compActivated, SWT.RADIO);
		deactivated.setSelection(!isActivated);
		deactivated.setText("No");
	}

	private void addPackageTextfield(Composite container) {
		Label lblPackageHeader = new Label(container, SWT.READ_ONLY);
		lblPackageHeader.setToolTipText("Select a package");
		lblPackageHeader.setText("Package:");
		lblPackageHeader.setForeground(HEADER_COLOR);

		packageText = new Text(container, SWT.BORDER);
		packageText.setSize(200, packageText.getSize().y);
		packageText.setLayoutData(new GridData(GridData.BEGINNING));

		if (ciConfig != null) {
			packageText.setText(ciConfig.getPackageName());
			packageText.setEnabled(false);
		}
	}

	private void addProjectComboBox(Composite container) {
		Label lblProjectHeader = new Label(container, SWT.READ_ONLY);
		lblProjectHeader.setText("Project:");
		lblProjectHeader.setToolTipText("Select a ABAP project");
		lblProjectHeader.setBounds(10, 50, 100, 65);
		lblProjectHeader.setForeground(HEADER_COLOR);

		cbProjectContent = new Combo(container, SWT.READ_ONLY);
		cbProjectContent.setItems(getAbapProjectsArray());

		if (ciConfig != null) {
			cbProjectContent.setText(ciConfig.getProjectName());
			cbProjectContent.setEnabled(false);
		}
	}

	private String[] getAbapProjectsArray() {
		ArrayList<String> projectNames = new ArrayList<>();
		IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
		IProject[] availableProjects = workspaceRoot.getProjects();

		// IProject[] availableProjects =
		// AdtCoreProjectServiceFactory.createCoreProjectService()
		// .getAvailableAdtCoreProjects();

		for (IProject project : availableProjects) {
			// if (AbapProjectService.getInstance().isAbapProject(project)) {
			projectNames.add(project.getName());
			// }
		}
		return projectNames.toArray(new String[0]);
	}

	public static String[] getElementsOfEnum(Class<? extends Enum<?>> e) {
		return Arrays.stream(e.getEnumConstants()).map(Enum::name).toArray(String[]::new);
	}

	@Override
	protected void okPressed() {
		if (!showPopUpYes.getSelection()) {
			FeatureFacade featureFacade = new FeatureFacade();
			featureFacade.setShowDialogNewPackageForCiRun(showPopUpYes.getSelection());
		}
		presenter.addContinousIntegrationConfig(new ContinuousIntegrationConfig(cbProjectContent.getText(),
				packageText.getText(), activated.getSelection(), activated.getSelection()));
		super.okPressed();
	}
}