package pipeline.core;

import java.io.File;
import java.io.IOException;
import java.net.URL;

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
			try {
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(xmlFile);
				
				//extraemos el contenido de las etiquetas <exec> del fichero XML
				pipelineParser.solveExecXML(doc.getElementsByTagName("exec"));

				JAXBContext jaxbContext = JAXBContext.newInstance(Pipeline.class);  
				Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller(); 
				//Obtenemos todos los programas y parametros del fichero XML
				Pipeline pipeline = (Pipeline) jaxbUnmarshaller.unmarshal(new File(xmlFile));
				ProgramManager programManager = new ProgramManager(pipeline);

				//PRUEBA DE FUNCIONAMIENTO DEL ALGORITMO DE EJECUCION
				while(!programManager.getProgramsLeft().isEmpty()){
					for(Program pr : programManager.getRunnablePrograms()){
						System.out.println(pr.getId());
						programManager.getDAG().get(pr.getId()).setFinished(true);
					}
					try {
						Thread.currentThread();
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

				System.out.println("FIN MAIN");
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			} catch (JAXBException e) {
				e.printStackTrace();
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}//cierre if validacion XML
	}//cierre main
}
