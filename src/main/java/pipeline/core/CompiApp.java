package pipeline.core;

import java.io.File;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import pipeline.validation.DOMparsing;
import pipeline.validation.PipelineParser;
import pipeline.validation.Resolver;

public class CompiApp {

	public synchronized static void main(String args[]) {

		final String xmlPipelineFile = args[0];
		final String xmlParamsFile = args[1];
		final String threadNumber = args[2];
		System.out.println(threadNumber);
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
				ExecutorService executorService = Executors.newFixedThreadPool(Integer.parseInt(threadNumber));
				// comprobamos que los Ids que estan en la etiqueta dependsOn
				// existen
				// programManager.checkDependsOnIds();
				pipelineParser.solveExec(pipeline.getPrograms());
				// obtenemos la linea final de ejecucion resolviendo la etiqueta
				// <exec> con los parametros del fichero XML
				for (Program p : pipeline.getPrograms()) {
					for (String s : p.getExecStrings()) {
						// System.out.println(p.getToExecute());
						resolver.resolveToExecute(p, s);
					}
				}
				// bloque de ejecucion

				while (!programManager.getProgramsLeft().isEmpty()) {
					for (Program p : programManager.getRunnablePrograms()) {
						ProgramRunnable programRunnable = new ProgramRunnable(p);
						executorService.submit(programRunnable);
						// marcamos el programa actual con el flag de
						// ejecucion
						p.setRunning(true);
						Future future = executorService.submit(programRunnable);
						future.get();
					}
				}

				Thread.currentThread().wait();
				Thread.currentThread().notify();
				// CompiApp.class.wait();
				// CompiApp.class.notify();

				System.out.println("------Fin programa------");
			} catch (JAXBException | InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		} // cierre
			// if
			// validacion
			// XML
	}// cierre main

}