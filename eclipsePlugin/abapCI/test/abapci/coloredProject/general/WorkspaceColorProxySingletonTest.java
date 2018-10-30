package abapci.coloredProject.general;

import static org.junit.Assert.*;

import org.eclipse.core.resources.IProject;
import org.junit.Test;
import org.mockito.Mockito;

import abapci.Exception.AbapCiColoredProjectFileParseException;
import abapci.coloredProject.model.ColoredProject;
import abapci.coloredProject.samples.ColoredProjectTestSample;

public class WorkspaceColorProxySingletonTest {

	IProject project;

	@Test
	public void test() throws AbapCiColoredProjectFileParseException {

		project = Mockito.mock(IProject.class);
		Mockito.when(project.getName()).thenReturn(ColoredProjectTestSample.getRedColoredProject().getName());

		WorkspaceColorProxySingleton cut = WorkspaceColorProxySingleton.getEmptyInstance();

		assertEquals(0, cut.getNumColoredProjects());
		assertEquals(null, cut.getColoring(project));
		assertEquals(false, cut.isConfigured(project));

		ColoredProject coloredProject = ColoredProjectTestSample.getRedColoredProject();
		cut.addOrUpdate(coloredProject);
		assertEquals(1, cut.getNumColoredProjects());
		assertEquals(coloredProject.getColor().getRGB(), cut.getColoring(project).getStatusBarColor().getColor().getRGB());
		assertEquals(true, cut.isConfigured(project));
	}

}
