package abapci.xml;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import abapci.Exception.ContinuousIntegrationConfigFileParseException;
import abapci.domain.ContinuousIntegrationConfig;

public class ContinuousIntegrationXmlModel extends XmlModel {

	private static final String PROJECT_NAME_ATTRIBUTE = "project_name";
	private static final String PACKAGE_NAME_ATTRIBUTE = "package_name";
	private static final String UNIT_TESTS_ENABLED = "unit_tests_enabled";
	private static final String ATC_ENABLED = "atc_enabled";

	static final String CONTINUOUS_INTEGRATION_MODEL_FILE_NAME = "continuousIntegrationModel.xml";

	private static final String ROOT_XML_TAG = "Root";

	public ContinuousIntegrationXmlModel() {
		super(null, CONTINUOUS_INTEGRATION_MODEL_FILE_NAME);
	}

	public List<ContinuousIntegrationConfig> getAll() throws ContinuousIntegrationConfigFileParseException {
		List<ContinuousIntegrationConfig> continousIntegrationConfigs = new ArrayList<>();

		NodeList nodeList = getNodeListOfDocument();

		for (int nodeNumber = 0; nodeNumber < nodeList.getLength(); nodeNumber++) {
			Element childElement = (Element) nodeList.item(nodeNumber);
			String projectName = childElement.getAttribute(PROJECT_NAME_ATTRIBUTE);
			String packageName = childElement.getAttribute(PACKAGE_NAME_ATTRIBUTE);
			boolean utEnabled = Boolean.valueOf(childElement.getAttribute(UNIT_TESTS_ENABLED));
			boolean atcEnabled = Boolean.valueOf(childElement.getAttribute(ATC_ENABLED));

			if (packageName != null && !packageName.equals("")) {
				continousIntegrationConfigs
						.add(new ContinuousIntegrationConfig(projectName, packageName, utEnabled, atcEnabled));
			}
		}
		return continousIntegrationConfigs;
	}

	public void add(ContinuousIntegrationConfig ciConfig) {

		Document document = xmlWriter.getNormalizedDocument();

		Element rootElement = document.getDocumentElement();
		Element childElement = document.createElement(ROOT_XML_TAG);
		childElement.setAttribute(PROJECT_NAME_ATTRIBUTE, ciConfig.getProjectName());
		childElement.setAttribute(PACKAGE_NAME_ATTRIBUTE, ciConfig.getPackageName());
		childElement.setAttribute(UNIT_TESTS_ENABLED, String.valueOf(ciConfig.getUtActivated()));
		childElement.setAttribute(ATC_ENABLED, String.valueOf(ciConfig.getAtcActivated()));
		rootElement.appendChild(childElement);

		xmlWriter.transformDocument(document);
	}

	public void remove(ContinuousIntegrationConfig ciConfig) throws ContinuousIntegrationConfigFileParseException {

		NodeList nodeList = getNodeListOfDocument();

		Document document = xmlWriter.getNormalizedDocument();
		nodeList = document.getElementsByTagName(ROOT_XML_TAG);

		for (int nodeNumber = 0; nodeNumber < nodeList.getLength(); nodeNumber++) {
			Element childElement = (Element) nodeList.item(nodeNumber);

			if (ciConfig.getProjectName().equals(childElement.getAttribute(PROJECT_NAME_ATTRIBUTE))
					&& ciConfig.getPackageName().equals(childElement.getAttribute(PACKAGE_NAME_ATTRIBUTE))) {
				childElement.getParentNode().removeChild(childElement);
			}
		}

		xmlWriter.transformDocument(document);

	}

	private NodeList getNodeListOfDocument() throws ContinuousIntegrationConfigFileParseException {
		try {
			Document document = xmlWriter.getNormalizedDocument();
			return document.getElementsByTagName(ROOT_XML_TAG);
		} catch (Exception ex) {
			throw new ContinuousIntegrationConfigFileParseException(ex);
		}

	}

}
