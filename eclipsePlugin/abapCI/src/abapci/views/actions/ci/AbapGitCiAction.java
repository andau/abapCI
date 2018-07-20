package abapci.views.actions.ci;

import abapci.AbapCiPlugin;
import abapci.handlers.AbapGitHandler;

public class AbapGitCiAction extends AbstractCiAction {

	private static final String ICONS_GIT = "icons/git.ico";

	public AbapGitCiAction(String label, String tooltip) {
		this.setText(label);
		this.setToolTipText(tooltip);
		this.setImageDescriptor(AbapCiPlugin.getImageDescriptor(ICONS_GIT));
	}

	public void run() {
		new AbapGitHandler().execute("");
	}

}
