package abapci.handlers;

import java.util.List;

import org.eclipse.core.resources.IProject;

import com.sap.adt.atc.IAtcCheckableItem;

public interface IAtcWorklistService {
	public void checkItems(List<IAtcCheckableItem> var1, IProject var2, String var3);

	public void checkItemsWithSystemDefaultCheckVariant(List<IAtcCheckableItem> var1, IProject var2);

	public void checkItemsWithinWorklist(List<IAtcCheckableItem> var1, String var2);

	public void discardJobsOfWorklist(String var1);

	public void getWorklistWithObjectSetComplete(String var1, IProject var2, String var3);
}