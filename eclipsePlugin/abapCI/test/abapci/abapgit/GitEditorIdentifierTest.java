package abapci.abapgit;

import static org.junit.Assert.assertEquals;

import org.eclipse.core.resources.IProject;
import org.junit.Test;
import org.mockito.Mockito;

import abapci.abapgit.GitEditorIdentifier;

public class GitEditorIdentifierTest {

	private static final String TEST_PROJECT_NAME = "TEST_PROJECT_NAME";
	private static final String TEST_PACKAGE_NAME = "TEST_PACKAGE_NAME";

	private static final IProject TEST_PROJECT = Mockito.mock(IProject.class);

	@Test
	public void test() {
		Mockito.when(TEST_PROJECT.getName()).thenReturn(TEST_PROJECT_NAME);
		final GitEditorIdentifier cut = new GitEditorIdentifier(TEST_PROJECT, TEST_PACKAGE_NAME);

		assertEquals(TEST_PROJECT_NAME, cut.getProject().getName());
		assertEquals(TEST_PACKAGE_NAME, cut.getPackageName());
	}

}
