package abapci.handlers;

import com.sap.adt.tools.abapsource.abapunit.services.AdtServicesPlugin;
import com.sap.adt.tools.abapsource.abapunit.services.IAdtServicesFactory;

public class AdtServicePluginHelper {

	public IAdtServicesFactory getServiceFactory() {
		return AdtServicesPlugin.getDefault().getFactory();
	}

}
