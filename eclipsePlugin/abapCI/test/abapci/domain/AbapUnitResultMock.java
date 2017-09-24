package abapci.domain;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import com.sap.adt.tools.abapsource.abapunit.IAbapUnitAlert;
import com.sap.adt.tools.abapsource.abapunit.IAbapUnitResult;
import com.sap.adt.tools.abapsource.abapunit.IAbapUnitResultItem;

public class AbapUnitResultMock implements IAbapUnitResult 
{
	private List<IAbapUnitAlert> alerts = new ArrayList<IAbapUnitAlert>(); 
	private List<IAbapUnitResultItem> items = new ArrayList<IAbapUnitResultItem>(); 

	public void addAlert(IAbapUnitAlert abapUnitAlert) 
	{
		alerts.add(abapUnitAlert); 
	}

	@Override
	public List<IAbapUnitAlert> getAlerts() {
		// TODO Auto-generated method stub
		return alerts;
	}

	@Override
	public List<IAbapUnitResultItem> getItems() {
		return items; 
	}
	
	public void addItem(IAbapUnitResultItem resultItem) 
	{
		items.add(resultItem);
    }

	@Override
	public URI getCoverageMeasurementUri() {
		// TODO Auto-generated method stub
		return null;
	}
}