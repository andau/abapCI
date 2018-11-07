package abapci.coloredProject.view;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.preference.ColorSelector;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import abapci.AbapCiPlugin;
import abapci.coloredProject.model.ColoredProject;
import abapci.coloredProject.model.projectColor.IProjectColor;
import abapci.coloredProject.model.projectColor.IProjectColorFactory;
import abapci.coloredProject.model.projectColor.ProjectColorFactory;
import abapci.coloredProject.presenter.ColoredProjectsPresenter;
import abapci.feature.FeatureFacade;
import abapci.feature.activeFeature.ColoredProjectFeature;
import abapci.preferences.PreferenceConstants;

public class AddOrUpdateColoredProjectPage extends Dialog {

	ColoredProjectsPresenter presenter;
	Combo comboColoredProject;
	ColorSelector colorSelector;
	ColoredProject coloredProject;
	Button btnSuppressColoring;
	Button showPopUpYes;
	boolean showPopUpButton;

	public AddOrUpdateColoredProjectPage(Shell parentShell, ColoredProjectsPresenter presenter,
			ColoredProject coloredProject, boolean showPopUpButton) {
		super(parentShell);
		this.presenter = presenter;
		this.coloredProject = coloredProject;
		this.showPopUpButton = showPopUpButton;
	}

	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText("Assignment of a color to an ABAP project");
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);

		addEmptyLine(container);

		Label headerText1 = new Label(container, SWT.READ_ONLY);
		headerText1.setText(
				"Assign a color to a project. The color will be used to indicate the current selected project by coloring specific UI components.");
		Label headerText2 = new Label(container, SWT.READ_ONLY);
		headerText2.setText("See Preferences - section ABAP CI for details.");

		addEmptyLine(container);

		Label coloredProjectLabel = new Label(container, SWT.READ_ONLY);
		coloredProjectLabel.setText("Project:");
		coloredProjectLabel.setToolTipText("Select a ABAP project");
		// coloredProjectLabel.setBounds(10, 50, 25, 10);
		comboColoredProject = new Combo(container, SWT.READ_ONLY);
		comboColoredProject.setItems(getProjectNames());

		Label colorLabel = new Label(container, SWT.READ_ONLY);
		colorLabel.setToolTipText("Select a predefined color");
		colorLabel.setText("Color:");

		colorSelector = new ColorSelector(container);

		if (coloredProject != null) {
			if (coloredProject.getName() != null) {
				comboColoredProject.setText(coloredProject.getName());
				comboColoredProject.setEnabled(false);
			}

			if (coloredProject.getColor() != null) {
				colorSelector.setColorValue(coloredProject.getColor().getRGB());
			} else {
				colorSelector.setColorValue(new RGB(255, 255, 255));
			}
		}

		btnSuppressColoring = new Button(container, SWT.CHECK);
		btnSuppressColoring.setText("Do not color this project");
		btnSuppressColoring.setSelection(coloredProject != null ? coloredProject.isSuppressedColoring() : false);

		addEmptyLine(container);

		showCheckBoxForPopUp(container, showPopUpButton);

		try {
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
					.showView("abapci.views.AbapCiColoredProjectView");
		} catch (PartInitException e) {
			// if project color view can not be opened we keep on going as this is not
			// critical
			e.printStackTrace();
		}

		return container;
	}

	private void addEmptyLine(Composite container) {
		new Label(container, SWT.READ_ONLY);
	}

	private void showCheckBoxForPopUp(Composite container, boolean showPopUpCheckBox) {
		Label lblShowPopUpAgain = new Label(container, SWT.READ_ONLY);
		lblShowPopUpAgain.setText("Show this popup again, when an project without an color assignment is detected?");

		Composite compShowPopUp = new Composite(container, SWT.NULL);
		compShowPopUp.setLayout(new RowLayout());
		showPopUpYes = new Button(compShowPopUp, SWT.RADIO);
		showPopUpYes.setSelection(true);
		showPopUpYes.setText("Yes");
		Button showPopUpNo = new Button(compShowPopUp, SWT.RADIO);
		showPopUpNo.setSelection(false);
		showPopUpNo.setText("No");

		lblShowPopUpAgain.setVisible(showPopUpCheckBox);
		compShowPopUp.setVisible(showPopUpCheckBox);
	}

	private String[] getProjectNames() {
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		List<String> projectNames = Arrays.stream(projects).map(item -> item.getName()).collect(Collectors.toList());
		return projectNames.toArray(new String[0]);
	}

	public static String[] getElementsOfEnum(Class<? extends Enum<?>> e) {
		return Arrays.stream(e.getEnumConstants()).map(Enum::name).toArray(String[]::new);
	}

	@Override
	protected void okPressed() {

		ColoredProjectFeature coloredProjectFeature = createColoredProjectFeature();

		coloredProjectFeature.setDialogEnabled(showPopUpYes.getSelection());

		IPreferenceStore prefs = AbapCiPlugin.getDefault().getPreferenceStore();
		prefs.setValue(PreferenceConstants.PREF_COLORED_PROJECTS_NEW_DIALOG_ENABLED, showPopUpYes.getSelection());

		RGB selectedRgb = colorSelector.getColorValue();
		if (comboColoredProject.getText() != null && !comboColoredProject.getText().equals("") && selectedRgb != null) {
			IProjectColorFactory projectColorFactory = new ProjectColorFactory();
			IProjectColor projectColor = projectColorFactory.create(selectedRgb, btnSuppressColoring.getSelection());
			presenter.addColoredProject(new ColoredProject(comboColoredProject.getText(), projectColor,
					btnSuppressColoring.getSelection()));
			presenter.setViewerInput();
		}
		super.okPressed();
	}

	private ColoredProjectFeature createColoredProjectFeature() {
		FeatureFacade featureFacade = new FeatureFacade();
		ColoredProjectFeature coloredProjectFeature = featureFacade.getColoredProjectFeature();
		return coloredProjectFeature;
	}

	@Override
	protected void cancelPressed() {
		IPreferenceStore prefs = AbapCiPlugin.getDefault().getPreferenceStore();
		prefs.setValue(PreferenceConstants.PREF_COLORED_PROJECTS_NEW_DIALOG_ENABLED, showPopUpYes.getSelection());

		super.cancelPressed();
	}
}