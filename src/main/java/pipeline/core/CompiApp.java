package pipeline.core;

import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

import pipeline.validation.DOMparsing;
import pipeline.validation.PipelineParser;



public class CompiApp{

	public static void main(String args[]){

		URL xsdPath = Thread.currentThread().getContextClassLoader().getResource("xsd/pipeline.xsd");
		PipelineParser pipelineParser = new PipelineParser(args[0]);

		if(DOMparsing.validateXMLSchema(args[0], xsdPath.getPath())){//comprobamos si el fichero XML valida con el fichero XSD
			try{
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(args[0]);
				//extraemos el contenido de las etiquetas <exec> del fichero XML
				pipelineParser.solveExecXML(doc.getElementsByTagName("exec"));
				for(String s: pipelineParser.getExecStrings()){
					System.out.println(s);
				}
			}
			catch(Exception e){
				e.printStackTrace();
			}

		}
	}
}
