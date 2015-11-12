package pipeline.validation;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import pipeline.core.Program;

public class Resolver {

	private String xmlParamsFile;

	public Resolver(String file) {
		this.xmlParamsFile = file;
	}

	public void resolveToExecute(Program p, String tag) {
		String parsed = resolveString(tag);
		p.setToExecute(p.getToExecute().replace("${" + tag + "}", parsed));
	}
	
	private String resolveString(String tag) {
		String resolvedString = new String();
		try {
			File xmlFile = new File(this.xmlParamsFile);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(xmlFile);
			doc.getDocumentElement().normalize();
			
			NodeList nodeList = doc.getElementsByTagName(tag);
			if (nodeList.getLength() == 0) {
				throw new IllegalArgumentException("El tag: " + tag + " no existe en el fichero " + this.xmlParamsFile);
			}
			
			for (int i = 0; i < nodeList.getLength(); i++) {
				Node node = nodeList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) node;
					resolvedString=element.getTextContent();
				}
			}
		} catch (SAXException | IOException | ParserConfigurationException e) {
			e.printStackTrace();
		}
		return resolvedString;
	}


}
