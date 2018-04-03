package abapci.xml;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class XmlWriter {

	private File xmlFile; 

	public XmlWriter(File xmlFile) 
	{
		this.xmlFile = xmlFile; 
	}
	public Document getNormalizedDocument() {
		Document doc = null; 

		try {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
			dBuilder = dbFactory.newDocumentBuilder();
		doc = dBuilder.parse(xmlFile);
		doc.normalize();

		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return doc; 

	}
	public void transformDocument(Document document) {
		try {
			DOMSource source = new DOMSource(document);

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer;
				transformer = transformerFactory.newTransformer();
			StreamResult result = new StreamResult(xmlFile);
			transformer.transform(source, result);			
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 		
		
	}

}
