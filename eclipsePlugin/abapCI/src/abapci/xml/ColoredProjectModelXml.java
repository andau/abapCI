package abapci.xml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.core.runtime.IPath;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import abapci.domain.ColoredProject;
import abapci.domain.UiColor;

public class ColoredProjectModelXml {

	private static final String PROJECT_UI_COLOR_ATTRIBUTE = "UiColor";

	private static final String PROJECT_NAME_ATTRIBUTE = "Name";

	private static final String COLORED_PROJECT_XML_TAG = "ColoredProject";

	static final String COLORED_PROJECTS_FILE_NAME = "coloredProjectModel.xml";

	File coloredProjectsFile;
	XmlWriter xmlWriter;

	public ColoredProjectModelXml() {
		this(null, COLORED_PROJECTS_FILE_NAME);
	}

	public ColoredProjectModelXml(IPath path, String filename) {
		if (path == null) {
			XmlFileLocationHelper xmlFileLocationHelper = new XmlFileLocationHelper();
			path = xmlFileLocationHelper.getStateLocation();
		}
		coloredProjectsFile = new File(path.toFile(), filename);

		xmlWriter = new XmlWriter(coloredProjectsFile);

		if (!fileExists()) {
			createFile();
		}


	}

	public boolean fileExists() {
		return coloredProjectsFile.exists();
	}

	public void createFile() {

		try (FileWriter fileWriter = new FileWriter(coloredProjectsFile.getAbsolutePath())) {

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder;
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.newDocument();
			Node root = doc.createElement(COLORED_PROJECT_XML_TAG);
			doc.appendChild(root);

			xmlWriter.transformDocument(doc);

			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void addColoredProjectToXML(String name, UiColor uiColor) {

		Document document = xmlWriter.getNormalizedDocument();

		Element rootElement = document.getDocumentElement();
		Element coloredProject = document.createElement(COLORED_PROJECT_XML_TAG);
		coloredProject.setAttribute(PROJECT_NAME_ATTRIBUTE, name);
		coloredProject.setAttribute(PROJECT_UI_COLOR_ATTRIBUTE, uiColor.toString());
		rootElement.appendChild(coloredProject);

		xmlWriter.transformDocument(document);
	}

	public void clear() {

		Document document = xmlWriter.getNormalizedDocument();

		NodeList nodeList = document.getElementsByTagName(COLORED_PROJECT_XML_TAG);

		for (int nodeNumber = 0; nodeNumber < nodeList.getLength(); nodeNumber++) {
			Element coloredProjectElement = (Element) nodeList.item(nodeNumber);
			coloredProjectElement.getParentNode().removeChild(coloredProjectElement);
		}

		xmlWriter.transformDocument(document);
	}

	public UiColor getColorForProject(String projectName) {

		UiColor uiColorForProject = UiColor.DEFAULT;

		Document document = xmlWriter.getNormalizedDocument();
		NodeList nodeList = document.getElementsByTagName(COLORED_PROJECT_XML_TAG);

		for (int nodeNumber = 0; nodeNumber < nodeList.getLength(); nodeNumber++) {
			Element coloredProjectElement = (Element) nodeList.item(nodeNumber);
			if (coloredProjectElement.getAttribute(PROJECT_NAME_ATTRIBUTE).equals(projectName)) {
				String uiColorString = coloredProjectElement.getAttribute(PROJECT_UI_COLOR_ATTRIBUTE);
				uiColorForProject = UiColor.valueOf(uiColorString);
			}
		}

		return uiColorForProject;

	}

	public List<ColoredProject> getColoredProjects() {
		List<ColoredProject> coloredProjects = new ArrayList<>();

		Document document = xmlWriter.getNormalizedDocument();
		NodeList nodeList = document.getElementsByTagName(COLORED_PROJECT_XML_TAG);

		for (int nodeNumber = 0; nodeNumber < nodeList.getLength(); nodeNumber++) {
			Element coloredProjectElement = (Element) nodeList.item(nodeNumber);
			String name = coloredProjectElement.getAttribute(PROJECT_NAME_ATTRIBUTE);
			if (name != null && name !=  "") 
			{
				String uiColorString = coloredProjectElement.getAttribute(PROJECT_UI_COLOR_ATTRIBUTE);
				UiColor uiColor = UiColor.valueOf(uiColorString);
	
				coloredProjects.add(new ColoredProject(name, uiColor));
			}
		}
		return coloredProjects;
	}

	public void removeColoredProject(ColoredProject coloredProject) {

		Document document = xmlWriter.getNormalizedDocument();
		NodeList nodeList = document.getElementsByTagName(COLORED_PROJECT_XML_TAG);

		for (int nodeNumber = 0; nodeNumber < nodeList.getLength(); nodeNumber++) {
			Element coloredProjectElement = (Element) nodeList.item(nodeNumber);
			String coloredProjectName = coloredProject.getName();
			String coloredProjectElementName = coloredProjectElement.getAttribute(PROJECT_NAME_ATTRIBUTE);
			if (coloredProjectElementName.equals(coloredProjectName)) {
				coloredProjectElement.getParentNode().removeChild(coloredProjectElement);
			}
		}

		xmlWriter.transformDocument(document);

	}

}
