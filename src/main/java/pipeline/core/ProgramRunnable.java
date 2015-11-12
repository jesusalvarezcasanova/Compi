package pipeline.core;

import pipeline.validation.Resolver;

public class ProgramRunnable implements Runnable {
	private Program program;
	private ProgramManager programManager;
	private Resolver resolver;

	public ProgramRunnable(final Program p, final Resolver r) {
		this.program = p;
		this.resolver = r;
	}

	public void run() {
		// Comprobar si con el XMLResolver se puede resolver el exec del fichero
		// XML sino excepcion
		// Resolver r = new Resolver();
		try {
			// s.programFinished(this.p);
		} catch (Exception e) {
			programAborted(this.program, e);
		}
	}

	public void programFinished(Program p) {
		// marcamos el programa como finalizado
		this.programManager.getDAG().get(p.getId()).setFinished(true);
	}

	public void programAborted(Program p, Exception e) {
		// marcamos el programa como abortado
		this.programManager.getDAG().get(p.getId()).setAborted(true);
		// comprobamos sus dependencias para ver si es necesario abortar la
		// ejecucion del programa o se puede seguir
	}

}
