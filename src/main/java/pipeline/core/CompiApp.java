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

public class CompiApp implements Runnable {

	private Pipeline pipeline;
	private ProgramManager programManager;
	private PipelineParser pipelineParser;
	private Resolver resolver;
	private ExecutorService executorService;
	private ProgramRunnable programRunnable;
	private final String[] args;

	public CompiApp(final String[] args) {
		this.args = args;
	}

	@Override
	public void run() {
		final String xmlPipelineFile = this.args[0];
		final String xmlParamsFile = this.args[1];
		final String threadNumber = this.args[2];
		final URL xsdPath = Thread.currentThread().getContextClassLoader()
				.getResource("xsd/pipeline.xsd");
		// comprobamos si el fichero XML valida con el fichero XSD
		if (DOMparsing.validateXMLSchema(xmlPipelineFile, xsdPath.getPath())) {
			try {
				final JAXBContext jaxbContext = JAXBContext
						.newInstance(Pipeline.class);
				final Unmarshaller jaxbUnmarshaller = jaxbContext
						.createUnmarshaller();
				// obtenemos los programas y parametros del fichero XML
				pipeline = (Pipeline) jaxbUnmarshaller
						.unmarshal(new File(xmlPipelineFile));
				programManager = new ProgramManager(pipeline);
				pipelineParser = new PipelineParser();
				resolver = new Resolver(xmlParamsFile);
				executorService = Executors
						.newFixedThreadPool(Integer.parseInt(threadNumber));
				// comprobamos que los Ids que estan en la etiqueta dependsOn
				// existen
				// programManager.checkDependsOnIds();
				pipelineParser.solveExec(pipeline.getPrograms());
				// obtenemos la linea final de ejecucion resolviendo la etiqueta
				// <exec> con los parametros del fichero XML
				for (final Program p : pipeline.getPrograms()) {
					for (final String s : p.getExecStrings()) {
						resolver.resolveToExecute(p, s);
					}
				}
				// bloque de ejecucion
				synchronized (this) {
					while (!programManager.getProgramsLeft().isEmpty()) {
						for (final Program p : programManager
								.getRunnablePrograms()) {
							programRunnable = new ProgramRunnable(p, this);
							// marcamos el programa actual con el flag de
							// ejecucion
							p.setRunning(true);

							final Future future = executorService
									.submit(programRunnable);
							future.get();
						}
					}
					this.wait();
				} // cierre synchronized
				this.notify();
				System.out.println("------Fin programa------");
			} catch (JAXBException | InterruptedException
					| ExecutionException e) {
				e.printStackTrace();
			}
		} // cierre if validacion XML
	}// cierre run

	public Pipeline getPipeline() {
		return pipeline;
	}

	public void setPipeline(final Pipeline pipeline) {
		this.pipeline = pipeline;
	}

	public ProgramManager getProgramManager() {
		return programManager;
	}

	public void setProgramManager(final ProgramManager programManager) {
		this.programManager = programManager;
	}

	public PipelineParser getPipelineParser() {
		return pipelineParser;
	}

	public void setPipelineParser(final PipelineParser pipelineParser) {
		this.pipelineParser = pipelineParser;
	}

	public Resolver getResolver() {
		return resolver;
	}

	public void setResolver(final Resolver resolver) {
		this.resolver = resolver;
	}

	public ExecutorService getExecutorService() {
		return executorService;
	}

	public void setExecutorService(final ExecutorService executorService) {
		this.executorService = executorService;
	}

}