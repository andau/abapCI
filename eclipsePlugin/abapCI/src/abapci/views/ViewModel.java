package abapci.views;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.osgi.service.prefs.BackingStoreException;

import abapci.domain.AbapPackageTestState;
import abapci.domain.GlobalTestState;
import abapci.domain.TestState;
import abapci.lang.UiTexts;

public enum ViewModel {
    INSTANCE;
	
	Viewer viewer; 
	Label lblOverallTestState; 

    private List<AbapPackageTestState> abapPackageTestStates;
    private TestState overallTestState; 

    private ViewModel() {

      	abapPackageTestStates = new ArrayList<AbapPackageTestState>();

      	IEclipsePreferences packageNamePrefs = ConfigurationScope.INSTANCE
                .getNode("packageNames");
  
		try {
			for (String key : packageNamePrefs.keys()) 
			{
			    abapPackageTestStates.add(new AbapPackageTestState(packageNamePrefs.get(key, "default"), 
			    		UiTexts.NOT_YET_EXECUTED, UiTexts.NOT_YET_EXECUTED, UiTexts.NOT_YET_EXECUTED));
			}
		} catch (BackingStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		overallTestState = TestState.UNDEF; 
		
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

	public void setGlobalTestState(GlobalTestState globalTestState) {	
		Display.getDefault().asyncExec(new Runnable() {
		    public void run() {
		    	lblOverallTestState.setText(globalTestState.getTestStateOutputForDashboard()); 
		    	lblOverallTestState.setBackground(globalTestState.getColor());
		    }
		});		
	}

	public TestState getOverallTestState() {
		return INSTANCE.overallTestState; 
	}

	public void setLblOverallTestState(Label lblOverallTestState) {
		INSTANCE.lblOverallTestState = lblOverallTestState; 	
	}

}
