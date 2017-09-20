package unit;

import java.math.BigDecimal;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import com.sap.adt.tools.abapsource.abapunit.AbapUnitResultItemType;
import com.sap.adt.tools.abapsource.abapunit.AbapUnitTimeUnitKind;
import com.sap.adt.tools.abapsource.abapunit.IAbapUnitAlert;
import com.sap.adt.tools.abapsource.abapunit.IAbapUnitResultItem;

public class AbapUnitResultItemMock implements IAbapUnitResultItem {

	private List<IAbapUnitResultItem> childItems = new ArrayList<IAbapUnitResultItem>();
	private List<IAbapUnitAlert> alerts = new ArrayList<IAbapUnitAlert>(); 
	
	@Override
	public List<IAbapUnitAlert> getAlerts() {
		return alerts; 
	}
	
	public void setAlert(IAbapUnitAlert abapUnitAlert) 
	{
		alerts.add(abapUnitAlert); 
	}

	@Override
	public List<IAbapUnitResultItem> getChildItems() {
		return childItems; 
	}
	
	public void addChildItem(IAbapUnitResultItem resultItem) 
	{
		childItems.add(resultItem); 
	}

	@Override
	public BigDecimal getExecutionTime() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbapUnitResultItemType getType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbapUnitTimeUnitKind getUnitofTime() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public URI getUri() {
		// TODO Auto-generated method stub
		return null;
	}

}
