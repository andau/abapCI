package abapci.handlers;

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

public class AbapGitHandlerTest {

	private String abapGitTransactionName;

	private static final String TEST_PROJECT_NAME = "TEST_PROJECT_NAME";
	private static final String TEST_PACKAGE_NAME = "TEST_PACKAGE_NAME";
	private AbapGitHandler cut;

	private final IAdtCoreProjectService coreProjectService = Mockito.mock(IAdtCoreProjectService.class);
	IAdtSapGuiEditorUtility sapGuiEditorUtility = Mockito.mock(IAdtSapGuiEditorUtility.class);

	IWorkbench workbench = Mockito.mock(IWorkbench.class);
	IWorkbenchWindow workbenchWindow = Mockito.mock(IWorkbenchWindow.class);
	IWorkbenchPage workbenchPage = Mockito.mock(IWorkbenchPage.class);
	IEditorReference[] editorReferences = new IEditorReference[] {};

	private final IProject testProject = Mockito.mock(IProject.class);

	@Before
	public void before() throws IllegalArgumentException, IllegalAccessException {
		cut = new AbapGitHandler(true);
		Whitebox.setInternalState(cut, "coreProjectService", coreProjectService);
		Whitebox.setInternalState(cut, "workbench", workbench);
		Whitebox.setInternalState(cut, "sapGuiEditorUtility", sapGuiEditorUtility);

		abapGitTransactionName = (String) Whitebox.getField(AbapGitHandler.class, "ABAP_GIT_TRANSACTION_NAME")
				.get(null);

		Mockito.when(coreProjectService.findProject(TEST_PROJECT_NAME)).thenReturn(testProject);

		Mockito.when(workbench.getActiveWorkbenchWindow()).thenReturn(workbenchWindow);
		Mockito.when(workbenchWindow.getActivePage()).thenReturn(workbenchPage);
		Mockito.when(workbenchPage.getEditorReferences()).thenReturn(editorReferences);
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
		cut.execute(TEST_PROJECT_NAME, TEST_PACKAGE_NAME);
		// TODO decide whether get or set params should be used for package name
		// transfer
		final Map<String, String> params = new HashMap<>();
		params.put("PACKAGE_NAME", TEST_PACKAGE_NAME);

		Mockito.verify(sapGuiEditorUtility).openEditorAndStartTransaction(testProject, abapGitTransactionName, true,
				params, params);
	}

}
