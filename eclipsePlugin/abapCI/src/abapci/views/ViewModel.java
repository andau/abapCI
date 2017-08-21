package abapci.views;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Display;
import org.osgi.service.prefs.BackingStoreException;

import abapci.Domain.AbapPackageTestState;

public enum ViewModel {
    INSTANCE;
	
	Viewer viewer; 

    private List<AbapPackageTestState> abapPackageTestStates;

    private ViewModel() {

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

    //TODO Dirty but working for the beginning - observableList should make this obsolete in future   
    public void setView(Viewer viewer) 
    {
    	INSTANCE.viewer = viewer;
    }
    
    public List<AbapPackageTestState> getPackageTestStates() {
        return abapPackageTestStates;
    }

	public void setPackageTestStates(List<AbapPackageTestState> abapPackageTestStates) {
		
		this.abapPackageTestStates = abapPackageTestStates; 
		Display.getDefault().asyncExec(new Runnable() {
		    public void run() {
		    	viewer.setInput(abapPackageTestStates);
		    }
		});
		
	}

}
