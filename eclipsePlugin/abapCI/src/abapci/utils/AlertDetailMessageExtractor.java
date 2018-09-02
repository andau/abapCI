package abapci.utils;

import com.sap.adt.tools.abapsource.abapunit.IAbapUnitAlert;

public class AlertDetailMessageExtractor {

	public static String extractMessageForUi(IAbapUnitAlert unitAlert) {
		StringBuilder extractedMessageBuilder = new StringBuilder();
		extractedMessageBuilder.append(unitAlert.getDetails().size() == 0 ? ""
				: extractUsefulMessage(unitAlert.getDetails().get(0).getText().toString()));

		if (unitAlert.getDetails().get(0).getChildDetails().size() > 0) {
			extractedMessageBuilder.append(
					", " + extractUsefulMessage(unitAlert.getDetails().get(0).getChildDetails().get(0).getText()));
		}
		return extractedMessageBuilder.toString();
	}

	private static String extractUsefulMessage(String detailMessage) {
		return detailMessage.contains("===") ? detailMessage.substring(0, detailMessage.indexOf("===")) : detailMessage;
	}

}
