package abapci.abapgit.handlers;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.ISources;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;

import com.sap.adt.project.IAdtCoreProjectService;
import com.sap.adt.sapgui.ui.editors.IAdtSapGuiEditorUtility;

import abapci.AbapCiPluginHelper;
import abapci.feature.activeFeature.AbapGitFeature;

public class AbapGitHandlerTest {

	private String abapGitTransactionName;

	private static final String TEST_PROJECT_NAME = "TEST_PROJECT_NAME";
	private static final String TEST_PACKAGE_NAME = "TEST_PACKAGE_NAME";
	private static final IProject TEST_PROJECT = Mockito.mock(IProject.class);

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

		Mockito.when(coreProjectService.findProject(TEST_PROJECT_NAME)).thenReturn(TEST_PROJECT);

		Mockito.when(abapGitHandlerHelper.getSapGuiEditorUtility()).thenReturn(sapGuiEditorUtility);
		Mockito.when(abapGitHandlerHelper.getWorkbench()).thenReturn(workbench);
		Mockito.when(abapGitHandlerHelper.getAbapGitFeature()).thenReturn(abapGitFeature);

		Mockito.when(workbench.getActiveWorkbenchWindow()).thenReturn(workbenchWindow);
		Mockito.when(workbenchWindow.getActivePage()).thenReturn(workbenchPage);
		Mockito.when(workbenchPage.getEditorReferences()).thenReturn(editorReferences);
		Mockito.when(abapGitFeature.isOnlyOneAbapGitTransactionActive()).thenReturn(false);

		Mockito.when(TEST_PROJECT.getName()).thenReturn(TEST_PROJECT_NAME);
	}

	@Ignore("ExecutionEvent is not mockable with mockito standard")
	@Test
	public void testExecuteExecutionEvent() {
		final ExecutionEvent event = Mockito.mock(ExecutionEvent.class);
		final IEvaluationContext evaluationContext = Mockito.mock(IEvaluationContext.class);
		final TreeSelection treeSelection = Mockito.mock(TreeSelection.class);

		Mockito.when(event.getApplicationContext()).thenReturn(evaluationContext);
		Mockito.when(evaluationContext.getVariable(ISources.ACTIVE_CURRENT_SELECTION_NAME)).thenReturn(treeSelection);

		try {
			cut.execute(event);
		} catch (final ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testExecuteString() {
		cut.execute(TEST_PROJECT, TEST_PACKAGE_NAME);
		// TODO decide whether get or set params should be used for package name
		// transfer
		final Map<String, String> params = new HashMap<>();
		params.put("p_package_name", TEST_PACKAGE_NAME);

		Mockito.verify(sapGuiEditorUtility).openEditorAndStartTransaction(TEST_PROJECT, abapGitTransactionName, true,
				params, params);
	}

}
