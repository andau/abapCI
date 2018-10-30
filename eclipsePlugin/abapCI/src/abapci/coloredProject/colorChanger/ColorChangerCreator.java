package abapci.coloredProject.colorChanger;

import org.eclipse.ui.IEditorPart;

import abapci.feature.ColoredProjectFeature;

public interface  ColorChangerCreator {
    public ColorChanger create(IEditorPart editorPart, ColoredProjectFeature coloredProjectFeature);  
}
