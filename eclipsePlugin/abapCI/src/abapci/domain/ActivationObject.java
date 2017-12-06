package abapci.domain;

public class ActivationObject {
    public String packagename; 
	public String classname; 
	
	public ActivationObject(String packagename, String classname) 
	{
		this.packagename = packagename; 
		this.classname = classname; 
	}
	
	public String getPackagename() 
	{
		return packagename; 
	}

	public String getClassname() 
	{
		return classname; 
	}
	
}
