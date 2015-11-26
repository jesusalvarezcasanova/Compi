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
	private boolean runningProgram = true;
	private final CompiApp compiApp;

	public ProgramRunnable(final Program program, final CompiApp compiapp) {
		this.program = program;
		this.compiApp = compiapp;
	}

	public void finishThread() {
		this.runningProgram = false;
	}

	@Override
	public void run() {
		// while(runningProgram){
		try {
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
				// finishThread();
				programFinished(this.program);
			} else {
				// finishThread();
				programAborted(this.program);
			}
		} catch (IOException | InterruptedException e) {
			// finishThread();
			programAborted(this.program, e);
		}
		// } //cierre runningProgram

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

	public void programFinished(final Program program) {
		System.out.println("program finished OK");
		// marcamos el programa como finalizado
		compiApp.getProgramManager().getDAG().get(program.getId())
				.setFinished(true);
		compiApp.getProgramManager().getDAG().get(program.getId())
				.setRunning(false);
		compiApp.getProgramManager().getProgramsLeft().remove(program.getId());
	}

	public void programAborted(final Program program) {
		System.out.println("program finished BAD");
		// marcamos el programa como abortado
		compiApp.getProgramManager().getDAG().get(program.getId())
				.setFinished(true);
		compiApp.getProgramManager().getDAG().get(program.getId())
				.setAborted(true);
		compiApp.getProgramManager().getDAG().get(program.getId())
				.setRunning(false);
		compiApp.getProgramManager().getProgramsLeft().remove(program.getId());
		// comprobamos sus dependencias para ver si es necesario abortar la
		// ejecucion del programa o se puede seguir
	}

	public void programAborted(final Program program, final Exception e) {
		System.out.println("program finished BAD - EXCEPTION");
		// marcamos el programa como abortado
		compiApp.getProgramManager().getDAG().get(program.getId())
				.setFinished(true);
		compiApp.getProgramManager().getDAG().get(program.getId())
				.setAborted(true);
		compiApp.getProgramManager().getDAG().get(program.getId())
				.setRunning(false);
		compiApp.getProgramManager().getProgramsLeft().remove(program.getId());
		// comprobamos sus dependencias para ver si es necesario abortar la
		// ejecucion del programa o se puede seguir
	}

}
