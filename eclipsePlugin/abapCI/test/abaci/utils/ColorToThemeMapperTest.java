package abaci.utils;

import static org.junit.Assert.*;

import org.junit.Test;

import abapci.domain.UiColor;
import abapci.domain.UiTheme;
import abapci.utils.ColorToThemeMapper;

public class ColorToThemeMapperTest {
	
	@Test
	public void mappingCompleteTest() 
	{
	   for(UiColor uiColor : UiColor.values()) 
	   {
		   UiTheme uiTheme = ColorToThemeMapper.mapUiColorToTheme(uiColor);  
		   assertNotNull(uiTheme);
		   if(uiColor != UiColor.DEFAULT && uiColor != UiColor.STANDARD)
		   {
			   assertNotEquals(UiTheme.STANDARD_THEME, uiTheme);			   
		   }
	   }
	}
	
	
}
