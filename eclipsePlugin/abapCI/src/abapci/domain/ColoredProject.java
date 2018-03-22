package abapci.domain;

public class ColoredProject {

	private String name; 
	private UiColor uiColor;
	
	public ColoredProject(String name, UiColor uiColor) 
	{
		this.name = name; 
		this.uiColor = uiColor; 
	}
	
	public String getName()
	{
		return name; 
	}
	
	public UiColor getUiColor() {
		return uiColor; 
	}
}
