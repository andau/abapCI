package abapci.views.wizard;

import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import abapci.domain.ContinuousIntegrationConfig;
import abapci.feature.FeatureFacade;
import abapci.presenter.ContinuousIntegrationPresenter;

public class AddOrUpdateContinuousIntegrationConfigPage extends Dialog {

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

		Label lblProjectHeader = new Label(container, SWT.READ_ONLY);
		lblProjectHeader.setText("Project:");
		lblProjectHeader.setToolTipText("Select a ABAP project");
		lblProjectHeader.setBounds(10, 50, 25, 65);

		cbProjectContent = new Combo(container, SWT.READ_ONLY);
		cbProjectContent.setItems(getAbapProjectsArray());

		if (ciConfig != null) {
			cbProjectContent.setText(ciConfig.getProjectName());
			cbProjectContent.setEnabled(false);
		}

		Label lblPackageHeader = new Label(container, SWT.READ_ONLY);
		lblPackageHeader.setToolTipText("Select a package");
		lblPackageHeader.setText("Package:");

		packageText = new Text(container, SWT.SINGLE);
		packageText.setBounds(10, 50, 25, 65);

		if (ciConfig != null) {
			packageText.setText(ciConfig.getPackageName());
			packageText.setEnabled(false);
		}

		Label lblActivated = new Label(container, SWT.READ_ONLY);
		lblActivated.setText("Include the Package in the CI Run?");

		boolean isActivated = ciConfig == null ? true : ciConfig.getUtActivated();
		Composite compActivated = new Composite(container, SWT.NULL);
		compActivated.setLayout(new RowLayout());
		activated = new Button(compActivated, SWT.RADIO);
		activated.setSelection(isActivated);
		activated.setText("Yes");
		Button deactivated = new Button(compActivated, SWT.RADIO);
		deactivated.setSelection(!isActivated);
		deactivated.setText("No");

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

		container.pack();

		return container;
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