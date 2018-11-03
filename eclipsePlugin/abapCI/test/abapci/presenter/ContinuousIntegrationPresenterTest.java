package abapci.presenter;

import org.eclipse.core.resources.IProject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import abapci.domain.ContinuousIntegrationConfig;
import abapci.model.IContinuousIntegrationModel;
import abapci.views.AbapCiMainView;

public class ContinuousIntegrationPresenterTest {

	private static final String TEST_PROJECT_NAME = "TEST_PROJECT_NAME";
	private static final String TEST_PACKAGE = "TEST_PROJECT_PACKAGE";

	ContinuousIntegrationPresenter cut;

	AbapCiMainView abapCiMainView;
	IContinuousIntegrationModel continousIntegrationModel;
	IProject currentProject;

	@Before
	public void before() {

		AbapCiMainView abapCiMainView = Mockito.mock(AbapCiMainView.class);
		continousIntegrationModel = Mockito.mock(IContinuousIntegrationModel.class);
		currentProject = Mockito.mock(IProject.class);
		Mockito.when(currentProject.getName()).thenReturn(TEST_PROJECT_NAME);

		cut = new ContinuousIntegrationPresenter(abapCiMainView, continousIntegrationModel, currentProject);
	}

	@Test
	public void testAddAndRemoveContinousIntegrationConfig() {
		ContinuousIntegrationConfig ciConfig = new ContinuousIntegrationConfig(currentProject.getName(), TEST_PACKAGE,
				false, false);
		cut.addContinousIntegrationConfig(ciConfig);
		cut.removeContinousIntegrationConfig(ciConfig);
	}
}
