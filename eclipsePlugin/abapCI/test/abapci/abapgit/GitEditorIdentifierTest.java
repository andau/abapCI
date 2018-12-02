package abapci.abapgit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.eclipse.core.resources.IProject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class GitEditorIdentifierTest {

	private static final GitEditorIdentifier GIT_EDITOR_IDENTIFIER_NULL = new GitEditorIdentifier(null, null);
	private static final GitEditorIdentifier GIT_EDITOR_IDENTIFIER_NULL_DUPL = new GitEditorIdentifier(null, null);
	private static final String TEST_PROJECT_NAME = "TEST_PROJECT_NAME";
	private static final String TEST_PACKAGE_NAME = "TEST_PACKAGE_NAME";
	private static final GitEditorIdentifier GIT_EDITOR_IDENTIFIER_NO_PROJECT = new GitEditorIdentifier(null,
			TEST_PACKAGE_NAME);
	private static final GitEditorIdentifier GIT_EDITOR_IDENTIFIER_NO_PROJECT_DUPL = new GitEditorIdentifier(null,
			TEST_PACKAGE_NAME);

	private static final IProject TEST_PROJECT = Mockito.mock(IProject.class);
	private static final GitEditorIdentifier GIT_EDITOR_IDENTIFIER_OTHER = new GitEditorIdentifier(TEST_PROJECT,
			TEST_PACKAGE_NAME + "_OTHER");
	private static final GitEditorIdentifier GIT_EDITOR_IDENTIFIER_FULL_SPECIFIED = new GitEditorIdentifier(
			TEST_PROJECT, TEST_PACKAGE_NAME);
	private static final GitEditorIdentifier GIT_EDITOR_IDENTIFIER_FULL_SPECIFIED_DUPL = new GitEditorIdentifier(
			TEST_PROJECT, TEST_PACKAGE_NAME);

	@Before
	public void before() {
		Mockito.when(TEST_PROJECT.getName()).thenReturn(TEST_PROJECT_NAME);
	}

	@Test
	public void testCreate() {
		final GitEditorIdentifier cut = GIT_EDITOR_IDENTIFIER_FULL_SPECIFIED;

		assertEquals(TEST_PROJECT_NAME, cut.getProject().getName());
		assertEquals(TEST_PACKAGE_NAME, cut.getPackageName());
	}

	@Test
	public void testEqual() {
		assertEquals(GIT_EDITOR_IDENTIFIER_FULL_SPECIFIED, GIT_EDITOR_IDENTIFIER_FULL_SPECIFIED_DUPL);

		assertEquals(GIT_EDITOR_IDENTIFIER_NO_PROJECT, GIT_EDITOR_IDENTIFIER_NO_PROJECT_DUPL);
		assertEquals(GIT_EDITOR_IDENTIFIER_NO_PROJECT.hashCode(), GIT_EDITOR_IDENTIFIER_NO_PROJECT_DUPL.hashCode());

		assertEquals(GIT_EDITOR_IDENTIFIER_NULL, GIT_EDITOR_IDENTIFIER_NULL_DUPL);
		assertEquals(GIT_EDITOR_IDENTIFIER_NULL.hashCode(), GIT_EDITOR_IDENTIFIER_NULL_DUPL.hashCode());

		assertEquals(GIT_EDITOR_IDENTIFIER_FULL_SPECIFIED.hashCode(),
				GIT_EDITOR_IDENTIFIER_FULL_SPECIFIED_DUPL.hashCode());

		assertNotEquals(GIT_EDITOR_IDENTIFIER_FULL_SPECIFIED, GIT_EDITOR_IDENTIFIER_OTHER);
		assertNotEquals(GIT_EDITOR_IDENTIFIER_FULL_SPECIFIED_DUPL.hashCode(), GIT_EDITOR_IDENTIFIER_OTHER.hashCode());

		assertNotEquals(GIT_EDITOR_IDENTIFIER_NO_PROJECT, GIT_EDITOR_IDENTIFIER_FULL_SPECIFIED);
		assertNotEquals(GIT_EDITOR_IDENTIFIER_NO_PROJECT, new GitEditorIdentifier(TEST_PROJECT, null));

	}

}
