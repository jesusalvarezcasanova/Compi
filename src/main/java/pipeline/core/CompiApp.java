package pipeline.core;

import static pipeline.validation.DOMparsing.validateXMLSchema;

import java.io.File;
import java.net.URL;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import pipeline.validation.PipelineParser;
import pipeline.validation.Resolver;

public class CompiApp implements Runnable {

	private Pipeline pipeline;
	private ProgramManager programManager;
	private PipelineParser pipelineParser;
	private Resolver resolver;
	private ExecutorService executorService;
	private ProgramRunnable programRunnable;
	private Future future;
	private final String[] args;

	public CompiApp(final String[] args) {
		this.args = args;
	}

	@Override
	public void run() {
		final String xmlPipelineFile = this.args[0];
		final String xmlParamsFile = this.args[1];
		final String threadNumber = this.args[2];
		final URL xsdPath = Thread.currentThread().getContextClassLoader().getResource("xsd/pipeline.xsd");
		// comprobamos si el fichero XML valida con el fichero XSD
		if (validateXMLSchema(xmlPipelineFile, xsdPath.getPath())) {
			try {
				initializePipeline(xmlPipelineFile, xmlParamsFile, threadNumber);

				// bloque de ejecucion
				synchronized (this) {
					while (!programManager.getProgramsLeft().isEmpty()) {
						for (final Program programToRun : programManager.getRunnablePrograms()) {
							programRunnable = new ProgramRunnable(programToRun, this);
							// marcamos el programa actual con el flag de
							// ejecucion
							programToRun.setRunning(true);

							future = executorService.submit(programRunnable);
							future.get();
							if (future.isDone()) {
								System.out.println("Future done");
							} else {
								System.out.println("Future NOT done");
							}
						}
						System.out.println("DESPUES DEL FOR, PROGRAMS LEFT: " + programManager.getProgramsLeft());
						Thread.currentThread().sleep(500);
					}
					System.out.println("ANTES DEL WAIT");
					// this.wait();
					System.out.println("DESPUES DEL WAIT");
				} // cierre synchronize
					// this.notify();
				executorService.shutdown();
			} catch (JAXBException | InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		} // cierre if validacion XML
		System.out.println("------Fin compiapp------");
	}// cierre run

	private void initializePipeline(final String xmlPipelineFile, final String xmlParamsFile, final String threadNumber)
			throws JAXBException {

		final JAXBContext jaxbContext = JAXBContext.newInstance(Pipeline.class);
		final Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

		// obtenemos los programas y parametros del fichero XML
		pipeline = (Pipeline) jaxbUnmarshaller.unmarshal(new File(xmlPipelineFile));
		programManager = new ProgramManager(pipeline);
		pipelineParser = new PipelineParser();
		resolver = new Resolver(xmlParamsFile);
		executorService = Executors.newFixedThreadPool(Integer.parseInt(threadNumber));

		// comprobamos que los Ids que estan en la etiqueta dependsOn
		// existen
		programManager.checkDependsOnIds();
		pipelineParser.solveExec(pipeline.getPrograms());
		programManager.setDependencies();

		// obtenemos la linea final de ejecucion resolviendo la etiqueta
		// <exec> con los parametros del fichero XML
		for (final Program p : pipeline.getPrograms()) {
			for (final String s : p.getExecStrings()) {
				resolver.resolveToExecute(p, s);
			}
		}
	}

	public Pipeline getPipeline() {
		return pipeline;
	}

	public ProgramManager getProgramManager() {
		return programManager;
	}

	public PipelineParser getPipelineParser() {
		return pipelineParser;
	}

	public Resolver getResolver() {
		return resolver;
	}

	public ExecutorService getExecutorService() {
		return executorService;
	}
}