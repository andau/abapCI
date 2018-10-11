package abapci.handlers;

import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchManager;

import com.sap.adt.atc.IAtcCheckableItem;
import com.sap.adt.atc.IAtcSetting;

public class AtcLauncher implements IAtcLauncher {
	private final ILaunchManager launchManager;
	private final AtcLaunchConfigurationSupport configSupport;
	IProgressMonitor monitor;

	public AtcLauncher(ILaunchManager launchManager) {
		this.launchManager = launchManager;
		this.configSupport = new AtcLaunchConfigurationSupport(launchManager);
	}

	public ILaunch launch(String projectName, IAtcSetting projectSetting, Set<IAtcCheckableItem> adtItems)
			throws CoreException {
		ILaunchConfiguration configuration = this.configSupport.tryToGetExistingConfig(projectName, projectSetting,
				adtItems);
		if (configuration == null) {
			configuration = this.configSupport.createConfiguration(projectName, projectSetting, adtItems);
		}
		return this.createLaunchAndAddToHistory(configuration);
	}

	private ILaunch createLaunchAndAddToHistory(ILaunchConfiguration configuration) throws CoreException {
		ILaunch launch = configuration.launch("run", null, false, false);
		this.launchManager.addLaunch(launch);
		this.launchManager.removeLaunch(launch);
		return launch;
	}
}