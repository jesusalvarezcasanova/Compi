package pipeline.core;

import java.io.File;
import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import pipeline.validation.DOMparsing;
import pipeline.validation.PipelineParser;
import pipeline.validation.Resolver;

public class CompiApp {

	public static void main(String args[]) {

		final String xmlPipelineFile = args[0];
		final String xmlParamsFile = args[1];
		final URL xsdPath = Thread.currentThread().getContextClassLoader().getResource("xsd/pipeline.xsd");

		// comprobamos si el fichero XML valida con el fichero XSD
		if (DOMparsing.validateXMLSchema(xmlPipelineFile, xsdPath.getPath())) {
			try {
				JAXBContext jaxbContext = JAXBContext.newInstance(Pipeline.class);
				Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
				// obtenemos los programas y parametros del fichero XML
				final Pipeline pipeline = (Pipeline) jaxbUnmarshaller.unmarshal(new File(xmlPipelineFile));
				final ProgramManager programManager = new ProgramManager(pipeline);
				final PipelineParser pipelineParser = new PipelineParser();
				final Resolver resolver = new Resolver(xmlParamsFile);

				// comprobamos que los Ids que estan en la etiqueta dependsOn
				// existen
				programManager.checkDependsOnIds();
				pipelineParser.solveExec(pipeline.getPrograms());
				for (Program p : pipeline.getPrograms()) {
					for (String s : p.getExecStrings()) {
						resolver.resolveToExecute(p, s);
					}
				}
				for (Program p: pipeline.getPrograms()) {
					System.out.println(p.getToExecute());
				}
				
				// while (!programManager.getProgramsLeft().isEmpty()) {
				// for (Program pr : programManager.getRunnablePrograms()) {
				// System.out.println(pr.getId());
				// programManager.getDAG().get(pr.getId()).setFinished(true);
				// }
				// }
				System.out.println("FIN MAIN");
			} catch (JAXBException e) {
				e.printStackTrace();
			}
		} // cierre if validacion XML
	}// cierre main
}
