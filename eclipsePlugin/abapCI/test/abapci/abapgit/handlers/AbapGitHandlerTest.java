package abapci.abapgit.handlers;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;

import com.sap.adt.project.IAdtCoreProjectService;
import com.sap.adt.projectexplorer.ui.node.AbapRepositoryBaseNode;
import com.sap.adt.sapgui.ui.editors.IAdtSapGuiEditorUtility;

import abapci.AbapCiPluginHelper;
import abapci.abapgit.GitEditorIdentifier;
import abapci.feature.activeFeature.AbapGitFeature;

public class AbapGitHandlerTest {

	private String abapGitTransactionName;

	private static final String TEST_PROJECT_NAME = "TEST_PROJECT_NAME";
	private static final String TEST_PACKAGE_NAME = "TEST_PACKAGE_NAME";
	private static final IProject ABAP_TEST_PROJECT = Mockito.mock(IProject.class);
	private static final IProject JAVA_TEST_PROJECT = Mockito.mock(IProject.class);

	private AbapGitHandler cut;

	private final IAdtCoreProjectService coreProjectService = Mockito.mock(IAdtCoreProjectService.class);
	IAdtSapGuiEditorUtility sapGuiEditorUtility = Mockito.mock(IAdtSapGuiEditorUtility.class);
	AbapGitFeature abapGitFeature = Mockito.mock(AbapGitFeature.class);
	AbapCiPluginHelper abapCiPluginHelper = Mockito.mock(AbapCiPluginHelper.class);
	AbapGitHandlerHelper abapGitHandlerHelper = Mockito.mock(AbapGitHandlerHelper.class);

	IWorkbench workbench = Mockito.mock(IWorkbench.class);
	IWorkbenchWindow workbenchWindow = Mockito.mock(IWorkbenchWindow.class);
	IWorkbenchPage workbenchPage = Mockito.mock(IWorkbenchPage.class);
	IEditorReference[] editorReferences = new IEditorReference[] {};

	@Before
	public void before() throws IllegalArgumentException, IllegalAccessException {
		cut = new AbapGitHandler();
		Whitebox.setInternalState(cut, "abapCiPluginHelper", abapCiPluginHelper);
		Whitebox.setInternalState(cut, "abapGitHandlerHelper", abapGitHandlerHelper);

		abapGitTransactionName = (String) Whitebox.getField(AbapGitHandler.class, "ABAP_GIT_TRANSACTION_NAME")
				.get(null);

		Mockito.when(coreProjectService.isAdtCoreProject(ABAP_TEST_PROJECT)).thenReturn(true);
		Mockito.when(coreProjectService.isAdtCoreProject(JAVA_TEST_PROJECT)).thenReturn(false);

		Mockito.when(abapGitHandlerHelper.getSapGuiEditorUtility()).thenReturn(sapGuiEditorUtility);
		Mockito.when(abapGitHandlerHelper.getWorkbench()).thenReturn(workbench);
		Mockito.when(abapGitHandlerHelper.getAbapGitFeature()).thenReturn(abapGitFeature);
		Mockito.when(abapGitHandlerHelper.getCoreProjectService()).thenReturn(coreProjectService);

		Mockito.when(workbench.getActiveWorkbenchWindow()).thenReturn(workbenchWindow);
		Mockito.when(workbenchWindow.getActivePage()).thenReturn(workbenchPage);
		Mockito.when(workbenchPage.getEditorReferences()).thenReturn(editorReferences);
		Mockito.when(abapGitFeature.isOnlyOneAbapGitTransactionActive()).thenReturn(false);

		Mockito.when(ABAP_TEST_PROJECT.getName()).thenReturn(TEST_PROJECT_NAME);
	}

	@Test
	public void testExecuteExecutionEvent() {
		final ExecutionEvent event = new ExecutionEvent();
		final TreeSelection treeSelection = Mockito.mock(TreeSelection.class);
		final AbapRepositoryBaseNode abapRepositoryBaseNode = Mockito.mock(AbapRepositoryBaseNode.class);

		Mockito.when(treeSelection.getFirstElement()).thenReturn(abapRepositoryBaseNode);
		Mockito.when(abapRepositoryBaseNode.getProject()).thenReturn(ABAP_TEST_PROJECT);
		Mockito.when(abapRepositoryBaseNode.getPackageName()).thenReturn(TEST_PACKAGE_NAME);

		Mockito.when(abapGitHandlerHelper.getCurrentSelection(Mockito.any(ExecutionEvent.class)))
				.thenReturn(treeSelection);

		try {
			cut.execute(event);
		} catch (final ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		final Map<String, String> params = new HashMap<>();
		params.put("p_package_name", TEST_PACKAGE_NAME);

		Mockito.verify(sapGuiEditorUtility).openEditorAndStartTransaction(ABAP_TEST_PROJECT, abapGitTransactionName,
				true, params, params);

	}

	@Test
	public void testExecuteStringNoActiveGitEditor() {

		final GitEditorIdentifier identifier = new GitEditorIdentifier(ABAP_TEST_PROJECT, TEST_PACKAGE_NAME);
		cut.execute(identifier);
		// TODO evaluate whether get or set params are necessary for package name
		// transfer in production code, currently both are set
		final Map<String, String> params = new HashMap<>();
		params.put("p_package_name", TEST_PACKAGE_NAME);

		Mockito.verify(sapGuiEditorUtility).openEditorAndStartTransaction(ABAP_TEST_PROJECT, abapGitTransactionName,
				true, params, params);
	}

	@Test
	public void testExecuteStringActiveGitEditor() {
		final IEditorPart editorPart = Mockito.mock(IEditorPart.class);

		final GitEditorIdentifier identifier = new GitEditorIdentifier(ABAP_TEST_PROJECT, TEST_PACKAGE_NAME);
		Mockito.when(abapCiPluginHelper.getParticularOrGeneralGitEditor(identifier)).thenReturn(editorPart);

		cut.execute(identifier);
		Mockito.verify(workbenchPage).activate(editorPart);

	}

	@Test
	public void testExecuteWithProjectNull() {

		final GitEditorIdentifier identifier = new GitEditorIdentifier(null, TEST_PACKAGE_NAME);
		cut.execute(identifier);

		Mockito.verify(abapGitHandlerHelper).showMessage(Mockito.any(String.class));
	}

	@Test
	public void testExecuteWithNoAbapProject() {

		final GitEditorIdentifier identifier = new GitEditorIdentifier(JAVA_TEST_PROJECT, TEST_PACKAGE_NAME);
		cut.execute(identifier);

		Mockito.verify(abapGitHandlerHelper).showMessage(Mockito.any(String.class));
	}

}
