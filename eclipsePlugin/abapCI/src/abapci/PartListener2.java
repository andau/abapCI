package abapci;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.ViewReference;

import abapci.presenter.GeneralThemePresenter;

@SuppressWarnings("restriction")
public class PartListener2 implements IPartListener2 {
	private GeneralThemePresenter generalThemePresenter;

	public PartListener2(GeneralThemePresenter generalThemePresenter) {
		this.generalThemePresenter = generalThemePresenter;
	}

	@Override
	public void partActivated(IWorkbenchPartReference arg0) {
		IProject currentProject = null;

		IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();

		IEditorPart activeEditor = activePage.getActiveEditor();
		IWorkbenchPartReference partReference = activePage.getActivePartReference();
		if (!partReference.getClass().equals(ViewReference.class) && (activeEditor != null)) {
			IEditorInput input = activeEditor.getEditorInput();
			currentProject = input.getAdapter(IProject.class);
			if (currentProject == null) {
				IResource resource = input.getAdapter(IResource.class);
				if (resource != null) {
					currentProject = resource.getProject();
				}
			}
		}
		String currentProjectname = (currentProject == null) ? "UNDEF" : currentProject.getName();
		setProjectColor(currentProjectname);
	}

	private void setProjectColor(String projectName) {
		generalThemePresenter.updateEditorLabel(projectName);
	}

	@Override
	public void partBroughtToTop(IWorkbenchPartReference arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void partClosed(IWorkbenchPartReference arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void partDeactivated(IWorkbenchPartReference arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void partHidden(IWorkbenchPartReference arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void partInputChanged(IWorkbenchPartReference arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void partOpened(IWorkbenchPartReference arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void partVisible(IWorkbenchPartReference arg0) {
		// TODO Auto-generated method stub

	}
}
