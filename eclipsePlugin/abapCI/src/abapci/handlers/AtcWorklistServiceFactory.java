package abapci.handlers;

import com.sap.adt.atc.ui.internal.IAtcWorklistService;
import com.sap.adt.atc.ui.internal.launch.worklist.AtcWorklistService;

public class AtcWorklistServiceFactory {
	public static IAtcWorklistService getAtcWorklistService() {
		AtcWorklistService service = new AtcWorklistService();
		return service;
	}
}