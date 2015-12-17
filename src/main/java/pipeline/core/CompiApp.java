package pipeline.core;

import static pipeline.validation.DOMparsing.validateXMLSchema;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.xml.sax.SAXException;

import pipeline.interfaces.ProgramExecutionHandler;
import pipeline.validation.PipelineParser;
import pipeline.validation.Resolver;

public class CompiApp implements ProgramExecutionHandler {

	private Pipeline pipeline;
	private ProgramManager programManager;
	private PipelineParser pipelineParser;
	private Resolver resolver;
	private ExecutorService executorService;
	private ProgramRunnable programRunnable;
	private Future<?> future;
	private final String[] args;

	public CompiApp(final String[] args) {
		this.args = args;
	}

	public void run() {
		final String xmlPipelineFile = this.args[0];
		final String xmlParamsFile = this.args[1];
		final String threadNumber = this.args[2];
		final URL xsdPath = Thread.currentThread().getContextClassLoader()
				.getResource("xsd/pipeline.xsd");
		try {
			// comprobamos si el fichero XML valida con el fichero XSD
			validateXMLSchema(xmlPipelineFile, xsdPath.getPath());
			initializePipeline(xmlPipelineFile, xmlParamsFile, threadNumber);

			// bloque de ejecucion
			synchronized (this) {
				while (!programManager.getProgramsLeft().isEmpty()) {
					for (final Program programToRun : programManager
							.getRunnablePrograms()) {
						programRunnable = new ProgramRunnable(programToRun,
								this);
						// marcamos el programa actual con el flag de
						// ejecucion
						this.programStarted(programToRun);
						setFuture(executorService.submit(programRunnable));

					}
					System.out.println("DESPUES DEL FOR, PROGRAMS LEFT: "
							+ programManager.getProgramsLeft());
					this.wait();
				}
			} // cierre synchronize
			executorService.shutdown();
		} catch (JAXBException | InterruptedException | SAXException
				| IOException e) {
			e.printStackTrace();
		}
	}// cierre run

	private void initializePipeline(final String xmlPipelineFile,
			final String xmlParamsFile, final String threadNumber)
					throws JAXBException {

		final JAXBContext jaxbContext = JAXBContext.newInstance(Pipeline.class);
		final Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

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

	synchronized public void programFinished(Program p) {
		this.getProgramManager().programFinished(p);
		this.notify();

	}

	synchronized public void programAborted(Program p, Exception e) {
		this.getProgramManager().programAborted(p, e);
		this.notify();

	}

	public void programStarted(Program p) {
		this.getProgramManager().programStarted(p);

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

	public Future<?> getFuture() {
		return future;
	}

	public void setFuture(Future<?> future) {
		this.future = future;
	}

}