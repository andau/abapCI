package abapci.abapgit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.eclipse.core.resources.IProject;
import org.eclipse.ui.IEditorPart;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class ActiveGitEditorsTest {

	private static final String TEST_PACKAGE_NAME_ONE = "TEST_PACKAGE_NAME_ONE";
	private static final String TEST_PACKAGE_NAME_TWO = "TEST_PACKAGE_NAME_TWO";
	private static final String TEST_PROJECT_NAME = "TEST_PROJECT_NAME";

	private static final IProject TEST_PROJECT = Mockito.mock(IProject.class);
	private static final String TEST_EDITOR_TITLE = "TEST_EDITOR_TITLE";

	final IEditorPart editorPartOne = Mockito.mock(IEditorPart.class);
	final IEditorPart editorPartTwo = Mockito.mock(IEditorPart.class);

	ActiveGitEditors cut = new ActiveGitEditors();
	GitEditorIdentifier identifierOne = new GitEditorIdentifier(TEST_PROJECT, TEST_PACKAGE_NAME_ONE);
	GitEditorIdentifier identifierTwo = new GitEditorIdentifier(TEST_PROJECT, TEST_PACKAGE_NAME_TWO);

	@Before
	public void before() {
		Mockito.when(TEST_PROJECT.getName()).thenReturn(TEST_PROJECT_NAME);
		Mockito.when(editorPartOne.getTitle()).thenReturn(TEST_EDITOR_TITLE);
	}

	@Test
	void testInitial() {
		assertFalse(cut.isActive(identifierOne));
	}

	@Test
	void testAddOneEntry() {
		assertFalse(cut.isActive(identifierOne));
		cut.addEditor(identifierOne, editorPartOne);
		assertTrue(cut.isActive(identifierOne));
	}

	@Test
	void testAddTwoEntries() {
		assertFalse(cut.isActive(identifierOne));
		assertFalse(cut.isActive(identifierTwo));

		cut.addEditor(identifierOne, editorPartOne);

		assertTrue(cut.isActive(identifierOne));
		assertFalse(cut.isActive(identifierTwo));

		cut.addEditor(identifierOne, editorPartOne);
		cut.addEditor(identifierTwo, editorPartTwo);

		assertTrue(cut.isActive(identifierOne));
		assertTrue(cut.isActive(identifierTwo));
	}

	@Test
	void testGetEntry() {
		assertNull(cut.getEditor(identifierOne));
		cut.addEditor(identifierOne, editorPartOne);
		assertNotNull(cut.getEditor(identifierOne));

		final IEditorPart editorPart = cut.getEditor(identifierOne);
		assertEquals(editorPartOne.getTitle(), editorPart.getTitle());
	}

	@Test
	void testAddGeneralEditor() {
		assertNull(cut.getEditor(null));
		cut.addEditor(null, editorPartOne);
		assertNotNull(cut.getEditor(null));

	}
}
