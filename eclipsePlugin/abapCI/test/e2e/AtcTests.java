package e2e;

import static org.junit.Assert.*;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;

import com.sap.adt.atc.ui.internal.launch.AtcLaunchShortcut;

public class AtcTests {

	@org.junit.Test
	public void testAtcLaunch() {
		
		ISelection selection = new StructuredSelection();
		//TODO decorate selection
		
		//TODO set mode 
		String mode = "";  
		
		AtcLaunchShortcut atcLaunchShortcut = new AtcLaunchShortcut();
		atcLaunchShortcut.launch(selection, mode); 
		
		fail("Assert not yet implemented");
	}

}
