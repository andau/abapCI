package abapci.coloredProject.view;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.preference.ColorSelector;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
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
import abapci.coloredProject.model.ColoredProject;
import abapci.coloredProject.model.projectColor.IProjectColor;
import abapci.coloredProject.model.projectColor.IProjectColorFactory;
import abapci.coloredProject.model.projectColor.ProjectColorFactory;
import abapci.coloredProject.presenter.ColoredProjectsPresenter;
import abapci.feature.FeatureFacade;
import abapci.feature.activeFeature.ColoredProjectFeature;
import abapci.preferences.PreferenceConstants;
import abapci.utils.ResourcePluginHelper;

public class AddOrUpdateColoredProjectPage extends Dialog {

	final static Color HEADER_COLOR = new Color(Display.getCurrent(), 0, 0, 255);

	ResourcePluginHelper resourcePluginHelper;

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
		resourcePluginHelper = new ResourcePluginHelper();
	}

	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText("Colored Projects: Assignment of a coloring to a project");
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);

		addEmptyLine(container);

		addProjectComboBox(container);

		addColorChooser(container);

		addSuppressColoringButton(container);

		addEmptyLine(container);

		addCheckBoxForDialogPopUp(container, showPopUpButton);
		addEmptyLine(container);

		addDescriptionPart(parent, container);

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

	private void addProjectComboBox(Composite container) {
		Label coloredProjectLabel = new Label(container, SWT.READ_ONLY);
		coloredProjectLabel.setText("Project:");
		coloredProjectLabel.setToolTipText("Select the project which should be colored");
		coloredProjectLabel.setForeground(HEADER_COLOR);

		comboColoredProject = new Combo(container, SWT.READ_ONLY);
		comboColoredProject.setItems(getProjectNames());
	}

	private void addColorChooser(Composite container) {
		Label colorLabel = new Label(container, SWT.READ_ONLY);
		colorLabel.setToolTipText(
				"Select the color which should be used to color the development objects of the choosen project");
		colorLabel.setText("Color:");
		colorLabel.setForeground(HEADER_COLOR);

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
	}

	private void addSuppressColoringButton(Composite container) {
		btnSuppressColoring = new Button(container, SWT.CHECK);
		btnSuppressColoring.setText("Do not color this project");
		btnSuppressColoring.setSelection(coloredProject != null ? coloredProject.isSuppressedColoring() : false);
		btnSuppressColoring.setToolTipText("Check this checkbox if the selected project should not be colored");
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
		descriptionTextBuilder.append(
				"Assign a color to a project. The color will be used to indicate the current selected project by coloring specific UI components.");
		descriptionTextBuilder.append(System.lineSeparator() + System.lineSeparator());
		descriptionTextBuilder.append("The recommended standard configuration for a DEV, QAS, PRD system is: ");
		descriptionTextBuilder.append(System.lineSeparator());
		descriptionTextBuilder.append("   DEV: Check checkbox 'Do not color this project' ");
		descriptionTextBuilder.append(System.lineSeparator());
		descriptionTextBuilder.append("   QAS: Choose for example the color 'orange'");
		descriptionTextBuilder.append(System.lineSeparator());
		descriptionTextBuilder.append("   PRD: Choose for example the color 'red'");
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
						"abapci.preferences.coloredProjectsPreferences", null, null);
				dialog.open();
			}
		});
	}

	private void addEmptyLine(Composite container) {
		new Label(container, SWT.READ_ONLY);
	}

	private void addCheckBoxForDialogPopUp(Composite container, boolean showPopUpCheckBox) {
		Label lblShowPopUpAgain = new Label(container, SWT.READ_ONLY);
		lblShowPopUpAgain.setText("Show this dialog again, when a project without an color assignment is opened?");

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
		IProject[] projects = resourcePluginHelper.getProjects();

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