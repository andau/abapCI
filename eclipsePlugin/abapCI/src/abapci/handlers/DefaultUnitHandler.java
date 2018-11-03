package abapci.handlers;

import java.util.Set;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;

import abapci.activation.Activation;
import abapci.testResult.TestResultSummary;

public class DefaultUnitHandler implements IUnitHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TestResultSummary executePackage(IProject project, String packageName, Set<Activation> activations) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TestResultSummary executeObjects(IProject project, String packageName, Set<Activation> activations) {
		// TODO Auto-generated method stub
		return null;
	}

}
