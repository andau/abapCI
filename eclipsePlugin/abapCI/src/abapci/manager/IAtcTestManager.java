package abapci.manager;

import java.util.List;

import org.eclipse.core.resources.IProject;

import abapci.activation.Activation;
import abapci.domain.AbapPackageTestState;
import abapci.domain.TestState;

public interface IAtcTestManager {

	void setPackages(List<String> packageNames);

	TestState executeAllPackages(IProject currentProject,
			List<AbapPackageTestState> abapPackageTestStatesForCurrentProject, List<Activation> inactiveObjects);

}
