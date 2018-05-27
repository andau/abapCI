package abapci.feature;

public class SourcecodeFormattingFeature extends ActiveFeature {
	private String prefix;

	public SourcecodeFormattingFeature(String prefix) 
	{
		this.prefix = prefix;		
	}
	
	public SourcecodeFormattingFeature() 
	{
		throw new UnsupportedOperationException(); 
	}
	
	
	public String getPrefix() {
		return prefix;
	}

}
