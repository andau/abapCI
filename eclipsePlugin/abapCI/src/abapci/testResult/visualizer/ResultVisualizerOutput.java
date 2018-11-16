package abapci.testResult.visualizer;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.graphics.Color;

import abapci.domain.AbapPackageTestState;
import abapci.utils.ColorChooser;
import abapci.utils.InvalidItemUtil;
import abapci.utils.StringUtils;

public class ResultVisualizerOutput {

	public static final String FAILING_UNIT_TESTS_HEADER = "Failing unit tests:";
	public static final String FAILING_ATC_CHECKS_HEADER = "Failing atc checks:";

	private static final int MAX_VISUALIZED_INVALID_ITEMS = 10;
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
		backgroundColor = color;
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

		final List<String> failedUnitTestClasses = getFailingUnitTests();

		if (!failedUnitTestClasses.isEmpty()) {
			return FAILING_UNIT_TESTS_HEADER + System.lineSeparator() + System.lineSeparator()
					+ String.join(System.lineSeparator(), failedUnitTestClasses);
		} else {
			final List<String> failedAtcFiles = getFailingAtcChecks();

			if (!failedAtcFiles.isEmpty()) {
				return FAILING_ATC_CHECKS_HEADER + System.lineSeparator() + System.lineSeparator()
						+ String.join(System.lineSeparator(), failedAtcFiles);
			} else {
				return StringUtils.EMPTY;
			}
		}

	}

	private List<String> getFailingUnitTests() {
		final List<String> failedUnitTestClasses = abapPackageTestStatesForCurrentProject.stream()
				.flatMap(item -> item.getUnitTestResult().getActiveErrors().stream())
				.map(item -> InvalidItemUtil.getOutputForUnitTest(item)).limit(MAX_VISUALIZED_INVALID_ITEMS)
				.collect(Collectors.<String>toList());
		return failedUnitTestClasses;
	}

	private List<String> getFailingAtcChecks() {
		final List<String> failedAtcFiles = abapPackageTestStatesForCurrentProject.stream()
				.flatMap(item -> item.getAtcTestResult().getActiveErrors().stream())
				.map(item -> InvalidItemUtil.getOutputForAtcTest(item)).limit(MAX_VISUALIZED_INVALID_ITEMS)
				.collect(Collectors.<String>toList());
		return failedAtcFiles;
	}

}
