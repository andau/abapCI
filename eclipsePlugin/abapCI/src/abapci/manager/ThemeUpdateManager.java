package abapci.manager;

import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

import abapci.domain.SourcecodeState;
import abapci.feature.FeatureDecision;

public class ThemeUpdateManager {
	
	private static final String STANDARD_THEME = "org.eclipse.ui.r30";
	private static final String ABAP_CI_CUSTOM_THEME = "com.abapCi.custom.theme";
	FeatureDecision featureDecision; 
	
	public ThemeUpdateManager()
	{
		featureDecision = new FeatureDecision(); 		
	}
	
	public void updateTheme(SourcecodeState sourcecodeState) {
		
		String targetTheme;
	
		switch(sourcecodeState) 
		{
		   case UT_FAIL: 
			   targetTheme = featureDecision.changeColorOnFailedTests() ?  ABAP_CI_CUSTOM_THEME : STANDARD_THEME; 
			   break; 
		   case CLEAN:
		   default: 
			   targetTheme = ABAP_CI_CUSTOM_THEME;
			   break; 
		}
		
		final String updateTargetTheme = targetTheme; 
		
		if (featureDecision.changeColorOnFailedTests()) 
		{			
			Runnable task = () ->  PlatformUI.getWorkbench().getThemeManager().setCurrentTheme(updateTargetTheme); 		
			Display.getDefault().asyncExec(task);  
		}
		
	}


}
