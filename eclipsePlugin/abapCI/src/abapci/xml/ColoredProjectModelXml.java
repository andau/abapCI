package abapci.xml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import abapci.domain.ColoredProject;
import abapci.domain.UiColor;

public class ColoredProjectModelXml {
	Bundle bundle = FrameworkUtil.getBundle(abapci.views.AbapCiColoredProjectView.class);
	IPath stateLoc = Platform.getStateLocation(bundle);

	static final String COLORED_PROJECTS_FILE_NAME = "coloredProjectModel.xml";
	File coloredProjectsFile;
	
	public ColoredProjectModelXml() {
		coloredProjectsFile = new File(stateLoc.toFile(), COLORED_PROJECTS_FILE_NAME);

		if (!coloredProjectsFile.exists()) {
			try (FileWriter fileWriter = new FileWriter(stateLoc.toFile() + COLORED_PROJECTS_FILE_NAME)) {

				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder;
				dBuilder = dbFactory.newDocumentBuilder();
				Document doc;
				doc = dBuilder.newDocument();
				Node root = doc.createElement("coloredProjects");
				doc.appendChild(root);
				DOMSource source = new DOMSource(doc);

				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				StreamResult result = new StreamResult(coloredProjectsFile.getPath());
				transformer.transform(source, result);

				fileWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (TransformerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public void addColoredProjectToXML(String name, UiColor uiColor) {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc;
			try {
				doc = dBuilder.parse(coloredProjectsFile);
				doc.normalize();

				Element rootElement = doc.getDocumentElement();
				Element coloredProject = doc.createElement("ColoredProject");
				coloredProject.setAttribute("Name", name);
				coloredProject.setAttribute("UiColor", uiColor.toString());
				rootElement.appendChild(coloredProject);

				DOMSource source = new DOMSource(doc);

				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				StreamResult result = new StreamResult(coloredProjectsFile);
				transformer.transform(source, result);
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TransformerConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TransformerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void clear() {
			
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder;
			try {
				dBuilder = dbFactory.newDocumentBuilder();
				Document doc;
				doc = dBuilder.parse(coloredProjectsFile);
				doc.normalize();
				NodeList nodeList = doc.getElementsByTagName("ColoredProject");

				for (int nodeNumber = 0; nodeNumber < nodeList.getLength(); nodeNumber++) {
					Element coloredProjectElement = (Element) nodeList.item(nodeNumber);
			        coloredProjectElement.getParentNode().removeChild(coloredProjectElement);
			    }
				
				DOMSource source = new DOMSource(doc);

				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				StreamResult result = new StreamResult(coloredProjectsFile);
				transformer.transform(source, result);

			
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TransformerConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TransformerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	public UiColor getColorForProject(String projectName) {

		UiColor uiColorForProject = UiColor.STANDARD; 
		
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc;
			doc = dBuilder.parse(coloredProjectsFile);
			doc.normalize();
			NodeList nodeList = doc.getElementsByTagName("ColoredProject");

			for (int nodeNumber = 0; nodeNumber < nodeList.getLength(); nodeNumber++) {
				Element coloredProjectElement = (Element) nodeList.item(nodeNumber);
				if (coloredProjectElement.getAttribute("Name").equals(projectName)) {
					String uiColorString = coloredProjectElement.getAttribute("UiColor");
					uiColorForProject = UiColor.valueOf(uiColorString);
				}
			}
		
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return uiColorForProject; 

	}

	public List<ColoredProject> getColoredProjects() {
		List<ColoredProject> coloredProjects = new ArrayList<>(); 
		
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc;
			doc = dBuilder.parse(coloredProjectsFile);
			doc.normalize();
			NodeList nodeList = doc.getElementsByTagName("ColoredProject");

			for (int nodeNumber = 0; nodeNumber < nodeList.getLength(); nodeNumber++) {
				Element coloredProjectElement = (Element) nodeList.item(nodeNumber);
				String name = coloredProjectElement.getAttribute("Name");
				String uiColorString = coloredProjectElement.getAttribute("UiColor");
				UiColor uiColor = UiColor.valueOf(uiColorString);
				
				coloredProjects.add(new ColoredProject(name, uiColor)); 
			}
		
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return coloredProjects; 
	}

	public void removeColoredProject(ColoredProject coloredProject) {
		
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc;
			doc = dBuilder.parse(coloredProjectsFile);
			doc.normalize();
			NodeList nodeList = doc.getElementsByTagName("ColoredProject");

			for (int nodeNumber = 0; nodeNumber < nodeList.getLength(); nodeNumber++) {
				Element coloredProjectElement = (Element) nodeList.item(nodeNumber);
				String coloredProjectName = coloredProject.getName();
				String  coloredProjectElementName = coloredProjectElement.getAttribute("Name");
				if (coloredProjectElementName.equals(coloredProjectName))  
				{
					coloredProjectElement.getParentNode().removeChild(coloredProjectElement); 
				}
		    }
			
			DOMSource source = new DOMSource(doc);

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			StreamResult result = new StreamResult(coloredProjectsFile);
			transformer.transform(source, result);

		
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
