package abapci.coloredProject.colorChanger;

import java.lang.reflect.Method;

import org.eclipse.jface.text.ITextViewer;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.texteditor.AbstractTextEditor;

import abapci.Exception.ActiveEditorNotSetException;

public abstract class ARulerColorChanger extends ColorChanger {

	protected IEditorPart editorPart;

	public ARulerColorChanger(IEditorPart editorPart) 
	{
		this.editorPart = editorPart; 
	}

	public abstract void change() throws ActiveEditorNotSetException; 

	protected ITextViewer getTextViewer() throws ActiveEditorNotSetException 
	{
		if (editorPart == null) 
		{
			throw new ActiveEditorNotSetException(); 
		}
		else 
		{
			ITextViewer textViewer = null;
			Object activeEditor;

			if (editorPart instanceof AbstractTextEditor) {
				activeEditor = editorPart;
			} else {
				activeEditor = ((MultiPageEditorPart) editorPart).getSelectedPage();
			}
			if (activeEditor instanceof AbstractTextEditor) {
				try {
					textViewer = callGetSourceViewer((AbstractTextEditor) activeEditor);
				} catch (Exception e) {
					//if editor is no textviewer null is intentionally returned 
				}
			}
			
			return textViewer; 			
		}
	}
	
	private ITextViewer callGetSourceViewer(AbstractTextEditor editor) throws Exception {
		try {
			Method method = AbstractTextEditor.class.getDeclaredMethod("getSourceViewer"); //$NON-NLS-1$
			method.setAccessible(true);

			return (ITextViewer) method.invoke(editor);
		} catch (NullPointerException npe) {
			return null;
		}
	}


}
