package pipeline.core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

public class ProgramRunnable implements Runnable {
	private final Program program;
	private boolean running = true;
	private CompiApp compiApp;

	public ProgramRunnable(final Program p,
			final CompiApp ca) {
		this.program = p;
		this.compiApp = ca;
	}

	public void finishThread() {
		this.running = false;
	}

	@Override
	public void run() {
		while (running) {
			try {
				System.out.println(program.isRunning());
				final Process process = Runtime.getRuntime()
						.exec(this.program.getToExecute());

				// final BufferedReader stdOut = new BufferedReader(
				// new InputStreamReader(process.getInputStream()));
				// final BufferedReader stdErr = new BufferedReader(
				// new InputStreamReader(process.getErrorStream()));

				// creamos un hilos para la salida estandar
				// startFileLog(stdOut);

				// creamos un hilos para la salida de error
				// startFileErrorLog(stdErr);

				if (process.waitFor() == 0) {
					finishThread();
					programFinished(this.program);
				} else {
					finishThread();
					programAborted(this.program);
				}
			} catch (IOException | InterruptedException e) {
				finishThread();
				programAborted(this.program, e);
			}
		}
	}

	private void startFileLog(final BufferedReader stdOut)
			throws UnsupportedEncodingException, FileNotFoundException {
		if (this.program.getFileLog() != null) {
			final BufferedWriter out = new BufferedWriter(
					new OutputStreamWriter(
							new FileOutputStream(this.program.getFileLog()),
							"utf-8"));
			// Thread that reads std out and feeds the writer given in input
			new Thread() {
				@Override
				public void run() {
					String line;
					try {
						while ((line = stdOut.readLine()) != null) {
							out.write(line
									+ System.getProperty("line.separator"));
						}
					} catch (final Exception e) {
						// throw new Error(e);
					}
					try {
						out.flush();
						out.close();
					} catch (final IOException e) {
					}
				}
			}.start();
		}
	}

	private void startFileErrorLog(final BufferedReader stdErr)
			throws UnsupportedEncodingException, FileNotFoundException {
		if (this.program.getFileErrorLog() != null) {
			final BufferedWriter err = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(
							this.program.getFileErrorLog()), "utf-8"));
			new Thread() {
				@Override
				public void run() {
					String line;
					try {
						while ((line = stdErr.readLine()) != null) {
							err.write(line
									+ System.getProperty("line.separator"));
						}
					} catch (final Exception e) {
						// throw new Error(e);
					}
					try {
						err.flush();
						err.close();
					} catch (final IOException e) {
					}
				}
			}.start();
		}
	}

	public void programFinished(final Program p) {
		System.out.println("program finished OK");
		// marcamos el programa como finalizado
		// this.programManager.getDAG().get(p.getId()).setFinished(true);
		// this.programManager.getDAG().get(p.getId()).setRunning(false);
		// this.programManager.getProgramsLeft().remove(p);
		// compiApp.setProgramManager(this.programManager);
		compiApp.getProgramManager().getDAG().get(p.getId()).setFinished(true);
		compiApp.getProgramManager().getDAG().get(p.getId()).setRunning(false);
		compiApp.getProgramManager().getProgramsLeft().remove(p);
		notify();
	}

	public void programAborted(final Program p) {
		System.out.println("program finished BAD");
		// marcamos el programa como abortado
		// this.programManager.getDAG().get(p.getId()).setAborted(true);
		// this.programManager.getDAG().get(p.getId()).setRunning(false);
		// this.programManager.getProgramsLeft().remove(p);
		compiApp.getProgramManager().getDAG().get(p.getId()).setFinished(true);
		compiApp.getProgramManager().getDAG().get(p.getId()).setAborted(true);
		compiApp.getProgramManager().getDAG().get(p.getId()).setRunning(false);
		compiApp.getProgramManager().getProgramsLeft().remove(p);
		// comprobamos sus dependencias para ver si es necesario abortar la
		// ejecucion del programa o se puede seguir
	}

	public void programAborted(final Program p, final Exception e) {
		System.out.println("program finished BAD - EXCEPTION");
		// marcamos el programa como abortado
		// this.programManager.getDAG().get(p.getId()).setAborted(true);
		// this.programManager.getDAG().get(p.getId()).setRunning(false);
		// this.programManager.getProgramsLeft().remove(p);
		compiApp.getProgramManager().getDAG().get(p.getId()).setFinished(true);
		compiApp.getProgramManager().getDAG().get(p.getId()).setAborted(true);
		compiApp.getProgramManager().getDAG().get(p.getId()).setRunning(false);
		compiApp.getProgramManager().getProgramsLeft().remove(p);
		// comprobamos sus dependencias para ver si es necesario abortar la
		// ejecucion del programa o se puede seguir
	}

}
