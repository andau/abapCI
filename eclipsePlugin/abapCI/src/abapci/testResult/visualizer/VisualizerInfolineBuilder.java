package abapci.testResult.visualizer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.eclipse.core.resources.IProject;

import abapci.domain.AbapPackageTestState;

public class VisualizerInfolineBuilder {

	
	public String buildInfoLine(IProject currentProject, List<AbapPackageTestState> abapPackageTestStatesForCurrentProject, boolean showAtcInfo) {
		int overallTests = abapPackageTestStatesForCurrentProject.stream().mapToInt(item -> item.getNumTests()).sum();
		int overallErrors = abapPackageTestStatesForCurrentProject.stream().mapToInt(item -> item.getAUnitNumErr())
				.sum();
		int overallSuppressed = abapPackageTestStatesForCurrentProject.stream()
				.mapToInt(item -> item.getAUnitNumSuppressed()).sum();

		int overallAtcNum = abapPackageTestStatesForCurrentProject.stream().mapToInt(item -> item.getAtcNumFiles())
				.sum();
		int overallAtcErr = abapPackageTestStatesForCurrentProject.stream().mapToInt(item -> item.getAtcNumErr()).sum();
		int overallAtcWarnings = abapPackageTestStatesForCurrentProject.stream().mapToInt(item -> item.getAtcNumWarn())
				.sum();
		int overallAtcInfos = abapPackageTestStatesForCurrentProject.stream().mapToInt(item -> item.getAtcNumInfo())
				.sum();
		int overallAtcSuppressed = abapPackageTestStatesForCurrentProject.stream()
				.mapToInt(item -> item.getAtcNumSuppressed()).sum();

		String unitTestInfoString = String.format("[%s / %s,%s]", overallTests, overallErrors, overallSuppressed);

		String atcInfoString = showAtcInfo
				? String.format(" [%s / %s,%s,%s,%s]", overallAtcNum, overallAtcErr, overallAtcWarnings,
						overallAtcInfos, overallAtcSuppressed)
				: "";

		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		Date date = new Date();

		return dateFormat.format(date) + ": " + currentProject.getName() + " " + unitTestInfoString + " "
				+ atcInfoString;
	}

}
