package abapci.views.actions.ci;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

import abapci.AbapCiPlugin;
import abapci.GeneralProjectUtil;
import abapci.abapgit.GitEditorIdentifier;
import abapci.abapgit.handlers.AbapGitHandler;
import abapci.ci.presenter.ContinuousIntegrationPresenter;
import abapci.domain.AbapPackageTestState;

public class AbapGitCiAction extends AbstractCiAction {

	private static final String ICONS_GIT = "icons/abapgit.png";

	public AbapGitCiAction(ContinuousIntegrationPresenter continuousIntegrationPresenter, String label,
			String tooltip) {
		this.setText(label);
		this.setToolTipText(tooltip);
		this.setImageDescriptor(AbapCiPlugin.getImageDescriptor(ICONS_GIT));
		this.continuousIntegrationPresenter = continuousIntegrationPresenter;
	}

	@Override
	public void run() {
		for (final AbapPackageTestState abapPackageTestState : getSelectedAbapPackageTestStates()) {

			try {
				final IProject project = GeneralProjectUtil
						.getAbapProjectByProjectName(abapPackageTestState.getProjectName());
				final String packageName = abapPackageTestState.getPackageName();

				final GitEditorIdentifier identifier = new GitEditorIdentifier(project, packageName);
				new AbapGitHandler().execute(identifier);

			} catch (final Exception ex) {
				final String errormessage = String.format("Error while opening abapGit, errormessage: %s",
						ex.getMessage());
				continuousIntegrationPresenter.setStatusMessage(errormessage,
						new Color(Display.getCurrent(), new RGB(255, 0, 0)));
			}

		}
	}

}
