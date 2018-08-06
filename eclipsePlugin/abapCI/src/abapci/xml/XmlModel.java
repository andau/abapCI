package abapci.xml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.core.runtime.IPath;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public abstract class XmlModel {

	File xmlFile;
	XmlWriter xmlWriter;

	private static final String ROOT_XML_TAG = "Root";

	public XmlModel(IPath path, String filename) {
		if (path == null) {
			XmlFileLocationHelper xmlFileLocationHelper = new XmlFileLocationHelper();
			path = xmlFileLocationHelper.getStateLocation();
		}
		xmlFile = new File(path.toFile(), filename);

		xmlWriter = new XmlWriter(xmlFile);

		if (!fileExists()) {
			createFile();
		}

	}

	public boolean fileExists() {
		return xmlFile.exists();
	}

	public void createFile() {

		try (FileWriter fileWriter = new FileWriter(xmlFile.getAbsolutePath())) {

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder;
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.newDocument();
			Node root = doc.createElement(ROOT_XML_TAG);
			doc.appendChild(root);

			xmlWriter.transformDocument(doc);

			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}

	public void clear() {

		Document document = xmlWriter.getNormalizedDocument();

		NodeList nodeList = document.getElementsByTagName(ROOT_XML_TAG);

		for (int nodeNumber = 0; nodeNumber < nodeList.getLength(); nodeNumber++) {
			Element childElement = (Element) nodeList.item(nodeNumber);
			childElement.getParentNode().removeChild(childElement);
		}

		xmlWriter.transformDocument(document);
	}

}
