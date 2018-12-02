package abapci.abapgit;

import static org.junit.Assert.assertEquals;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.sap.adt.projectexplorer.ui.node.AbapRepositoryBaseNode;

import abapci.utils.StringUtils;

public class GitEditorIdentifierAdapterTest {

	private static final String TEST_PROJECT_NAME = "TEST_PROJECT_NAME";
	private static final String TEST_PACKAGE_NAME = "TEST_PACKAGE_NAME";
	private static final IProject TEST_PROJECT = Mockito.mock(IProject.class);

	GitEditorIdentifierAdapter cut;

	final TreeSelection treeSelectionPackageNode = Mockito.mock(TreeSelection.class);
	final TreeSelection treeSelectionProjectNode = Mockito.mock(TreeSelection.class);
	final ISelection anySelection = Mockito.mock(ISelection.class);

	final AbapRepositoryBaseNode abapRepositoryBaseNode = Mockito.mock(AbapRepositoryBaseNode.class);
	final IProject projectNode = Mockito.mock(IProject.class);

	@Before
	public void before() {

		cut = new GitEditorIdentifierAdapter();

		Mockito.when(TEST_PROJECT.getName()).thenReturn(TEST_PROJECT_NAME);

		Mockito.when(treeSelectionPackageNode.getFirstElement()).thenReturn(abapRepositoryBaseNode);
		Mockito.when(abapRepositoryBaseNode.getProject()).thenReturn(TEST_PROJECT);
		Mockito.when(abapRepositoryBaseNode.getPackageName()).thenReturn(TEST_PACKAGE_NAME);

		Mockito.when(treeSelectionProjectNode.getFirstElement()).thenReturn(projectNode);
		Mockito.when(projectNode.getName()).thenReturn(TEST_PROJECT_NAME);
	}

	@Test
	public void testAdaptTreeSelectionPackageNode() {

		final GitEditorIdentifier identifier = cut.adapt(treeSelectionPackageNode);

		assertEquals(TEST_PROJECT_NAME, identifier.getProject().getName());
		assertEquals(TEST_PACKAGE_NAME, identifier.getPackageName());

	}

	@Test
	public void testAdaptTreeSelectionProjectNode() {

		final GitEditorIdentifier identifier = cut.adapt(treeSelectionProjectNode);

		assertEquals(TEST_PROJECT_NAME, identifier.getProject().getName());
		assertEquals(StringUtils.EMPTY, identifier.getPackageName());

	}

	@Test
	public void testAdaptNullSelection() {

		final GitEditorIdentifier identifier = cut.adapt(null);

		assertEquals(null, identifier.getProject());
		assertEquals(StringUtils.EMPTY, identifier.getPackageName());

	}

	@Test
	public void testAdaptEmptySelection() {

		Mockito.when(treeSelectionProjectNode.isEmpty()).thenReturn(true);

		final GitEditorIdentifier identifier = cut.adapt(treeSelectionProjectNode);

		assertEquals(null, identifier.getProject());
		assertEquals(StringUtils.EMPTY, identifier.getPackageName());
	}

	@Test
	public void anySelection() {

		final GitEditorIdentifier identifier = cut.adapt(anySelection);

		assertEquals(null, identifier.getProject());
		assertEquals(StringUtils.EMPTY, identifier.getPackageName());
	}

}
