package abapci.coloredProject.model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.core.runtime.IPath;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import abapci.Exception.AbapCiColoredProjectFileParseException;
import abapci.Exception.ProjectColorNotDefinedException;
import abapci.xml.XmlFileLocationHelper;
import abapci.xml.XmlWriter;

public class ColoredProjectModelXml {

	private static final String PROJECT_COLOR_ATTRIBUTE_RED = "Color_RED";
	private static final String PROJECT_COLOR_ATTRIBUTE_GREEN = "Color_GREEN";
	private static final String PROJECT_COLOR_ATTRIBUTE_BLUE = "Color_BLUE";
	private static final String PROJECT_COLOR_ATTRIBUTE_SUPPRESS_COLORING = "suppress_coloring";

	private static final String PROJECT_NAME_ATTRIBUTE = "Name";

	private static final String COLORED_PROJECT_XML_TAG = "ColoredProject";

	static final String COLORED_PROJECTS_FILE_NAME = "newColoredProjectModel.xml";

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
			e.printStackTrace();
		}
	}

	public void addColoredProjectToXML(String name, Color uiColor, boolean suppressColoring) {

		Document document = xmlWriter.getNormalizedDocument();

		Element rootElement = document.getDocumentElement();
		Element coloredProject = document.createElement(COLORED_PROJECT_XML_TAG);
		coloredProject.setAttribute(PROJECT_NAME_ATTRIBUTE, name);
		coloredProject.setAttribute(PROJECT_COLOR_ATTRIBUTE_RED, Integer.toString(uiColor.getRed()));
		coloredProject.setAttribute(PROJECT_COLOR_ATTRIBUTE_GREEN, Integer.toString(uiColor.getGreen()));
		coloredProject.setAttribute(PROJECT_COLOR_ATTRIBUTE_BLUE, Integer.toString(uiColor.getBlue()));
		coloredProject.setAttribute(PROJECT_COLOR_ATTRIBUTE_SUPPRESS_COLORING, Boolean.toString(suppressColoring));
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

	public Color getColorForProject(String projectName)
			throws AbapCiColoredProjectFileParseException, ProjectColorNotDefinedException {

		Color uiColorForProject = null;
		boolean suppressedColoring = false;
		NodeList nodeList = getNodeListOfDocument();

		for (int nodeNumber = 0; nodeNumber < nodeList.getLength(); nodeNumber++) {
			Element coloredProjectElement = (Element) nodeList.item(nodeNumber);
			if (coloredProjectElement.getAttribute(PROJECT_NAME_ATTRIBUTE).equals(projectName)) {
				int red = Integer.parseInt(coloredProjectElement.getAttribute(PROJECT_COLOR_ATTRIBUTE_RED));
				int green = Integer.parseInt(coloredProjectElement.getAttribute(PROJECT_COLOR_ATTRIBUTE_GREEN));
				int blue = Integer.parseInt(coloredProjectElement.getAttribute(PROJECT_COLOR_ATTRIBUTE_BLUE));
				suppressedColoring = Boolean
						.parseBoolean(coloredProjectElement.getAttribute(PROJECT_COLOR_ATTRIBUTE_SUPPRESS_COLORING));
				uiColorForProject = new Color(Display.getCurrent(), red, green, blue);
			}
		}

		if (uiColorForProject == null || suppressedColoring) {
			throw new ProjectColorNotDefinedException();
		} else {
			return uiColorForProject;
		}
	}

	public List<ColoredProject> getColoredProjects() throws AbapCiColoredProjectFileParseException {
		List<ColoredProject> coloredProjects = new ArrayList<>();

		NodeList nodeList = getNodeListOfDocument();

		for (int nodeNumber = 0; nodeNumber < nodeList.getLength(); nodeNumber++) {
			Element coloredProjectElement = (Element) nodeList.item(nodeNumber);
			String name = coloredProjectElement.getAttribute(PROJECT_NAME_ATTRIBUTE);
			if (name != null && name != "") {
				int red = Integer.parseInt(coloredProjectElement.getAttribute(PROJECT_COLOR_ATTRIBUTE_RED));
				int green = Integer.parseInt(coloredProjectElement.getAttribute(PROJECT_COLOR_ATTRIBUTE_GREEN));
				int blue = Integer.parseInt(coloredProjectElement.getAttribute(PROJECT_COLOR_ATTRIBUTE_BLUE));
				boolean suppressedColoring = Boolean
						.parseBoolean(coloredProjectElement.getAttribute(PROJECT_COLOR_ATTRIBUTE_SUPPRESS_COLORING));
				coloredProjects.add(new ColoredProject(name, new Color(Display.getCurrent(), red, green, blue),
						suppressedColoring));
			}
		}
		return coloredProjects;
	}

	public void removeColoredProject(ColoredProject coloredProject) throws AbapCiColoredProjectFileParseException {

		NodeList nodeList = getNodeListOfDocument();

		Document document = xmlWriter.getNormalizedDocument();
		nodeList = document.getElementsByTagName(COLORED_PROJECT_XML_TAG);

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

	private NodeList getNodeListOfDocument() throws AbapCiColoredProjectFileParseException {
		try {
			Document document = xmlWriter.getNormalizedDocument();
			return document.getElementsByTagName(COLORED_PROJECT_XML_TAG);
		} catch (Exception ex) {
			throw new AbapCiColoredProjectFileParseException(ex);
		}

	}

}
