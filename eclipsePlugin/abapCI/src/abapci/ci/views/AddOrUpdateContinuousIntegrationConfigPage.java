package abapci.ci.views;

import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.preference.IPreferenceStore;
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
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.PreferencesUtil;

import abapci.AbapCiPlugin;
import abapci.ci.presenter.ContinuousIntegrationPresenter;
import abapci.domain.ContinuousIntegrationConfig;
import abapci.feature.activeFeature.AtcFeature;
import abapci.feature.activeFeature.UnitFeature;
import abapci.preferences.PreferenceConstants;

public class AddOrUpdateContinuousIntegrationConfigPage extends Dialog {

	final static Color HEADER_COLOR = new Color(Display.getCurrent(), 0, 0, 255);

	ContinuousIntegrationPresenter presenter;
	Combo cbProjectContent;
	Text packageText;
	ContinuousIntegrationConfig ciConfig;

	Button unitTestsEnabled;
	Button atcEnabled;
	Button showPopUpYes;
	boolean showPopUpButton;

	AtcFeature atcFeature;
	UnitFeature unitFeature;

	public AddOrUpdateContinuousIntegrationConfigPage(Shell parentShell, ContinuousIntegrationPresenter presenter,
			ContinuousIntegrationConfig ciConfig, boolean showPopUpButton) {
		super(parentShell);
		this.presenter = presenter;
		this.ciConfig = ciConfig;
		this.showPopUpButton = showPopUpButton;

		atcFeature = presenter.getAtcFeature();
		unitFeature = presenter.getUnitFeature();

	}

	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText("ABAP package configuration for CI Job");
	}

	@Override
	protected Control createDialogArea(Composite parent) {

		final Composite container = (Composite) super.createDialogArea(parent);

		addProjectComboBox(container);

		addPackageTextfield(container);

		addEmptyLine(container);

		addUnitTestsCheckbox(container, unitFeature.isActive());

		addAtcCheckbox(container, atcFeature.isActive());

		addEmptyLine(container);

		addShowAgainCheckbox(container);

		addDescriptionPart(parent, container);
		addEmptyLine(container);

		return container;
	}

	private void addDescriptionPart(Composite parent, Composite container) {
		final Label descriptionLabel = new Label(container, SWT.NONE);

		descriptionLabel.setText("Description");
		descriptionLabel.setForeground(HEADER_COLOR);

		final Text description = new Text(container, SWT.WRAP | SWT.MULTI | SWT.BORDER);

		final GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL);
		gridData.horizontalSpan = 3;
		gridData.grabExcessVerticalSpace = true;

		final StringBuilder descriptionTextBuilder = new StringBuilder();
		descriptionTextBuilder.append(
				"If this dialog appeared automatically an ABAP package was detected which is not already configured for the CI run. You can include/exclude this package by activating or deactivating the two checkboxes above.");
		descriptionTextBuilder.append(System.lineSeparator() + System.lineSeparator());
		descriptionTextBuilder.append(
				"When the checkbox Unit test is checked, unit tests are run automatically after each activation of an ABAP development object which belongs to this package.");
		descriptionTextBuilder.append(System.lineSeparator() + System.lineSeparator());
		descriptionTextBuilder.append(
				"By activating the checkbox ATC checks, after each activation the activated ABAP classes are checked for ATC findings.");
		descriptionTextBuilder.append(System.lineSeparator());
		descriptionTextBuilder.append(
				"! Please be aware that this functionality generates some data in the table SATC_AC_RESULTVT. Reorganise this table with the ABAP program SATC_AC_CLEANUP continuously !");
		descriptionTextBuilder.append(System.lineSeparator());

		description.setLayoutData(gridData);
		description.setText(descriptionTextBuilder.toString());
		description.setEnabled(false);

		addEmptyLine(container);

		final Link link = new Link(container, SWT.NONE);
		link.setText("General configuration settings for this feature can be done in the <a>Eclipse preferences</a>");

		link.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				final PreferenceDialog dialog = PreferencesUtil.createPreferenceDialogOn(parent.getShell(),
						"abapci.preferences.abapCiPreferences", null, null);
				dialog.open();
			}
		});

		try {
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
					.showView("abapci.ci.views.AbapCiMainView.java");
		} catch (PartInitException e) {
			// if ABAP CI view can not be opened we keep on going as this is not
			// a critical function
			e.printStackTrace();
		}

	}

	private void addEmptyLine(Composite container) {
		new Label(container, SWT.READ_ONLY);
	}

	private void addShowAgainCheckbox(Composite container) {
		final Label lblShowPopUpAgain = new Label(container, SWT.READ_ONLY);
		lblShowPopUpAgain.setText("Show this dialog again, when the next not configured ABAP package is detected?");

		final Composite compShowPopUp = new Composite(container, SWT.NULL);
		compShowPopUp.setLayout(new RowLayout());
		showPopUpYes = new Button(compShowPopUp, SWT.RADIO);
		showPopUpYes.setSelection(true);
		showPopUpYes.setText("Yes");
		final Button showPopUpNo = new Button(compShowPopUp, SWT.RADIO);
		showPopUpNo.setSelection(false);
		showPopUpNo.setText("No");

		lblShowPopUpAgain.setVisible(showPopUpButton);
		compShowPopUp.setVisible(showPopUpButton);
	}

	private void addUnitTestsCheckbox(Composite container, boolean featureActivated) {
		final Label lblActivated = new Label(container, SWT.READ_ONLY);
		lblActivated.setText("Include ABAP package into the automatic Unit testrun?");
		lblActivated.setForeground(HEADER_COLOR);

		final boolean isActivated = ciConfig == null ? featureActivated : ciConfig.getUtActivated();

		final Composite compActivated = new Composite(container, SWT.NULL);
		compActivated.setLayout(new RowLayout());
		unitTestsEnabled = new Button(compActivated, SWT.RADIO);
		unitTestsEnabled.setSelection(isActivated);
		unitTestsEnabled.setText("Yes");
		final Button deactivated = new Button(compActivated, SWT.RADIO);
		deactivated.setSelection(!isActivated);
		deactivated.setText("No");

	}

	private void addAtcCheckbox(Composite container, boolean featureActivated) {
		final Label lblActivated = new Label(container, SWT.READ_ONLY);
		lblActivated.setText("Include ABAP package into the automatic ATC run (see description)?");
		lblActivated.setForeground(HEADER_COLOR);

		final boolean isActivated = ciConfig == null ? featureActivated : ciConfig.getUtActivated();

		final Composite compActivated = new Composite(container, SWT.NULL);
		compActivated.setLayout(new RowLayout());
		atcEnabled = new Button(compActivated, SWT.RADIO);
		atcEnabled.setSelection(isActivated);
		atcEnabled.setText("Yes");
		final Button deactivated = new Button(compActivated, SWT.RADIO);
		deactivated.setSelection(!isActivated);
		deactivated.setText("No");

	}

	private void addPackageTextfield(Composite container) {
		final Label lblPackageHeader = new Label(container, SWT.READ_ONLY);
		lblPackageHeader.setToolTipText("Select a package");
		lblPackageHeader.setText("Package:");
		lblPackageHeader.setForeground(HEADER_COLOR);

		packageText = new Text(container, SWT.BORDER);
		packageText.setSize(200, packageText.getSize().y);
		packageText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		if (ciConfig != null) {
			packageText.setText(ciConfig.getPackageName());
			packageText.setEnabled(false);
		}
	}

	private void addProjectComboBox(Composite container) {
		final Label lblProjectHeader = new Label(container, SWT.READ_ONLY);
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
		final ArrayList<String> projectNames = new ArrayList<>();
		final IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
		final IProject[] availableProjects = workspaceRoot.getProjects();

		// IProject[] availableProjects =
		// AdtCoreProjectServiceFactory.createCoreProjectService()
		// .getAvailableAdtCoreProjects();

		for (final IProject project : availableProjects) {
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

		handleShowPopUpCheckbox();

		presenter.addContinousIntegrationConfig(new ContinuousIntegrationConfig(cbProjectContent.getText(),
				packageText.getText(), unitTestsEnabled.getSelection(), atcEnabled.getSelection()));

		super.okPressed();
	}

	@Override
	protected void cancelPressed() {

		handleShowPopUpCheckbox();

		super.cancelPressed();
	}

	private void handleShowPopUpCheckbox() {
		if (!showPopUpYes.getSelection()) {
			disableNewConfigurationDialog();
		}
	}

	private void disableNewConfigurationDialog() {
		final IPreferenceStore prefs = AbapCiPlugin.getDefault().getPreferenceStore();
		prefs.setValue(PreferenceConstants.PREF_DIALOG_NEW_PACKAGE_FOR_CI_RUN_ENABLED, false);
	}

}