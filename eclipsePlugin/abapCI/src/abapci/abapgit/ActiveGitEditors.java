package abapci.abapgit;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.ui.IEditorPart;

public class ActiveGitEditors {

	private final Map<GitEditorIdentifier, IEditorPart> activeEditors;

	public ActiveGitEditors() {
		activeEditors = new HashMap<>();
	}

	public boolean isActive(GitEditorIdentifier identifier) {
		return activeEditors.containsKey(identifier);

	}

	public void addEditor(GitEditorIdentifier identifier, IEditorPart editorPart) {
		activeEditors.put(identifier, editorPart);
	}

	public IEditorPart getEditor(GitEditorIdentifier identifier) {
		return activeEditors.get(identifier);
	}

}
