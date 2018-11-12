package abapci.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import abapci.AbapCiPlugin;

public class ColoredProjectsPreferences extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	final static Color HEADER_COLOR = new Color(Display.getCurrent(), 0, 0, 255);
	PreferencesUiHelper preferencesUiHelper;

	public ColoredProjectsPreferences() {
		super(GRID);
		preferencesUiHelper = new PreferencesUiHelper();

	}

	@Override
	public void createFieldEditors() {

		createColorChangeChapter();

	}

	private void createColorChangeChapter() {

		preferencesUiHelper.addHeaderLabelWithSpaceBefore(getFieldEditorParent(), "Colored status bar");

		addField(new BooleanFieldEditor(PreferenceConstants.PREF_COLORED_PROJECTS_STATUS_BAR_WIDGET_ENABLED,
				"&Add a colored widget to the status bar", getFieldEditorParent()));

		addField(new BooleanFieldEditor(PreferenceConstants.PREF_COLORED_PROJECTS_STATUS_BAR_ENABLED,
				"&Change color of entire status bar", getFieldEditorParent()));

		preferencesUiHelper.addHeaderLabelWithSpaceBefore(getFieldEditorParent(), "Colored Editor title icon");

		addField(new BooleanFieldEditor(PreferenceConstants.PREF_COLORED_PROJECTS_TITLE_ICON_ENABLED,
				"&Add a rectangle to the right bottom of the editor title icon", getFieldEditorParent()));
		addField(new IntegerFieldEditor(PreferenceConstants.PREF_COLORED_PROJECTS_TITLE_ICON_WIDTH_PERCENT,
				"&Width of rectangle in percent of the icon width", getFieldEditorParent()));

		addField(new IntegerFieldEditor(PreferenceConstants.PREF_COLORED_PROJECTS_TITLE_ICON_HEIGTH_PERCENT,
				"&Height of rectangle in percent of the icon height", getFieldEditorParent()));

		preferencesUiHelper.addHeaderLabelWithSpaceBefore(getFieldEditorParent(), "Colored Texteditors");

		addField(new BooleanFieldEditor(PreferenceConstants.PREF_COLORED_PROJECTS_LEFT_RULER_ENABLED,
				"&Change color of the left ruler of text editors", getFieldEditorParent()));

		addField(new BooleanFieldEditor(PreferenceConstants.PREF_COLORED_PROJECTS_RIGHT_RULER_ENABLED,
				"&Change color of right ruler inside the editor", getFieldEditorParent()));

		preferencesUiHelper.addEmptyLabel(getFieldEditorParent());

		addField(new BooleanFieldEditor(PreferenceConstants.PREF_COLORED_PROJECTS_NEW_DIALOG_ENABLED,
				"&Show dialog when a new project without a color confinguration is detected", getFieldEditorParent()));

	}

	@Override
	public void init(IWorkbench workbench) {
		setPreferenceStore(AbapCiPlugin.getDefault().getPreferenceStore());
		setDescription("Settings for colored projects");
	}

}