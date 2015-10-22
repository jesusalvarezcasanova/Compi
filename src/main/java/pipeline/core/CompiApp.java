package pipeline.core;

import java.io.File;
import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import pipeline.validation.DOMparsing;
import pipeline.validation.PipelineParser;



public class CompiApp{

	public static void main(String args[]){

		final String xmlFile = args[0];
		final URL xsdPath = Thread.currentThread().getContextClassLoader().getResource("xsd/pipeline.xsd");
		final PipelineParser pipelineParser = new PipelineParser(xmlFile);

		//comprobamos si el fichero XML valida con el fichero XSD
		if(DOMparsing.validateXMLSchema(xmlFile, xsdPath.getPath())){
			try {
				JAXBContext jaxbContext = JAXBContext.newInstance(Pipeline.class);  
				Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller(); 

				//Obtenemos todos los programas y parametros del fichero XML
				final Pipeline pipeline = (Pipeline) jaxbUnmarshaller.unmarshal(new File(xmlFile));
				final ProgramManager programManager = new ProgramManager(pipeline);

				//extraemos el contenido de las etiquetas <exec> del fichero XML
				pipelineParser.solveExec(pipeline.getPrograms());

				//PRUEBA DE FUNCIONAMIENTO DEL ALGORITMO DE EJECUCION
				while(!programManager.getProgramsLeft().isEmpty()){
					for(Program pr : programManager.getRunnablePrograms()){
						System.out.println(pr.getParsedExec());
						programManager.getDAG().get(pr.getId()).setFinished(true);
					}
					try {
						Thread.currentThread();
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				System.out.println("FIN MAIN");
			} catch (JAXBException e) {
				e.printStackTrace();
			}
		}//cierre if validacion XML
	}//cierre main
}
