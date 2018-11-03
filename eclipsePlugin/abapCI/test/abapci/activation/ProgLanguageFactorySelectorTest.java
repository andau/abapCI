package abapci.activation;

import static org.junit.Assert.*;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.eclipse.jdt.core.JavaCore;

public class ProgLanguageFactorySelectorTest {

	IEditorReference editorReference = Mockito.mock(IEditorReference.class);
	IEditorPart editorPart = Mockito.mock(IEditorPart.class); 
	IEditorInput editorInput = Mockito.mock(IEditorInput.class); 
	IProject project = Mockito.mock(IProject.class);

	@Before 
	public void before() {
		
	    Mockito.when(editorReference.isDirty()).thenReturn(true); 
	    Mockito.when(editorReference.getEditor(false)).thenReturn(editorPart); 
	    Mockito.when(editorPart.getEditorInput()).thenReturn(editorInput); 
	    Mockito.when(editorInput.getAdapter(IProject.class)).thenReturn(project); 	
	}
	
	@Test
	public void testDetermineAbapLanguageFactory() throws CoreException {
		
		ProgLanguageFactorySelector progLanguageFactorySelector = new ProgLanguageFactorySelector(); 
		IEditorReference[] editorReferences = new IEditorReference[1];

		ILanguageFactory languageFactory = progLanguageFactorySelector.determine(ActivationAction.ACTION_ABAP_SINGLE_ACTIVATION, editorReferences ); 
		assertTrue(languageFactory instanceof AbapLanguageFactory); 
		
		languageFactory = progLanguageFactorySelector.determine(ActivationAction.ACTION_ABAP_MULTI_ACTIVATION, editorReferences ); 
	    assertTrue(languageFactory instanceof AbapLanguageFactory); 


	    editorReferences[0] = editorReference; 
    
	    Mockito.when(project.hasNature("com.sap.adt.abapnature")).thenReturn(true); 	
		languageFactory = progLanguageFactorySelector.determine(ActivationAction.ACTION_FILE_SAVE, editorReferences ); 
	    assertTrue(languageFactory instanceof AbapLanguageFactory); 

	}

	@Test
	public void testDetermineJavaLanguageFactory() throws CoreException {

		ProgLanguageFactorySelector progLanguageFactorySelector = new ProgLanguageFactorySelector(); 
		IEditorReference[] editorReferences = new IEditorReference[1];

	    editorReferences[0] = editorReference; 
	    Mockito.when(project.hasNature(JavaCore.NATURE_ID)).thenReturn(true); 	
	    
	    ILanguageFactory  languageFactory = progLanguageFactorySelector.determine(ActivationAction.ACTION_FILE_SAVE, editorReferences ); 
	    assertTrue(languageFactory instanceof JavaLanguageFactory); 

	}


	@Test
	public void testDetermineDefaultLanguageFactory() throws CoreException {

		ProgLanguageFactorySelector progLanguageFactorySelector = new ProgLanguageFactorySelector(); 
		IEditorReference[] editorReferences = new IEditorReference[1];

	    editorReferences[0] = editorReference; 
	    ILanguageFactory  languageFactory = progLanguageFactorySelector.determine(ActivationAction.ACTION_FILE_SAVE, editorReferences ); 
	    assertTrue(languageFactory instanceof DefaultLanguageFactory); 

	}

}
