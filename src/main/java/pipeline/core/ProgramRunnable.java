package pipeline.core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class ProgramRunnable implements Runnable {
	private Program program;
	private ProgramManager programManager;

	public ProgramRunnable(final Program p) {
		this.program = p;
	}

	public void run() {
		try {
			Process process = Runtime.getRuntime().exec(this.program.getToExecute());
			final BufferedReader stdOut = new BufferedReader(new InputStreamReader(process.getInputStream()));
			final BufferedReader stdErr = new BufferedReader(new InputStreamReader(process.getErrorStream()));
			// creamos un hilos para la salida estandar
			if (this.program.getFileLog() != null) {
				BufferedWriter out = new BufferedWriter(
						new OutputStreamWriter(new FileOutputStream(this.program.getFileLog()), "utf-8"));
				// Thread that reads std out and feeds the writer given in input
				new Thread() {
					public void run() {
						String line;
						try {
							while ((line = stdOut.readLine()) != null) {
								out.write(line + System.getProperty("line.separator"));
							}
						} catch (Exception e) {
							// throw new Error(e);
						}
						try {
							out.flush();
							out.close();
						} catch (IOException e) {
						}
					}
				}.start();
			}
			// creamos un hilos para la salida de error
			if (this.program.getFileErrorLog() != null) {
				BufferedWriter err = new BufferedWriter(
						new OutputStreamWriter(new FileOutputStream(this.program.getFileErrorLog()), "utf-8"));
				new Thread() {
					public void run() {
						String line;
						try {
							while ((line = stdErr.readLine()) != null) {
								err.write(line + System.getProperty("line.separator"));
							}
						} catch (Exception e) {
							// throw new Error(e);
						}
						try {
							err.flush();
							err.close();
						} catch (IOException e) {
						}
					}
				}.start();
			}

			if (process.waitFor() == 0) {
				programFinished(this.program);
			} else {
				programAborted(this.program);
			}
		} catch (IOException | InterruptedException e) {
			programAborted(this.program, e);
		}
	}

	public void programFinished(Program p) {
		// marcamos el programa como finalizado
		this.programManager.getDAG().get(p.getId()).setFinished(true);
		this.programManager.getDAG().get(p.getId()).setRunning(false);
	}

	public void programAborted(Program p) {
		// marcamos el programa como abortado
		this.programManager.getDAG().get(p.getId()).setAborted(true);
		this.programManager.getDAG().get(p.getId()).setRunning(false);
		// comprobamos sus dependencias para ver si es necesario abortar la
		// ejecucion del programa o se puede seguir
	}

	public void programAborted(Program p, Exception e) {
		// marcamos el programa como abortado
		this.programManager.getDAG().get(p.getId()).setAborted(true);
		this.programManager.getDAG().get(p.getId()).setRunning(false);
		// comprobamos sus dependencias para ver si es necesario abortar la
		// ejecucion del programa o se puede seguir
	}

}
