package abapci.xml;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

public class XmlFileLocationHelper {

	public IPath getStateLocation() {
		Bundle bundle = FrameworkUtil.getBundle(abapci.coloredProject.view.AbapCiColoredProjectView.class);
		return Platform.getStateLocation(bundle);
	}

}
