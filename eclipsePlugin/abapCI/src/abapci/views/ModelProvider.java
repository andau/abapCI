package abapci.views;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.osgi.service.prefs.BackingStoreException;

import abapci.Domain.AbapPackageTestState;

public enum ModelProvider {
    INSTANCE;

    private List<AbapPackageTestState> abapPackageTestStates;

    private ModelProvider() {

      	abapPackageTestStates = new ArrayList<AbapPackageTestState>();

      	IEclipsePreferences packageNamePrefs = ConfigurationScope.INSTANCE
                .getNode("packageNames");
  
		try {
			for (String key : packageNamePrefs.keys()) 
			{
			    abapPackageTestStates.add(new AbapPackageTestState(packageNamePrefs.get(key, "default"), "n/a", "n/a"));
			}
		} catch (BackingStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

    public List<AbapPackageTestState> getPersons() {
        return abapPackageTestStates;
    }

}
