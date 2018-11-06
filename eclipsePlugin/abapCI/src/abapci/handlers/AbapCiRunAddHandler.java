package abapci.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import com.sap.adt.projectexplorer.ui.node.AbapRepositoryBaseNode;

import abapci.AbapCiPlugin;
import abapci.domain.ContinuousIntegrationConfig;
import abapci.presenter.ContinuousIntegrationPresenter;
import abapci.views.wizard.AddOrUpdateContinuousIntegrationConfigPage;

public class AbapCiRunAddHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		ISelection selection = HandlerUtil.getCurrentSelection(event);
		if (!selection.isEmpty()) {
			if (selection instanceof TreeSelection) {
				if (!((TreeSelection) selection).isEmpty()
						&& ((TreeSelection) selection).getFirstElement() instanceof AbapRepositoryBaseNode) {
					AbapRepositoryBaseNode packageNode = (AbapRepositoryBaseNode) ((TreeSelection) selection)
							.getFirstElement();

					IProject project = packageNode.getProject();

					ContinuousIntegrationConfig ciConfig = new ContinuousIntegrationConfig(project.getName(),
							packageNode.getPackageName(), true, true);

					ContinuousIntegrationPresenter continuousIntegrationPresenter = AbapCiPlugin
							.getDefault().continuousIntegrationPresenter;
					AddOrUpdateContinuousIntegrationConfigPage addContinuousIntegrationConfigPage = new AddOrUpdateContinuousIntegrationConfigPage(
							PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
							continuousIntegrationPresenter, ciConfig, false);
					if (addContinuousIntegrationConfigPage.open() == Window.OK) {
						continuousIntegrationPresenter.setStatusMessage(
								String.format("Package %s added to CI Run", packageNode.getProject().getName()));
					}

				}
			}
		}
		return null;
	}
}
