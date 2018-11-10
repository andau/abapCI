package abapci.testResult.visualizer;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.graphics.Color;

import abapci.domain.AbapPackageTestState;
import abapci.domain.InvalidItem;
import abapci.utils.ColorChooser;
import abapci.utils.InvalidItemUtil;

public class ResultVisualizerOutput {

	private String globalTestState;
	private Color backgroundColor;
	private List<AbapPackageTestState> abapPackageTestStatesForCurrentProject;

	private VisualizerInfolineBuilder visualizerInfolineBuilder;
	private boolean showAtcInfo;
	private IProject project;

	private final ColorChooser contrastColorDeterminer = new ColorChooser();

	public void setGlobalTestState(String globalTestState) {
		this.globalTestState = globalTestState;
		visualizerInfolineBuilder = new VisualizerInfolineBuilder();
	}

	public String getGlobalTestState() {
		return globalTestState;
	}

	public void setAbapPackageTestStates(List<AbapPackageTestState> abapPackageTestStatesForCurrentProject) {
		this.abapPackageTestStatesForCurrentProject = abapPackageTestStatesForCurrentProject;
	}

	public List<AbapPackageTestState> getAbapPackageTestStates() {
		return abapPackageTestStatesForCurrentProject;
	}

	public void setCurrentProject(IProject project) {
		this.project = project;
	}

	public void setShowAtcInfo(boolean enabled) {
		showAtcInfo = enabled;
	}

	public String getInfoline() {
		return visualizerInfolineBuilder.buildInfoLine(project, abapPackageTestStatesForCurrentProject, showAtcInfo);
	}

	public void setBackgroundColor(Color color) {
		this.backgroundColor = color;
	}

	public Color getBackgroundColor() {
		return backgroundColor;
	}

	public Color getForegroundColor() {
		return contrastColorDeterminer.getContrastColor(backgroundColor);
	}

	public void setTooltip(String tooltip) {
	}

	public String getTooltip() {

		List<AbapPackageTestState> packagesWithFailedTests = abapPackageTestStatesForCurrentProject.stream()
				.filter(item -> item.getFirstFailedUnitTest() != null)
				.collect(Collectors.<AbapPackageTestState>toList());

		List<AbapPackageTestState> packagesWithFailedAtc = abapPackageTestStatesForCurrentProject.stream()
				.filter(item -> item.getFirstFailedAtc() != null).collect(Collectors.<AbapPackageTestState>toList());

		if (packagesWithFailedTests.size() > 0) {
			List<InvalidItem> firstFailedTests = packagesWithFailedTests.stream()
					.map(item -> item.getFirstFailedUnitTest()).collect(Collectors.toList());
			return InvalidItemUtil.getOutputForUnitTest(firstFailedTests);
		} else {
			if (packagesWithFailedAtc.size() > 0) {
				List<InvalidItem> firstFailedTests = packagesWithFailedAtc.stream()
						.map(item -> item.getFirstFailedAtc()).collect(Collectors.toList());
				return InvalidItemUtil.getOutputForAtcTest(firstFailedTests);
			} else {
				return "";
			}

		}

	}

}
