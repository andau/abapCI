package abapci.handlers;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.progress.IProgressService;

import com.sap.adt.atc.AtcCustomizingProvider;
import com.sap.adt.atc.IAtcCustomizing;
import com.sap.adt.atc.IAtcCustomizingProvider;
import com.sap.adt.tools.core.project.IAbapProject;

public class AtcCustomizingAccess {
	private static HashMap<IAbapProject, IAtcCustomizing> cache = new HashMap(10);

	public IAtcCustomizing tryToGetAssynchronous(IAbapProject abapProject)
			throws InvocationTargetException, InterruptedException, CoreException {
		IAtcCustomizing atcCustomizing = cache.get((Object) abapProject);
		if (atcCustomizing != null) {
			return atcCustomizing;
		}
		IProgressService progressService = PlatformUI.getWorkbench().getProgressService();
		ResourceRunnable backendRetrieval = new ResourceRunnable(abapProject);
		progressService.busyCursorWhile((IRunnableWithProgress) backendRetrieval);
		atcCustomizing = backendRetrieval.getCustomizing();
		cache.put(abapProject, atcCustomizing);
		return atcCustomizing;
	}

	public IAtcCustomizing getSynchronous(IAbapProject abapProject) {
		IAtcCustomizing atcCustomizing = cache.get((Object) abapProject);
		if (atcCustomizing != null) {
			return atcCustomizing;
		}
		IAtcCustomizingProvider provider = this.createCustomizingProvider();
		try {
			atcCustomizing = provider.getCustomizing(abapProject);
		} catch (CoreException coreException) {
			return null;
		}
		cache.put(abapProject, atcCustomizing);
		return atcCustomizing;
	}

	protected IAtcCustomizingProvider createCustomizingProvider() {
		return new AtcCustomizingProvider();
	}

	private class ResourceRunnable implements IRunnableWithProgress {
		private final IAbapProject abapProject;
		private CoreException logonFailure;
		private IAtcCustomizing customizing;

		ResourceRunnable(IAbapProject abapProject) {
			this.abapProject = abapProject;
		}

		public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
			AtcCustomizingProvider provider = new AtcCustomizingProvider();
			try {
				this.customizing = provider.getCustomizing(this.abapProject);
			} catch (CoreException e) {
				this.logonFailure = e;
			}
		}

		public IAtcCustomizing getCustomizing() throws CoreException {
			if (this.logonFailure != null) {
				throw this.logonFailure;
			}
			return this.customizing;
		}
	}

}