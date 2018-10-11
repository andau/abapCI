package abapci.feature;

import org.eclipse.core.runtime.CoreException;

import com.sap.adt.atc.IAtcSetting;

public interface IAtcProjectSettingStore {
	public void saveSetting(IAtcSetting var1) throws CoreException;

	public AtcProjectSetting readSetting();
}