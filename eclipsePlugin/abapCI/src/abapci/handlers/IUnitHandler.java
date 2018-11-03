package abapci.handlers;

import java.util.Set;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;

import abapci.activation.Activation;
import abapci.testResult.TestResultSummary;

public interface IUnitHandler {
	public Object execute(ExecutionEvent event) throws ExecutionException; 
	public TestResultSummary executePackage(IProject project, String packageName, Set<Activation> activations); 
	public TestResultSummary executeObjects(IProject project, String packageName, Set<Activation> activations); 
}
