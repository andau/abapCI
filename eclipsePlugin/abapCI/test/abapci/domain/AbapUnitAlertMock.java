package abapci.domain;

import java.util.List;

import com.sap.adt.tools.abapsource.abapunit.AbapUnitAlertKind;
import com.sap.adt.tools.abapsource.abapunit.AbapUnitAlertSeverity;
import com.sap.adt.tools.abapsource.abapunit.IAbapUnitAlert;
import com.sap.adt.tools.abapsource.abapunit.IAbapUnitAlertDetail;
import com.sap.adt.tools.abapsource.abapunit.IAbapUnitAlertStackEntry;

public class AbapUnitAlertMock implements IAbapUnitAlert {

	@Override
	public List<IAbapUnitAlertDetail> getDetails() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbapUnitAlertKind getKind() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbapUnitAlertSeverity getSeverity() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<IAbapUnitAlertStackEntry> getStackEntries() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return null;
	}

}
