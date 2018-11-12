package abapci.ci.views;

import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;

import abapci.ci.views.AbapCiMainView;
import abapci.feature.activeFeature.AtcFeature;
import abapci.feature.activeFeature.JenkinsFeature;
import abapci.feature.activeFeature.UnitFeature;

public class AbapCiMainViewTest {

	AbapCiMainView cut;

	Shell shell = new Shell();

	private final UnitFeature unitFeature = Mockito.mock(UnitFeature.class);
	private final AtcFeature atcFeature = Mockito.mock(AtcFeature.class);
	private final JenkinsFeature jenkinsFeature = Mockito.mock(JenkinsFeature.class);

	@Before
	public void before() {
		cut = new AbapCiMainView(true);
		Mockito.when(unitFeature.isActive()).thenReturn(true);
		Mockito.when(atcFeature.isActive()).thenReturn(true);
		Mockito.when(jenkinsFeature.isActive()).thenReturn(true);
		Whitebox.setInternalState(cut, "unitFeature", unitFeature);
		Whitebox.setInternalState(cut, "atcFeature", atcFeature);
		Whitebox.setInternalState(cut, "jenkinsFeature", jenkinsFeature);
	}

	@Test(expected = NoClassDefFoundError.class)
	public void createPartControl() {
		cut.createPartControl(shell);
	}

}
