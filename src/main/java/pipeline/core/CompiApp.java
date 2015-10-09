package pipeline.core;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import pipeline.validation.DOMparsing;
import pipeline.validation.PipelineParser;



public class CompiApp{

	public static void main(String args[]){

		final String xmlFile = args[0];
		final URL xsdPath = Thread.currentThread().getContextClassLoader().getResource("xsd/pipeline.xsd");
		PipelineParser pipelineParser = new PipelineParser(xmlFile);

		//comprobamos si el fichero XML valida con el fichero XSD
		if(DOMparsing.validateXMLSchema(xmlFile, xsdPath.getPath())){
			

				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder;
				try {
					dBuilder = dbFactory.newDocumentBuilder();
					Document doc = dBuilder.parse(xmlFile);
					//extraemos el contenido de las etiquetas <exec> del fichero XML
					pipelineParser.solveExecXML(doc.getElementsByTagName("exec"));
//					for(String s: pipelineParser.getExecStrings()){
//						System.out.println(s);
//					}

					JAXBContext jaxbContext = JAXBContext.newInstance(Pipeline.class);  
					Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();  
					Pipeline pipeline = (Pipeline) jaxbUnmarshaller.unmarshal(new File(xmlFile));
					
					List<Program> programList = pipeline.getPrograms();
					for(Program p : programList){
						System.out.println(p.toString());
					}
					List<Param> paramList = pipeline.getParams();
					for(Param p : paramList){
						System.out.println(p.toString());
					}
				} catch (ParserConfigurationException | JAXBException | SAXException | IOException e) {
					e.printStackTrace();
				}
				
			
		}
	}
}
