package abapci.presenter;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.preference.IPreferenceStore;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import abapci.AbapCiPlugin;
import abapci.domain.ContinuousIntegrationConfig;
import abapci.model.IContinuousIntegrationModel;
import abapci.views.AbapCiMainView;

@RunWith(PowerMockRunner.class)
@PrepareForTest(AbapCiPlugin.class)
public class ContinuousIntegrationPresenterTest {

	private static final String TEST_PROJECT_NAME = "TEST_PROJECT_NAME";
	private static final String TEST_PACKAGE = "TEST_PROJECT_PACKAGE";

	ContinuousIntegrationPresenter cut;

	AbapCiMainView abapCiMainView;
	IContinuousIntegrationModel continousIntegrationModel;
	IProject currentProject;

	public void before() {

		AbapCiPlugin abapCiPlugin = Mockito.mock(AbapCiPlugin.class);
		PowerMockito.spy(AbapCiPlugin.class);
		Mockito.when(AbapCiPlugin.getDefault()).thenReturn(abapCiPlugin);
		PowerMockito.mockStatic(AbapCiPlugin.class);
		Mockito.when(AbapCiPlugin.getDefault()).thenReturn(abapCiPlugin);

		IPreferenceStore preferenceStore = Mockito.mock(IPreferenceStore.class);
		Mockito.when(abapCiPlugin.getPreferenceStore()).thenReturn(preferenceStore);

		AbapCiMainView abapCiMainView = Mockito.mock(AbapCiMainView.class);
		continousIntegrationModel = Mockito.mock(IContinuousIntegrationModel.class);
		currentProject = Mockito.mock(IProject.class);
		Mockito.when(currentProject.getName()).thenReturn(TEST_PROJECT_NAME);

		cut = new ContinuousIntegrationPresenter(abapCiMainView, continousIntegrationModel, currentProject);
	}

	public void testAddAndRemoveContinousIntegrationConfig() {
		ContinuousIntegrationConfig ciConfig = new ContinuousIntegrationConfig(currentProject.getName(), TEST_PACKAGE,
				false, false);
		cut.addContinousIntegrationConfig(ciConfig);
		cut.removeContinousIntegrationConfig(ciConfig);
	}
}
