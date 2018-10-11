package abapci.handlers;

import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunch;

import com.sap.adt.atc.IAtcCheckableItem;
import com.sap.adt.atc.IAtcSetting;

public interface IAtcLauncher {
	public ILaunch launch(String var1, IAtcSetting var2, Set<IAtcCheckableItem> var3) throws CoreException;
}