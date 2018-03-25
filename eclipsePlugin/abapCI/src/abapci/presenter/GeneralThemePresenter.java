package abapci.presenter;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

import abapci.domain.ColoredProject;
import abapci.domain.UiColor;
import abapci.domain.UiTheme;
import abapci.model.ColoredProjectModel;
import abapci.utils.ColorToThemeMapper;

public class GeneralThemePresenter {
   
    private static final String THEME_PREFIX = "com.abapCi.custom.";

    private ColoredProjectModel model; 
   
   
   public GeneralThemePresenter(ColoredProjectModel coloredProjectModel) 
   {
        this.model = coloredProjectModel;
        
        List<ColoredProject> coloredProjects = new ArrayList<>(); 
        coloredProjects.add(new ColoredProject("TESTPROJECT 1", UiColor.GREEN)); 
        coloredProjects.add(new ColoredProject("TESTPROJECT 2", UiColor.ORANGE)); 
        coloredProjects.add(new ColoredProject("TESTPROJECT 3", UiColor.LIGHT_YELLOW)); 
        
        model.saveColoredProjects(coloredProjects);
   }
   
  
   public void updateEditorLabel(String projectName) 
   {
	   UiColor uiColor = model.getColorForProject(projectName); 
	   UiTheme uiTheme = ColorToThemeMapper.mapUiColorToTheme(uiColor);
	   		
		final String changeToTheme = THEME_PREFIX + uiTheme.toString(); 
       
		Runnable task = () -> PlatformUI.getWorkbench().getThemeManager().setCurrentTheme(changeToTheme);
		Display.getDefault().asyncExec(task);				    
   }
   
}
