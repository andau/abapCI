package e2e;

import static org.junit.Assert.*;

import com.sap.adt.tools.abapsource.internal.abapunit.AbapUnitService;
import com.sap.adt.tools.abapsource.common.RestResourceFactoryFacade;
import com.sap.adt.communication.resources.IUriBuilder;
import com.sap.adt.communication.resources.UriBuilder;
import com.sap.adt.tools.abapsource.abapunit.AbapUnitTask;
import com.sap.adt.tools.abapsource.abapunit.IAbapUnitResult;
import com.sap.adt.tools.abapsource.abapunit.IAbapUnitService;
import com.sap.adt.tools.abapsource.abapunit.TestRunException;
import com.sap.adt.tools.abapsource.common.IRestResourceFactoryFacade;

public class AtcAbapUnitServiceTest  {
	
	
	@org.junit.Test
	public void testAbapUnitServiceTest() throws TestRunException 
	{
		String destination = "TBD"; 
		IRestResourceFactoryFacade resourceFactory = new RestResourceFactoryFacade(); 
		//TODO call constructor with uri 
		IUriBuilder uriBuilder = new UriBuilder(); 
		boolean enableDebugging = false; 
		
		IAbapUnitService abapUnitService = new AbapUnitService(destination, resourceFactory, uriBuilder, enableDebugging); 

		//String destination = "TBD"; 
		//IAbapUnitService abapUnitService  = AdtServicesFactory.createInstance()
		//		 .createAbapUnitService(destination, false);  
		
		AbapUnitTask abapUnitTask = null; 
		boolean arg1 = false; 
		String arg2 = "TBD"; 
		IAbapUnitResult abapUnitResult = abapUnitService.executeUnitTests(abapUnitTask, arg1, arg2); 
		
		assert(abapUnitResult.getAlerts().size() == 0); 
	}
}
