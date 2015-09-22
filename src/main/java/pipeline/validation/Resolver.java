package pipeline.validation;

import java.io.StringBufferInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class Resolver {

	private String ficheroXML;
	private ArrayList <String> execStrings;

	public ArrayList<String> getExecStrings() {
		return execStrings;
	}

	public void setExecStrings(ArrayList<String> execStrings) {
		this.execStrings = execStrings;
	}

	public String getFicheroXML() {
		return ficheroXML;
	}

	//clase para parsear los parametros ${..} dentro del XML
	public Resolver(String fichero){
		this.ficheroXML = fichero;
		this.execStrings = new ArrayList<String>();
	}

	public void solveExecXML(NodeList nl){
		//comprobamos si el fichero XML valida con el fichero XSD
		URL xsdPath = Thread.currentThread().getContextClassLoader().getResource("xsd/pipeline.xsd");

		if(DOMparsing.validateXMLSchema(this.ficheroXML, xsdPath.getPath())){
			try{
				int num = nl.getLength();
				for(int i=0; i<num;i++){
					Element node = (Element) nl.item(i);
					cleanExecXML(node.getTextContent().trim());
				}
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	private void cleanExecXML(String toClean){
//		Pattern pattern = Pattern.compile("(\\$\\{.*?\\})");
		Pattern pattern = Pattern.compile("(?!.*[\\$\\{])(.*)(?=\\})");
		Matcher matcher = pattern.matcher(toClean);
		if (matcher.find()){
			this.execStrings.add(matcher.group(0));
		}
	}
}
