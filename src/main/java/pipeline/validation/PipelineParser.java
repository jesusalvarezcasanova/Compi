package pipeline.validation;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class PipelineParser {

	private String ficheroXML;
	private ArrayList <String> execStrings;
	
	//clase para parsear los parametros ${..} dentro del XML
	public PipelineParser(String fichero){
		this.ficheroXML = fichero;
		this.execStrings = new ArrayList<String>();
	}

	public ArrayList<String> getExecStrings() {
		return execStrings;
	}

	public String getFicheroXML() {
		return ficheroXML;
	}

	public void solveExecXML(NodeList nl){
		//comprobamos si el fichero XML valida con el fichero XSD
		for(int i=0; i<nl.getLength();i++){
			Element node = (Element) nl.item(i);
			cleanExecXML(node.getTextContent().trim());
		}
	}

	private void cleanExecXML(String toClean){
		Pattern p = Pattern.compile("\\{(.*?)\\}");
		Matcher m = p.matcher(toClean);
		while(m.find()) {
		    this.execStrings.add(m.group(1));
		}
	}
}
